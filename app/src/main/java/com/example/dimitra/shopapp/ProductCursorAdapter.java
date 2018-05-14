package com.example.dimitra.shopapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimitra.shopapp.Data.ProductContract.ProductEntry;


/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {
    private Context mContext;
    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        mContext = context;

        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView supplierTextView = view.findViewById(R.id.supplier);
        TextView supplierPhoneTextView = view.findViewById(R.id.supplierPhone);

        final String nameColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final Integer quantityColumnIndex = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        final Integer priceColumnIndex = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        final String supplierColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_SUPPLIER));
        final Integer supplierPhoneColumnIndex = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER));

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(nameColumnIndex);
        priceTextView.setText(Integer.toString(priceColumnIndex));
        quantityTextView.setText(Integer.toString(quantityColumnIndex));
        supplierTextView.setText(supplierColumnIndex);
        supplierPhoneTextView.setText(Integer.toString(supplierPhoneColumnIndex));

        Button sellButton = view.findViewById(R.id.btn_sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    Object obj = view.getTag();
                    String st = obj.toString();
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameColumnIndex);
                    values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceColumnIndex);
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityColumnIndex >= 1? quantityColumnIndex-1: 0);
                    values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplierColumnIndex);
                    values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneColumnIndex);

                    Uri currentPetUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Integer.parseInt(st));

                    int rowsAffected = mContext.getContentResolver().update(currentPetUri, values, null, null);
                    if (rowsAffected == 0 || quantityColumnIndex == 0) {
                        Toast.makeText(mContext, mContext.getString(R.string.sell_product_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Object obj = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        sellButton.setTag(obj);
    }
}
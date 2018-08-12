package com.example.android.inventoryappstage2;

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

import com.example.android.inventoryappstage2.data.BookContract.BookEntry;
/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
      */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
      */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView itemName = (TextView) view.findViewById(R.id.list_item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.list_item_price);
        TextView itemQuantity = (TextView) view.findViewById(R.id.list_item_quantity);
        Button saleButton = (Button) view.findViewById(R.id.btn_sell);

        // Find the columns of book attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);


        // Read the book attributes from the Cursor for the current book
        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);


        // Updated attributes in the views
        itemName.setText(name);
        itemPrice.setText(price);
        itemQuantity.setText(quantity);

        final int stockQuantity = Integer.valueOf(quantity);
        final int currentBookId = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));

        //Set up the sale button to decrement when the user clicks on it
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Let's the user only decrement when the stockQuantity of books is > 0
                if (stockQuantity > 0) {
                    int decrementedQuantity = stockQuantity - 1;

                    //Get the URI with the ID for the row
                    Uri quantityUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, currentBookId);

                    //Update the database with the new quantity value
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY, decrementedQuantity);
                    context.getContentResolver().update(quantityUri, values, null, null);
                } else {
                    //Show an error when the quantity reaches 0
                    Toast.makeText(context, R.string.out_of_stock_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


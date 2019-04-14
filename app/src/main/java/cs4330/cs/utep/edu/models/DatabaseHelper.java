package cs4330.cs.utep.edu.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pricewatcher.db";
    private static final String TABLE_NAME = "pw_items";
    private static final String[] COL = {"i_id", "i_name", "i_price", "i_weblink", "i_image", "i_newprice"};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME +" (i_id INTEGER PRIMARY KEY AUTOINCREMENT, i_name TEXT, i_price FLOAT, i_weblink TEXT, i_image TEXT, i_newprice FLOAT  )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

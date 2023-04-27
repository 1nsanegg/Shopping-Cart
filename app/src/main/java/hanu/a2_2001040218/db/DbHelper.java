package hanu.a2_2001040218.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String dbName = "product.db";
    private static final int version = 1;


    public DbHelper(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table
        sqLiteDatabase.execSQL("CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "thumbnail TEXT, " +
                "name TEXT, " +
                "category TEXT, " +
                "unitPrice TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // drop old table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS friends");
        // create table again
        onCreate(sqLiteDatabase);
    }
}

package hanu.a2_2001040218.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hanu.a2_2001040218.models.Product;

public class ProductManager {
    // singleton instance
    private static ProductManager instance;

    private static final String INSERT_PRODUCT =
            "INSERT INTO" + "products" + "( thumbnail, name, category, unitPrice) VALUES (?,?,?,?)";

    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    private ProductManager(Context context) {
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

//    public List<Product> all() {
//        String sql = "SELECT * FROM products";
//        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
//
//
//    }
}

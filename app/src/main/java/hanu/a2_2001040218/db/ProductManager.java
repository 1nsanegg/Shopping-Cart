package hanu.a2_2001040218.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_2001040218.models.Product;

public class ProductManager {
    // singleton instance
    private static ProductManager instance;

    private static final String INSERT_PRODUCT =
            "INSERT INTO" + "products" + "( thumbnail, name, category, unitPrice) VALUES (?,?,?,?)";

    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;


    private ProductManager(Context context) {
        this.context = context;
    }

    public ProductManager() {
    }

    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    public void saveProduct(Product product) {
        // connect to db

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // insert product into db
        String thumbnail = product.getThumbnail();
        String name = product.getName();
        String category = product.getCategory();
        int unitPrice = product.getUnitPrice();


        // sql statement
        String sql = "INSERT INTO products ( thumbnail, name, category, unitPrice) VALUES (?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        // bind params
        statement.bindString(1, thumbnail);
        statement.bindString(2, name);
        statement.bindString(3, category);
        statement.bindString(4, String.valueOf(unitPrice));

        // execute
        long id = statement.executeInsert();

        // close connection
        db.close();
    }
}

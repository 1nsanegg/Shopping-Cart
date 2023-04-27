package hanu.a2_2001040218;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040218.db.DbHelper;
import hanu.a2_2001040218.models.Product;

public class MyCart extends AppCompatActivity {
    private DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor result;
    private List<Product> products;
    RecyclerView rvCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        // load products from db
        products = loadProducts();
        // recyclerView


    }

        private List<Product> loadProducts() {

        // connect to database
        dbHelper = new DbHelper(MyCart.this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        //manipulate db
        result = sqLiteDatabase.rawQuery("SELECT * FROM products", null);

        int idIndex = result.getColumnIndex("id");
        int thumbnailIndex = result.getColumnIndex("thumbnail");
        int nameIndex = result.getColumnIndex("name");
        int categoryIndex = result.getColumnIndex("category");
        int unitPriceIndex = result.getColumnIndex("unitPrice");

        while(result.moveToNext()) {
            int id = result.getInt(idIndex);
            String thumbnail = result.getString(thumbnailIndex);
            String name = result.getString(nameIndex);
            String category = result.getString(categoryIndex);
            int unitPrice = result.getInt(unitPriceIndex);
            Product product = new Product(id, thumbnail, name, category, unitPrice);
            products.add(product);
        }
        // close connection
        result.close();
        sqLiteDatabase.close();
        return products;


    }

}
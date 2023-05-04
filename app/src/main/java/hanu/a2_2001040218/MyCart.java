package hanu.a2_2001040218;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040218.db.DbHelper;
import hanu.a2_2001040218.db.ProductManager;
import hanu.a2_2001040218.models.Product;

public class MyCart extends AppCompatActivity {
    private DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor result;
    private List<Product> products;
    RecyclerView rvCart;
    CartAdapter cartAdapter;
    TextView totalBill;
    ProductManager productManager;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        productManager = new ProductManager(MyCart.this);
        products = new ArrayList<>();
        // load products from db
        rvCart = findViewById(R.id.rvCart);
        relativeLayout = findViewById(R.id.quantity_sumPrice);
        relativeLayout.setVisibility(View.GONE);
        products = loadProducts();
        if (products.size() > 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        }
        // recyclerView

        rvCart.setLayoutManager(new LinearLayoutManager(MyCart.this
        ));

        // connect to database
        totalBill = findViewById(R.id.totalBill);
        totalBill.setText(String.valueOf(productManager.getBill(products)));
        cartAdapter = new CartAdapter(products, MyCart.this, totalBill, relativeLayout);
        rvCart.setAdapter(cartAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rvCart.addItemDecoration(new CartItemDecoration(spacingInPixels));
        cartAdapter.setItemClickListener(index -> {
            products.remove(index);
            cartAdapter.notifyItemRemoved(index);

        });


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

        while (result.moveToNext()) {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
package hanu.a2_2001040218;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hanu.a2_2001040218.db.DbHelper;
import hanu.a2_2001040218.models.Product;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvProducts;
    List<Product> products;
    ProductAdapter productAdapter;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // btn_add
        products = new ArrayList<Product>();
        // recycler view
        rvProducts = findViewById(R.id.rvProduct);
        // layout items inside


        // add click function to btn
        String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";

        ExecutorService executor = Executors.newFixedThreadPool(4); // shared globally in app
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // do something for result
                String json = loadJSON(url);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // update UI (main thread) with result
                        if (json == null) {
                            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            JSONArray productsJSON = new JSONArray(json);
                            for (int i = 0; i < productsJSON.length(); i++) {
                                JSONObject productsJSONObject = productsJSON.getJSONObject(i);

                                int id = productsJSONObject.getInt("id");
                                String thumbnail = productsJSONObject.getString("thumbnail");
                                String name = productsJSONObject.getString("name");
                                String category = productsJSONObject.getString("category");
                                int unitPrice = productsJSONObject.getInt("unitPrice");


                                Product product = new Product(id, thumbnail, name, category, unitPrice);
                                products.add(product);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rvProducts.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

                        // connect dataset
                        productAdapter = new ProductAdapter(MainActivity.this, products);
                        rvProducts.setAdapter(productAdapter);
                    }
                });
            }
        });
    }
    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_bar) {
            Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


//    private List<Product> loadProducts() {
//
//        // connect to database
//        dbHelper = new DbHelper(MainActivity.this);
//        sqLiteDatabase = dbHelper.getWritableDatabase();
//
//        //manipulate db
//        Corsor result = sqLiteDatabase.rawQuery("SELECT * FROM products", null);
//
//        int idIndex = result.getColumnIndex("id");
//        int thumbnailIndex = result.getColumnIndex("thumbnail");
//        int nameIndex = result.getColumnIndex("name");
//        int categoryIndex = result.getColumnIndex("category");
//        int unitPriceIndex = result.getColumnIndex("unitPrice");
//
//        while(result.moveToNext()) {
//            int id = result.getInt(idIndex);
//            String thumbnail = result.getString(thumbnailIndex);
//            String name = result.getString(emailIndex);
//            String category = result.getString(categoryIndex);
//            String unitPrice = result.getString(unitPriceIndex)
//            Product product = newProduct(id, thumbnail, name, category, unitPrice);
//            products.add(product);
//        }
//        // close connection
//        result.close();
//        sqLiteDatabase.close();
//        return products;
//
//
//    }


}
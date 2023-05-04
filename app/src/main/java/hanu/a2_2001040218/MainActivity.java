package hanu.a2_2001040218;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

import hanu.a2_2001040218.models.Product;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvProducts;
    List<Product> products;
    ProductAdapter productAdapter;
//    DbHelper dbHelper;
//    SQLiteDatabase sqLiteDatabase;
    Button search_btn;
    EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // search
        search_btn = findViewById(R.id.searchBtn);
        searchQuery = findViewById(R.id.keyword);

        // btn_add
        products = new ArrayList<>();
        // recycler view
        rvProducts = findViewById(R.id.rvProduct);

        String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";

        ExecutorService executor = Executors.newFixedThreadPool(4); // shared globally in app
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(() -> {
            // do something for result
            String json = loadJSON(url);
            handler.post(() -> {
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
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                rvProducts.addItemDecoration(new ProductItemDecoration(2, spacingInPixels, true));
                productAdapter.filter("");
                search_btn.setOnClickListener(view -> {
                    String query = searchQuery.getText().toString();
                    productAdapter.filter(query);
                });
            });
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
//            Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, MyCart.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
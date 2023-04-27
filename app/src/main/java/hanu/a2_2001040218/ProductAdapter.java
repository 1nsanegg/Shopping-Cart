package hanu.a2_2001040218;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import hanu.a2_2001040218.db.DbHelper;
import hanu.a2_2001040218.db.ProductManager;
import hanu.a2_2001040218.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    private Context context;
    private ProductManager productManager = new ProductManager();

    public ProductAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(() -> {
            Bitmap bitmap = downloadImage(products.get(position).getThumbnail());
            if (bitmap != null) {
                handler.post(() -> holder.img.setImageBitmap(bitmap));             }         });
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getUnitPrice()));
        holder.btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "eweweqweqw", Toast.LENGTH_LONG).show();
                int id = product.getId();
                String thumbnail = product.getThumbnail();
                String name = product.getName();
                String category = product.getCategory();
                int unitPrice = product.getUnitPrice();
                Product product = new Product(thumbnail, name, category, unitPrice);
                productManager.saveProduct(product);


            }
        });
    }


    private Bitmap downloadImage(String thumbnail) {
        try {
            URL url = new URL(thumbnail);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView productName;
        TextView productPrice;
        ImageButton btn_add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_add = itemView.findViewById(R.id.btn_add);
            img = itemView.findViewById(R.id.img);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);


        }
    }



}

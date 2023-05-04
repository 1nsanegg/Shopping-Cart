package hanu.a2_2001040218;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import hanu.a2_2001040218.db.ProductManager;
import hanu.a2_2001040218.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final List<Product> products;
    private final Context context;
    private static ProductManager productManager;
    private final List<Product> resultList;
    private NumberFormat numberFormat;


    public ProductAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
        this.resultList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        productManager = new ProductManager(context);
        numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        Product product = resultList.get(position);
        holder.bind(product);
    }

    private static Bitmap downloadImage(String thumbnail) {
        try {
            URL url = new URL(thumbnail);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        public void bind(Product product) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            executor.execute(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                if (bitmap != null) {
                    handler.post(() -> img.setImageBitmap(bitmap));
                }
            });
            productName.setText(product.getName());
            productPrice.setText(numberFormat.format(product.getUnitPrice()));
            btn_add.setOnClickListener(view -> {
                Toast.makeText(context, "adding", Toast.LENGTH_LONG).show();



                String thumbnail = product.getThumbnail();
                String name = product.getName();
                String category = product.getCategory();
                int unitPrice = product.getUnitPrice();
                Product productTest = new Product(thumbnail, name, category, unitPrice);

                // check if the product has already added
                if (productManager.checkProductExists(context,name)) {
                    productManager.increaseQuantity(context, name);
                } else {
                    productManager.saveProduct(productTest, context);

                }



            });
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        resultList.clear();
        if (query.isEmpty()) {
            resultList.addAll(products);
        } else {
            query = query.toLowerCase(Locale.getDefault());
            for (Product product : products) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    resultList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }


}

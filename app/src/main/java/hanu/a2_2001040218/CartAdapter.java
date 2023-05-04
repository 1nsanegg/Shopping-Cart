package hanu.a2_2001040218;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hanu.a2_2001040218.db.ProductManager;
import hanu.a2_2001040218.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final List<Product> products;
    private final Context context;
    private ProductManager productManager;
    private itemClickListener listener;
    private final TextView totalBill;
    private NumberFormat numberFormat;

    private final RelativeLayout relativeLayout;

    public CartAdapter(List<Product> products, Context context, TextView totalBill, RelativeLayout relativeLayout) {
        this.products = products;
        this.context = context;
        this.totalBill = totalBill;
        this.relativeLayout = relativeLayout;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_items, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        productManager = new ProductManager(context);
        numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        Product product = products.get(position);
        holder.bind(product);
    }

    private Bitmap downloadImage(String thumbnail) {
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
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView productNameInCart;
        TextView priceInCart;
        ImageButton add_btn;
        TextView quantity;
        ImageButton minus_btn;
        TextView totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            productNameInCart = itemView.findViewById(R.id.productNameInCart);
            priceInCart = itemView.findViewById(R.id.priceInCart);
            add_btn = itemView.findViewById(R.id.add_btn);
            quantity = itemView.findViewById(R.id.quantity);
            minus_btn = itemView.findViewById(R.id.minus_btn);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }

        public void bind(Product product) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            executor.execute(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                if (bitmap != null) {
                    handler.post(() -> image.setImageBitmap(bitmap));
                }
            });
            productNameInCart.setText(product.getName());
            priceInCart.setText(numberFormat.format(product.getUnitPrice()));
            add_btn.setOnClickListener(view -> {
//                    Toast.makeText(context, "add", Toast.LENGTH_LONG).show();
                productManager.increaseQuantity(context, product.getName());
                quantity.setText(String.valueOf(productManager.getQuantity(context, product.getName())));
                totalPrice.setText(String.valueOf(productManager.getTotalPrice(product.getName())));
                getCalBill();

            });
            minus_btn.setOnClickListener(view -> {
//                    Toast.makeText(context, "minus", Toast.LENGTH_LONG).show();
                if (productManager.getQuantity(context, product.getName()) == 1) {
                    listener.itemClick(getBindingAdapterPosition());
                    remove(product.getName());
                    productManager.remove(product, context);
                } else {
                    productManager.decreaseQuantity(context, product.getName());

                }
                quantity.setText(String.valueOf(productManager.getQuantity(context, product.getName())));
                totalPrice.setText(String.valueOf(productManager.getTotalPrice(product.getName())));
                getCalBill();
            });
            quantity.setText(String.valueOf(productManager.getQuantity(context, product.getName())));
            totalPrice.setText(String.valueOf(productManager.getTotalPrice(product.getName())));

        }
    }

    public void getCalBill() {
        int totalBill1 = 0;
        for (Product product : products) {
            totalBill1 += productManager.getTotalPrice(product.getName());
        }
        totalBill.setText(numberFormat.format(totalBill1));
        if (totalBill1 == 0) {
            relativeLayout.setVisibility(View.GONE);
        }
    }
    public interface itemClickListener {
        void itemClick(int position);

    }
    public void setItemClickListener(itemClickListener itemClickListener) {
        listener = itemClickListener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void remove(String name) {
        products.removeIf(product -> product.getName().equals(name));
        notifyDataSetChanged();
    }

}

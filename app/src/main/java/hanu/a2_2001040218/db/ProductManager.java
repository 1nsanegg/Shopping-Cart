package hanu.a2_2001040218.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_2001040218.MyCart;
import hanu.a2_2001040218.ProductAdapter;
import hanu.a2_2001040218.models.Product;

public class ProductManager {
    // singleton instance
    private static ProductManager instance;

    private static final String INSERT_PRODUCT =
            "INSERT INTO " + "products" + "(thumbnail, name, category, unitPrice) VALUES (?, ?, ?, ?)";

    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;


    public ProductManager(Context context) {
        this.dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }
    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    public boolean saveProduct(Product product, Context context) {
        // connect to db

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // insert product into db
        String thumbnail = product.getThumbnail();
        String name = product.getName();
        String category = product.getCategory();
        int unitPrice = product.getUnitPrice();


        // sql statement
        String sql = "INSERT INTO products ( thumbnail, name, category, unitPrice, quantity, totalPrice) VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        // bind params
        statement.bindString(1, thumbnail);
        statement.bindString(2, name);
        statement.bindString(3, category);
        statement.bindString(4, String.valueOf(unitPrice));
        statement.bindString(5, "1");
        statement.bindString(6, String.valueOf(unitPrice));

        // execute
        long id = statement.executeInsert();

        // close connection
        db.close();
        return id > 0;
    }

    public int getQuantity(Context context, String name ) {
        // connect to database
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();

        // get quantity
        cursor = sqLiteDatabase.rawQuery("SELECT quantity FROM products WHERE name ='" + name + "'", null);
        int quantityIndex = cursor.getColumnIndex("quantity");
        int quantity = 0;
        if (cursor != null && cursor.moveToFirst()) {

            quantity = Integer.parseInt(cursor.getString(quantityIndex));
        } else {
            // cursor is null
        }
        cursor.close();
        db1.close();

        return quantity;
    }

    public int getTotalPrice(Context context, String name) {
        // connect to database
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();

        // get total price
        cursor = sqLiteDatabase.rawQuery("SELECT totalPrice FROM products WHERE name ='" + name +"'", null);
        int totalPriceIndex = cursor.getColumnIndex("totalPrice");
        int totalPrice = 0;
        if (cursor != null && cursor.moveToFirst()) {
            totalPrice = Integer.parseInt(cursor.getString(totalPriceIndex));
        }
        return totalPrice;
    }
    public void increaseQuantity (Context context, String name) {

        // connect to database
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // get the quantity and totalPrice
        cursor = sqLiteDatabase.rawQuery("SELECT unitPrice, quantity, totalPrice FROM products WHERE name ='" + name +"'" , null, null);
        int unitPriceIndex = cursor.getColumnIndex("unitPrice");
        int quantityIndex = cursor.getColumnIndex("quantity");
        int totalPriceIndex = cursor.getColumnIndex("totalPrice");

        while (cursor.moveToNext()) {

            int quantityBefore = Integer.parseInt(cursor.getString(quantityIndex));
            int totalPriceBefore = Integer.parseInt(cursor.getString(totalPriceIndex));
            int unitPrice = Integer.parseInt(cursor.getString(unitPriceIndex));
            String quantityAfter = String.valueOf((quantityBefore + 1));
            String totalPrice = String.valueOf((Integer.parseInt(quantityAfter) * unitPrice));

            // update quantity and totalPrice
            String addQuantityStatement = "UPDATE products SET quantity =" + quantityAfter +
                    ", totalPrice = " + totalPrice + " " +
                    "WHERE name ='" + name + "'";

            db.execSQL(addQuantityStatement);
        }



        cursor.close();
        db.close();
        db1.close();

    }
    public void decreaseQuantity (Context context, String name) {

        // connect to database
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // get the quantity and totalPrice
        cursor = sqLiteDatabase.rawQuery("SELECT unitPrice, quantity, totalPrice FROM products WHERE name ='" + name +"'" , null, null);
        int unitPriceIndex = cursor.getColumnIndex("unitPrice");
        int quantityIndex = cursor.getColumnIndex("quantity");
        int totalPriceIndex = cursor.getColumnIndex("totalPrice");

        while (cursor.moveToNext()) {

            int quantityBefore = Integer.parseInt(cursor.getString(quantityIndex));
            int totalPriceBefore = Integer.parseInt(cursor.getString(totalPriceIndex));
            int unitPrice = Integer.parseInt(cursor.getString(unitPriceIndex));
            String quantityAfter = String.valueOf((quantityBefore - 1));
            String totalPrice = String.valueOf((Integer.parseInt(quantityAfter) * unitPrice));

            // update quantity and totalPrice
            String addQuantityStatement = "UPDATE products SET quantity =" + quantityAfter +
                        ", totalPrice = " + totalPrice + " " +
                        "WHERE name ='" + name + "'";

            db.execSQL(addQuantityStatement);


        }



        cursor.close();
        db.close();
        db1.close();

    }


    public boolean checkProductExists(Context context, String productName) {
        //connect to database
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();



        String selectQuery = "SELECT * FROM products WHERE name ='" + productName + "'" ;

        Cursor cursor = db.rawQuery(selectQuery, null);

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
//    public boolean add(Product product) {
//        SQLiteStatement statement = sqLiteDatabase.compileStatement(INSERT_PRODUCT);
//        statement.bindString(1, product.getThumbnail());
//        statement.bindString(2, product.getName());
//        statement.bindString(3, product.getCategory());
//        statement.bindString(4, String.valueOf(product.getUnitPrice()));
//        int id = (int) statement.executeInsert();
//        if (id > 0) {
//            product.setId((id));
//            return true;
//        }
//        return false;
//    }

    public void remove(Product product, Context context) {

        // connect to database
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // get id of product
        String productName = product.getName();

        // sql statement
        db.delete("products", "name = ?", new String[] { productName });
        db.close();

        // update view

    }


    

}


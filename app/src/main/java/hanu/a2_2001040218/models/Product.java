package hanu.a2_2001040218.models;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Product {
    int id;
    String thumbnail;
    String name;
    String category;
    int unitPrice;

    public Product(int id, String thumbnail, String name, String category, int unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public Product(String thumbnail, String name, String category, int unitPrice) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && unitPrice == product.unitPrice && thumbnail.equals(product.thumbnail) && name.equals(product.name) && category.equals(product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, thumbnail, name, category, unitPrice);
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", thumbnail='" + thumbnail + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}

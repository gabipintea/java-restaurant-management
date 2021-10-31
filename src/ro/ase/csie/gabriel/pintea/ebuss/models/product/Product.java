package ro.ase.csie.gabriel.pintea.ebuss.models.product;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String title;
    protected int units;
    protected String mU;
    protected float price;

    public Product(String title, int units, String mU, float price) {
        this.title = title;
        this.units = units;
        this.mU = mU;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public abstract String showDescription();
}

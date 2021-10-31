package ro.ase.csie.gabriel.pintea.ebuss.models.order;

import ro.ase.csie.gabriel.pintea.ebuss.export.ExportZ;
import ro.ase.csie.gabriel.pintea.ebuss.models.product.Product;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private long orderId;
    private HashMap<String, Integer> products;
    private long waiterId;
    private float totalPrice;
    private LocalDateTime timestamp;
    private boolean isReady;
    private int tableNo;

    public static long noOrders = 0L;

    public Order() {
        this.products = new HashMap<String, Integer>();
    }

    public Order(HashMap<String, Integer> products, long waiterId, int tableNo) {
        this.orderId = ++noOrders;

        this.products = new HashMap<String, Integer>();
        this.products.putAll(products);

        this.waiterId = waiterId;
        this.tableNo = tableNo;
        this.isReady = false;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady() {
        isReady = true;
    }

    public void setTotalPrice(HashMap<String, Product> productList) {
        float value = 0;
        for(String key : products.keySet()) {
            value += (products.get(key)*productList.get(key).getPrice());
        }
        this.totalPrice = value;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Integer> products) {
        this.products.putAll(products);
    }

    public long getWaiterId() {
        return waiterId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String displayOrder(HashMap<String, Product> productList) {
        System.out.println("Order{" +
                "orderId=" + orderId +
                ", waiterId=" + waiterId +
                ", timestamp=" + timestamp +
                ", isReady=" + isReady +
                ", tableNo=" + tableNo +
                '}');
        System.out.println("Items ordered:");
        for(String key : products.keySet()) {
            System.out.println(products.get(key) + "x " + productList.get(key).showDescription() + " - " + productList.get(key).getPrice() + " lei");
        }
        System.out.println("Total price: " + this.totalPrice);
        return isReady?"----------done-------------":"---------pending-----------";
    }

    public void exportOrder(HashMap<String, Product> productList, ExportZ exportZ) throws IOException {
        exportZ.WriteLine("Order{" +
                "orderId=" + orderId +
                ", waiterId=" + waiterId +
                ", timestamp=" + timestamp +
                ", isReady=" + isReady +
                ", tableNo=" + tableNo +
                '}');
        exportZ.WriteLine("Items ordered:");
        for(String key : products.keySet()) {
            exportZ.WriteLine(products.get(key) + "x " + productList.get(key).showDescription() + " - " + productList.get(key).getPrice() + " lei");
        }
        exportZ.WriteLine("Total price: " + this.totalPrice);
        exportZ.WriteLine(isReady?"----------done-------------":"---------pending-----------");
        exportZ.WriteLine("\n");
    }
}

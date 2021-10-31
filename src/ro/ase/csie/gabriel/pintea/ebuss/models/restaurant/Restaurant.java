package ro.ase.csie.gabriel.pintea.ebuss.models.restaurant;

import ro.ase.csie.gabriel.pintea.ebuss.models.employee.Waiter;
import ro.ase.csie.gabriel.pintea.ebuss.models.foodmenu.FoodMenu;
import ro.ase.csie.gabriel.pintea.ebuss.models.order.Order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Restaurant implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Waiter> waiters;
    private Waiter loggedWaiter;
    private FoodMenu foodMenu;
    private List<Order> activeOrders;
    private long noEmployees;
    private long noOrders;

    public Restaurant(HashMap<String, Waiter> waiters, Waiter loggedWaiter, FoodMenu foodMenu, List<Order> activeOrders, long noEmpl, long noOrd) {
        this.waiters = waiters;
        this.loggedWaiter = loggedWaiter;
        this.foodMenu = foodMenu;
        this.activeOrders = activeOrders;
        this.noEmployees = noEmpl;
        this.noOrders = noOrd;
    }

    public long getNoOrders() {
        return noOrders;
    }

    public long getNoEmployees() {
        return noEmployees;
    }

    public HashMap<String, Waiter> getWaiters() {
        return waiters;
    }

    public Waiter getLoggedWaiter() {
        return loggedWaiter;
    }

    public FoodMenu getFoodMenu() {
        return foodMenu;
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }
}

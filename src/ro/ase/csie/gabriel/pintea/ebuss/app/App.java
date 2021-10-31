package ro.ase.csie.gabriel.pintea.ebuss.app;

import ro.ase.csie.gabriel.pintea.ebuss.exceptions.*;
import ro.ase.csie.gabriel.pintea.ebuss.export.ExportZ;
import ro.ase.csie.gabriel.pintea.ebuss.models.employee.Employee;
import ro.ase.csie.gabriel.pintea.ebuss.models.employee.Waiter;
import ro.ase.csie.gabriel.pintea.ebuss.models.employee.emplType;
import ro.ase.csie.gabriel.pintea.ebuss.models.foodmenu.FoodMenu;
import ro.ase.csie.gabriel.pintea.ebuss.models.order.Order;
import ro.ase.csie.gabriel.pintea.ebuss.models.product.Product;
import ro.ase.csie.gabriel.pintea.ebuss.models.product.ProductFactory;
import ro.ase.csie.gabriel.pintea.ebuss.models.product.ProductType;
import ro.ase.csie.gabriel.pintea.ebuss.models.restaurant.Restaurant;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.isNull;

public class App {
    public static HashMap<String, Waiter> waiters;
    public static Waiter loggedWaiter;
    public static FoodMenu foodMenu;
    public static List<Order> activeOrders;

    public static Scanner in;

    public static void start() {
        File data = new File("data.bin");
        if(data.exists()) {
            System.out.println("Existing data found. Do you want to import data (any key) or start fresh (FRESH)?");
            in = new Scanner(System.in);
            String opt = in.nextLine();

            if(!opt.equals("FRESH")) {
                ObjectInputStream is = null;
                try{
                    is = new ObjectInputStream(new FileInputStream("data.bin"));
                    Restaurant restaurant = (Restaurant)is.readObject();
                    waiters = restaurant.getWaiters();
                    loggedWaiter = restaurant.getLoggedWaiter();
                    foodMenu = restaurant.getFoodMenu();
                    activeOrders = restaurant.getActiveOrders();
                    if(!activeOrders.isEmpty()) {
                        Order.noOrders = restaurant.getNoOrders();
                    }
                    Employee.noEmployees = restaurant.getNoEmployees();

                    System.out.println("Restored");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null)
                        try
                        {
                            is.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                }
            } else {
                if(data.delete()) {
                    System.out.println("Fresh installation");
                } else {
                    System.out.println("Failed to re-initialize program");
                }
            }
        }
        menuStart();
    }
    public static void exit() {
        if(!isNull(waiters) && !isNull(loggedWaiter) && !isNull(foodMenu) && !isNull(activeOrders)) {
            Restaurant restaurant = new Restaurant(waiters, loggedWaiter, foodMenu, activeOrders, Employee.noEmployees, Order.noOrders);

            ObjectOutputStream os = null;
            try {
                os = new ObjectOutputStream(new FileOutputStream("data.bin"));
                os.writeObject(restaurant);

                System.out.println("Data has been saved!");
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                if( os != null ) {
                    try{
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Goodbye!");
        System.exit(0);
    }
    public static void menuStart() {
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Gabriel\'s Restaurant");
        System.out.println("-------------------------------------");
        System.out.println("Select an option:");
        System.out.println("1) Login as Waiter");
        System.out.println("2) Create a Waiter");
        System.out.println("0) Exit");

        in = new Scanner(System.in);
        int opt = in.nextInt();

        while(true) {
            switch (opt) {
                case 1:
                    try {
                        loginWaiter();
                    } catch(StartMenuException e) {
                        System.out.println(e.getMessage());
                        if(e.getErrCode() == 401) {
                            opt = 2;
                        } else if(e.getErrCode() == 404) {
                            opt = -1;
                        }
                    }
                    break;
                case 2:
                    createWaiter();
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("\n");
                    System.out.println("\n");
                    System.out.println("Try another menu option");
                    System.out.println("1) Login as Waiter");
                    System.out.println("2) Create a Waiter");
                    System.out.println("0) Exit");
                    opt = in.nextInt();
            }
        }
    }
    public static void menuLogged() {
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Gabriel\'s Restaurant - " + loggedWaiter.toString());
        System.out.println("-------------------------------------");
        System.out.println("Select an option:");
        System.out.println("1) Show active orders");
        System.out.println("2) Create order");
        System.out.println("3) See/edit food menu");
        System.out.println("4) End the day and print Z report");
        System.out.println("5) Change PIN code (recommended)");
        System.out.println("0) Back");

        in = new Scanner(System.in);
        int opt = in.nextInt();

        while(true) {
            switch (opt) {
                case 1:
                    showActiveOrders();
                    break;
                case 2:
                    createOrder();
                    break;
                case 3:
                    editFoodMenu();
                    break;
                case 4:
                    exportZ();
                    break;
                case 5:
                    changePIN();
                    break;
                case 0:
                    menuStart();
                    break;
                default:
                    System.out.println("Try another option");
                    opt = in.nextInt();
            }
        }
    }

    public static void createWaiter() {
        System.out.println("\n");
        System.out.println("\n");
        in = new Scanner(System.in);

        System.out.println("Waiter\'s name:");
        String name = in.nextLine();

        Waiter newEmployee = new Waiter(name, emplType.WAITER, false, null);
        if(isNull(waiters) || waiters.isEmpty()) {
            waiters = new HashMap<String, Waiter>();
        }
        waiters.put(name, newEmployee);
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Waiter created with default PIN code (0000)");
        menuStart();
    }
    public static void loginWaiter() throws StartMenuException {
        System.out.println("\n");
        System.out.println("\n");
        if(isNull(waiters) || waiters.isEmpty()) {
            throw new StartMenuException("No registered waiters. Please create a new waiter.", 401);
        }

        System.out.println("Login");
        System.out.println("-------------------------------------");
        System.out.println("Name:");
        in = new Scanner(System.in);
        String name = in.nextLine();
        String pinCode="0000";

        if(waiters.containsKey(name)) {
            boolean sizeOk = false;
            boolean charsOk = false;
            boolean pinOk = false;
            System.out.println("PIN code: (4 digits - default 0000)");
            while(!sizeOk || !charsOk || !pinOk) {
                pinCode = in.nextLine();

                sizeOk = pinCode.length() == 4;
                charsOk = pinCode.matches("^[0-9]*$");

                if(!sizeOk){
                    System.out.println("Exactly 4 digits allowed");
                } else if(!charsOk) {
                    System.out.println("Only numerical digits allowed");
                } else if(!waiters.get(name).login(name,pinCode)) {
                    System.out.println("Incorrect PIN. Please try again.");
                } else {
                    System.out.println("\n");
                    System.out.println("\n");
                    System.out.println("Successful login");
                    loggedWaiter = waiters.get(name);
                    menuLogged();
                    break;
                }
            }
        } else {
            throw new StartMenuException("No waiter with this name.", 404);
        }
    }

    private static void changePIN() {
        String pinCode, name;
        name = loggedWaiter.getName();

        boolean sizeOk = false;
        boolean charsOk = false;
        boolean pinOk = false;
        System.out.println("Provide the old PIN");
        while(!sizeOk || !charsOk || !pinOk) {
            in = new Scanner(System.in);
            pinCode = in.nextLine();

            sizeOk = pinCode.length() == 4;
            charsOk = pinCode.matches("^[0-9]*$");

            if(!sizeOk){
                System.out.println("Exactly 4 digits allowed");
            } else if(!charsOk) {
                System.out.println("Only numerical digits allowed");
            } else if(!waiters.get(name).login(name,pinCode)) {
                System.out.println("Incorrect PIN. Please try again.");
            } else {
                String oldPin = pinCode;
                boolean newSizeOk = false;
                boolean newCharsOk = false;
                boolean newPinOk = false;
                System.out.println("Provide the new PIN");
                while(!newSizeOk || !newCharsOk || !newPinOk) {
                    in = new Scanner(System.in);
                    pinCode = in.nextLine();

                    newSizeOk = pinCode.length() == 4;
                    newCharsOk = pinCode.matches("^[0-9]*$");

                    if(!newSizeOk){
                        System.out.println("Exactly 4 digits allowed");
                    } else if(!newCharsOk) {
                        System.out.println("Only numerical digits allowed");
                    } else if(oldPin.equals(pinCode)) {
                        System.out.println("Incorrect PIN. The new PIN cannot be the same.");
                    } else {
                        System.out.println("\n");
                        System.out.println("\n");
                        System.out.println("Successful change");
                        loggedWaiter.setPinCode(pinCode);
                        menuStart();
                        break;
                    }
                }
            }
        }
    }

    private static void showActiveOrders() {
        if(isNull(activeOrders) || activeOrders.isEmpty()) {
            System.out.println("No active order");
            menuLogged();
        }
        for(Order o : activeOrders) {
            System.out.println(o.displayOrder(foodMenu.getProductList()));
        }
        String opt;
        do{
            System.out.println("Type the Order ID of an order to mark it done, type \'all\' to mark all orders done or type \'n\' if none of the above is ready:");
            in = new Scanner(System.in);
            opt = in.nextLine();

            if(opt.equals("all")) {
                for(Order o : activeOrders) {
                    o.setReady();
                }
                System.out.println("All order are DONE.");
            }

            if(!opt.equals("n") && opt.matches("^[0-9]*$")) {
                int orderId;
                orderId = Integer.parseInt(opt);
                for(Order o : activeOrders) {
                    if(orderId == o.getOrderId()) {
                        o.setReady();
                        System.out.println("\n");
                        System.out.println("Order " + orderId + " is DONE.");
                    }
                }
            }
        } while (!opt.equals("n"));
        menuLogged();
    }

    public static void createOrder() {
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("New order");
        System.out.println("-------------------------------------");
        System.out.println("\n");
        System.out.println("Menu");
        System.out.println("-------------------------------------");

        String key, opt;
        int qty, tableNo=0;
        HashMap<String, Integer> orderProducts;
        if(isNull(foodMenu) || foodMenu.getSize() <= 0) {
            foodMenu = new FoodMenu();
        }
        System.out.println("\n");
        System.out.println("\n");
        System.out.println(foodMenu.toString());


       if(foodMenu.getSize() > 0) {
           orderProducts = new HashMap<String, Integer>();
           in = new Scanner(System.in);

           System.out.println("Type the table number");
           try {
               tableNo = in.nextInt();
           } catch (Exception e) {
               System.out.println("Provide numerical input");
               createOrder();
           }

           do {
               do {
                   System.out.println("Type the 6 chars ID of the item you want to order or \"abort\" to exit");
                   in = new Scanner(System.in);
                   key = in.nextLine();
               } while(!foodMenu.getProductList().containsKey(key) && !key.equals("abort"));

               if(key.equals("abort")) {
                   menuLogged();
                   break;
               } else {
                   System.out.println("How many?");
                   qty = in.nextInt();

                   if(orderProducts.containsKey(key)) {
                       orderProducts.put(key,orderProducts.get(key) + qty);
                   } else {
                       orderProducts.put(key, qty);
                   }
               }
               do {
                   System.out.println("Add more? (y/n)");
                   in = new Scanner(System.in);
                   opt = in.nextLine();
               } while(!opt.equals("y") && !opt.equals("n"));

           } while(!opt.equals("n"));

           Order newOrder = new Order(orderProducts, loggedWaiter.getEmplId(), tableNo);
           newOrder.setTotalPrice(foodMenu.getProductList());

           loggedWaiter.setAllocTable(tableNo);
           if(isNull(activeOrders)) {
               activeOrders = new ArrayList<Order>();
           }
           activeOrders.add(newOrder);
           System.out.println("\n");
           System.out.println("\n");
           System.out.println("Order created successfully");
       } else {
           System.out.println("Please create a menu first.");
       }

       menuLogged();

    }
    public static void editFoodMenu() {
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Food menu");
        System.out.println("-------------------------------------");
        System.out.println("Select an option:");
        System.out.println("1) Show menu");
        System.out.println("2) Add item");
        System.out.println("3) Remove item");
        System.out.println("0) Back");

        in = new Scanner(System.in);
        int opt = in.nextInt();

        while(true) {
            switch (opt) {
                case 1:
                    showFoodMenu();
                    break;
                case 2:
                    addItemToFoodMenu();
                    break;
                case 3:
                    removeItemFromFoodMenu();
                    break;
                case 0:
                    menuLogged();
                    break;
                default:
                    System.out.println("Try another option");
                    opt = in.nextInt();
            }
        }
    }

    public static void showFoodMenu() {
        if(isNull(foodMenu)) {
            foodMenu = new FoodMenu();
        }
        System.out.println("\n");
        System.out.println("\n");
        System.out.println(foodMenu.toString());
        editFoodMenu();
    }

    public static void addItemToFoodMenu() {
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Add a new item to the menu");
        System.out.println("-------------------------------------");

        String title;
        float price;
        Product newProduct;
        in = new Scanner(System.in);

        System.out.println("Provide a title");
        title = in.nextLine();

        System.out.println("Provide a price");
        price = (float)in.nextFloat();

        System.out.println("What type of product category you need? Select an option:");
        System.out.println("1) Breakfast item");
        System.out.println("2) Lunch item");
        System.out.println("3) Dinner item");
        System.out.println("4) Drink item");


        int opt = in.nextInt();

        while(true) {
            switch (opt) {
                case 1:
                    newProduct = ProductFactory.getProduct(title,price, ProductType.BREAKFAST);
                    addItem(newProduct);
                    break;
                case 2:
                    newProduct = ProductFactory.getProduct(title, price, ProductType.LUNCH);
                    addItem(newProduct);
                    break;
                case 3:
                    newProduct = ProductFactory.getProduct(title, price, ProductType.DINNER);
                    addItem(newProduct);
                    break;
                case 4:
                    newProduct = ProductFactory.getProduct(title, price, ProductType.DRINK);
                    addItem(newProduct);
                    break;
                default:
                    System.out.println("Try another option");
                    opt = in.nextInt();
            }
        }
    }
    public static void addItem(Product newProduct) {
        if(isNull(foodMenu)) {
            foodMenu = new FoodMenu();
        }
        if(foodMenu.addProduct(newProduct)) {
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("Item added successfully");
        } else {
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("An error occurred");
        }
        editFoodMenu();
    }

    private static void removeItemFromFoodMenu() {
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Remove an item from the menu");
        System.out.println("-------------------------------------");

        String key;

        if(isNull(foodMenu)) {
            foodMenu = new FoodMenu();
        }
        System.out.println(foodMenu.toString());

        if(foodMenu.getSize() > 0) {
            System.out.println("Type the 6 chars ID of the item you want to remove");
            in = new Scanner(System.in);
            key = in.nextLine();

            if(foodMenu.removeProduct(key)) {
                System.out.println("\n");
                System.out.println("\n");
                System.out.println("Item removed successfully");

                if(foodMenu.getSize() <= 0) {
                    activeOrders.clear();
                }
            } else {
                System.out.println("\n");
                System.out.println("\n");
                System.out.println("An error occurred");
            }
        }

        editFoodMenu();
    }

    private static void exportZ() {
        try {
            if(isNull(activeOrders)) {
                activeOrders = new ArrayList<Order>();
            } else {
                for(Order o : activeOrders) {
                    if(!o.isReady()) {
                        System.out.println("Cannot end day with pending orders. Please mark all orders as DONE.");
                        menuLogged();
                    }
                }
            }

            ExportZ exportZ = new ExportZ("exportZ-" +
                    LocalDateTime.now().getDayOfMonth() +
                    "-" +
                    LocalDateTime.now().getMonth() +
                    "-" +
                    LocalDateTime.now().getYear() +
                    ".txt");

            float totalDayValue = 0;
            for(Order o : activeOrders) {
                totalDayValue += o.getTotalPrice();
                o.exportOrder(foodMenu.getProductList(), exportZ);
            }
            exportZ.WriteLine("Total revenue today: " + totalDayValue);

            System.out.println("Z report exported");

            exportZ.close();

            System.out.println(exportZ.getDescription());

            activeOrders.clear();
            Order.noOrders = 0L;

            menuLogged();
        } catch (NullPointerException e) {
            System.out.println("No active order");
            menuLogged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuLogged();
    }

    public static void main(String[] args) {
        start();
    }
}
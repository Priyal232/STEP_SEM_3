import java.util.ArrayList;
import java.util.List;

public class FoodOrderAndFlexiblePaymentSystem {
    static class FoodItem {
        private final String name;
        private final double price;

        public FoodItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    static class Restaurant {
        private final String name;
        private final List<FoodItem> menu = new ArrayList<>();

        public Restaurant(String name) {
            this.name = name;
        }

        public FoodItem addMenuItem(String itemName, double price) {
            FoodItem item = new FoodItem(itemName, price);
            menu.add(item);
            return item;
        }

        public boolean offers(FoodItem item) {
            return menu.contains(item);
        }

        public String getName() {
            return name;
        }
    }

    static class LineItem {
        private final FoodItem foodItem;
        private final int quantity;

        LineItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
        }

        public double getSubtotal() {
            return foodItem.getPrice() * quantity;
        }

        public FoodItem getFoodItem() {
            return foodItem;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    interface IPaymentMethod {
        String getMethodName();

        boolean pay(double amount);
    }

    static class CreditCardPayment implements IPaymentMethod {
        private final String cardLastFour;
        private final double availableCredit;

        public CreditCardPayment(String cardLastFour, double availableCredit) {
            this.cardLastFour = cardLastFour;
            this.availableCredit = availableCredit;
        }

        @Override
        public String getMethodName() {
            return "Credit Card";
        }

        @Override
        public boolean pay(double amount) {
            return amount <= availableCredit;
        }
    }

    static class DigitalWalletPayment implements IPaymentMethod {
        private final String walletId;
        private double balance;

        public DigitalWalletPayment(String walletId, double balance) {
            this.walletId = walletId;
            this.balance = balance;
        }

        @Override
        public String getMethodName() {
            return "Digital Wallet";
        }

        @Override
        public boolean pay(double amount) {
            if (amount > balance) {
                return false;
            }
            balance -= amount;
            return true;
        }
    }

    static class CashOnDeliveryPayment implements IPaymentMethod {
        @Override
        public String getMethodName() {
            return "Cash on Delivery";
        }

        @Override
        public boolean pay(double amount) {
            return true;
        }
    }

    interface OrderEventListener {
        void onOrderEvent(Order order, String event);
    }

    static class Customer implements OrderEventListener {
        private final String name;
        private final List<Order> orders = new ArrayList<>();

        public Customer(String name) {
            this.name = name;
        }

        public Order createOrder(Restaurant restaurant) {
            Order order = new Order(this, restaurant);
            orders.add(order);
            System.out.println("Order created.");
            return order;
        }

        @Override
        public void onOrderEvent(Order order, String event) {
            System.out.println("Notification: Order #" + order.getOrderNumber() + " " + event + ".");
        }

        public String getName() {
            return name;
        }

        public List<Order> getOrders() {
            return new ArrayList<>(orders);
        }
    }

    static class Order {
        public static final String CREATED = "Created";
        public static final String PENDING_PAYMENT = "Pending Payment";
        public static final String PAID = "Paid";
        private static int nextOrderNumber = 123;
        private int orderNumber;
        private final Customer customer;
        private final Restaurant restaurant;
        private final List<LineItem> lineItems = new ArrayList<>();
        private final List<OrderEventListener> listeners = new ArrayList<>();
        private String status = CREATED;

        Order(Customer customer, Restaurant restaurant) {
            this.customer = customer;
            this.restaurant = restaurant;
            listeners.add(customer);
        }

        public void addListener(OrderEventListener listener) {
            listeners.add(listener);
        }

        public void addItem(FoodItem item, int quantity) {
            if (!CREATED.equals(status)) {
                throw new IllegalStateException("Cannot modify an order that has already been placed.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be at least 1.");
            }
            if (!restaurant.offers(item)) {
                throw new IllegalArgumentException(item.getName() + " is not offered by " + restaurant.getName() + ".");
            }
            lineItems.add(new LineItem(item, quantity));
            System.out.println("Added " + item.getName() + " (Qty " + quantity + ").");
        }

        public void place(IPaymentMethod paymentMethod) {
            if (!CREATED.equals(status)) {
                throw new IllegalStateException("Order #" + orderNumber + " has already been placed.");
            }
            if (lineItems.isEmpty()) {
                throw new IllegalStateException("Cannot place order: Order must contain at least one item.");
            }
            orderNumber = nextOrderNumber++;
            status = PENDING_PAYMENT;
            System.out.printf("Order #%d placed successfully. Total: $%.2f%n", orderNumber, getTotal());
            processPayment(paymentMethod, "placed and paid", "placed, awaiting payment");
        }

        public void retryPayment(IPaymentMethod paymentMethod) {
            if (!PENDING_PAYMENT.equals(status)) {
                throw new IllegalStateException("Order is not awaiting payment.");
            }
            processPayment(paymentMethod, "paid", "still awaiting payment");
        }

        private void processPayment(IPaymentMethod paymentMethod, String successEvent, String failureEvent) {
            boolean paid = paymentMethod.pay(getTotal());
            if (paid) {
                status = PAID;
                System.out.println("Payment via " + paymentMethod.getMethodName() + " successful.");
            } else {
                System.out.println("Payment via " + paymentMethod.getMethodName() + " failed.");
            }
            System.out.println("Order status: " + status + ".");
            publish(paid ? successEvent : failureEvent);
        }

        private void publish(String event) {
            for (OrderEventListener listener : listeners) {
                listener.onOrderEvent(this, event);
            }
        }

        public double getTotal() {
            double total = 0;
            for (LineItem lineItem : lineItems) {
                total += lineItem.getSubtotal();
            }
            return total;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public String getStatus() {
            return status;
        }

        public Customer getCustomer() {
            return customer;
        }

        public List<LineItem> getLineItems() {
            return new ArrayList<>(lineItems);
        }
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant("Spice Hub");
        FoodItem pizza = restaurant.addMenuItem("Pizza", 12.00);
        FoodItem soda = restaurant.addMenuItem("Soda", 2.00);
        FoodItem burger = restaurant.addMenuItem("Burger", 8.00);

        Customer customer = new Customer("Rahul");

        Order firstOrder = customer.createOrder(restaurant);
        firstOrder.addItem(pizza, 2);
        firstOrder.addItem(soda, 1);

        Order emptyOrder = customer.createOrder(restaurant);
        try {
            emptyOrder.place(new CashOnDeliveryPayment());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        firstOrder.place(new CreditCardPayment("4471", 500.00));

        Order secondOrder = customer.createOrder(restaurant);
        secondOrder.addItem(burger, 1);
        secondOrder.place(new DigitalWalletPayment("WAL-77", 5.00));

        secondOrder.retryPayment(new CashOnDeliveryPayment());
    }
}

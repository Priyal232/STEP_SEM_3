import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TheSwiftShipParcelTracker {
    interface ShippingType {
        String getName();

        double calculateCharge(double weightKg);
    }

    static class StandardShipping implements ShippingType {
        @Override
        public String getName() {
            return "Standard";
        }

        @Override
        public double calculateCharge(double weightKg) {
            return 40.0 + 10.0 * weightKg;
        }
    }

    static class ExpressShipping implements ShippingType {
        @Override
        public String getName() {
            return "Express";
        }

        @Override
        public double calculateCharge(double weightKg) {
            return 80.0 + 15.0 * weightKg;
        }
    }

    static class FragileShipping implements ShippingType {
        private static final double HANDLING_FEE = 50.0;
        private final ShippingType baseRate = new StandardShipping();

        @Override
        public String getName() {
            return "Fragile";
        }

        @Override
        public double calculateCharge(double weightKg) {
            return baseRate.calculateCharge(weightKg) + HANDLING_FEE;
        }
    }

    interface NotificationChannel {
        String getChannelName();

        void notify(String parcelId, String status);
    }

    static class SmsChannel implements NotificationChannel {
        private final String phoneNumber;

        public SmsChannel(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String getChannelName() {
            return "SMS";
        }

        @Override
        public void notify(String parcelId, String status) {
            System.out.println("[SMS] " + parcelId + " is now " + status + ".");
        }
    }

    static class EmailChannel implements NotificationChannel {
        private final String emailAddress;

        public EmailChannel(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        @Override
        public String getChannelName() {
            return "Email";
        }

        @Override
        public void notify(String parcelId, String status) {
            System.out.println("[Email] " + parcelId + " is now " + status + ".");
        }
    }

    static class Customer {
        private final String name;
        private final List<Parcel> parcels = new ArrayList<>();

        public Customer(String name) {
            this.name = name;
        }

        void addParcel(Parcel parcel) {
            parcels.add(parcel);
        }

        public String getName() {
            return name;
        }

        public List<Parcel> getParcels() {
            return new ArrayList<>(parcels);
        }
    }

    static class Parcel {
        public static final String BOOKED = "BOOKED";
        public static final String PICKED_UP = "PICKED_UP";
        public static final String IN_TRANSIT = "IN_TRANSIT";
        public static final String OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
        public static final String DELIVERED = "DELIVERED";
        public static final String CANCELLED = "CANCELLED";
        private static final String[] STATUS_FLOW = {BOOKED, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED};

        private final String parcelId;
        private final Customer customer;
        private final double weightKg;
        private final ShippingType shippingType;
        private final double charge;
        private final List<NotificationChannel> subscribers = new ArrayList<>();
        private String status = BOOKED;
        private boolean bookingAnnounced = false;

        Parcel(String parcelId, Customer customer, double weightKg, ShippingType shippingType) {
            if (weightKg <= 0) {
                throw new IllegalArgumentException("Booking failed: weight must be positive.");
            }
            this.parcelId = parcelId;
            this.customer = customer;
            this.weightKg = weightKg;
            this.shippingType = shippingType;
            this.charge = shippingType.calculateCharge(weightKg);
        }

        public void subscribe(NotificationChannel channel) {
            if (!subscribers.contains(channel)) {
                subscribers.add(channel);
            }
        }

        public void unsubscribe(NotificationChannel channel) {
            subscribers.remove(channel);
        }

        void announceBooking() {
            if (bookingAnnounced) {
                return;
            }
            bookingAnnounced = true;
            notifySubscribers();
        }

        private static int positionOf(String value) {
            for (int i = 0; i < STATUS_FLOW.length; i++) {
                if (STATUS_FLOW[i].equals(value)) {
                    return i;
                }
            }
            return -1;
        }

        public void updateStatus(String next) {
            if (CANCELLED.equals(next)) {
                cancel();
                return;
            }
            int current = positionOf(status);
            int target = positionOf(next);
            if (current < 0 || target < 0 || target != current + 1) {
                throw new IllegalStateException("Invalid transition: " + status + " → " + next + " is not allowed.");
            }
            status = next;
            notifySubscribers();
        }

        public void cancel() {
            if (!BOOKED.equals(status)) {
                throw new IllegalStateException("Cancellation failed: " + parcelId + " can be cancelled only while BOOKED.");
            }
            status = CANCELLED;
            notifySubscribers();
        }

        private void notifySubscribers() {
            for (NotificationChannel channel : subscribers) {
                channel.notify(parcelId, status);
            }
        }

        public String getParcelId() {
            return parcelId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public double getWeightKg() {
            return weightKg;
        }

        public ShippingType getShippingType() {
            return shippingType;
        }

        public double getCharge() {
            return charge;
        }

        public String getStatus() {
            return status;
        }
    }

    static class ParcelService {
        private final Map<String, Parcel> parcels = new LinkedHashMap<>();

        public Parcel book(Customer customer, String parcelId, double weightKg, ShippingType type, List<NotificationChannel> channels) {
            if (parcels.containsKey(parcelId)) {
                throw new IllegalArgumentException("Booking failed: parcel " + parcelId + " already exists.");
            }
            Parcel parcel = new Parcel(parcelId, customer, weightKg, type);
            for (NotificationChannel channel : channels) {
                parcel.subscribe(channel);
            }
            parcels.put(parcelId, parcel);
            customer.addParcel(parcel);
            System.out.println("Parcel " + parcelId + " booked (" + type.getName() + ", " + formatWeight(weightKg) + " kg). Charge: ₹" + String.format("%.2f", parcel.getCharge()) + ".");
            parcel.announceBooking();
            return parcel;
        }

        public void markPickedUp(String parcelId) {
            update(parcelId, Parcel.PICKED_UP);
        }

        public void markInTransit(String parcelId) {
            update(parcelId, Parcel.IN_TRANSIT);
        }

        public void markOutForDelivery(String parcelId) {
            update(parcelId, Parcel.OUT_FOR_DELIVERY);
        }

        public void markDelivered(String parcelId) {
            update(parcelId, Parcel.DELIVERED);
        }

        public void cancel(String parcelId) {
            try {
                find(parcelId).cancel();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        private void update(String parcelId, String next) {
            try {
                find(parcelId).updateStatus(next);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        private Parcel find(String parcelId) {
            Parcel parcel = parcels.get(parcelId);
            if (parcel == null) {
                throw new IllegalArgumentException("Parcel " + parcelId + " not found.");
            }
            return parcel;
        }

        private static String formatWeight(double weight) {
            if (weight == Math.floor(weight)) {
                return String.valueOf((long) weight);
            }
            return String.valueOf(weight);
        }
    }

    public static void main(String[] args) {
        ParcelService service = new ParcelService();
        Customer customer = new Customer("Anita");

        List<NotificationChannel> channels = new ArrayList<>();
        channels.add(new SmsChannel("9876543210"));
        channels.add(new EmailChannel("anita@example.com"));

        service.book(customer, "P101", 2, new ExpressShipping(), channels);
        service.markPickedUp("P101");
        service.cancel("P101");
        service.markInTransit("P101");
        service.markDelivered("P101");

        System.out.println("---");
        List<NotificationChannel> emailOnly = new ArrayList<>();
        emailOnly.add(new EmailChannel("anita@example.com"));
        service.book(customer, "P102", 3, new FragileShipping(), emailOnly);
        service.cancel("P102");
    }
}

import java.util.ArrayList;
import java.util.List;

public class VehicleRentalSystemWithDynamicPricing {
    static abstract class Vehicle {
        private final String vehicleId;
        private final String name;
        private boolean available = true;

        protected Vehicle(String vehicleId, String name) {
            this.vehicleId = vehicleId;
            this.name = name;
        }

        public abstract String getCategory();

        public abstract double calculateCharge(int days);

        public String getVehicleId() {
            return vehicleId;
        }

        public String getName() {
            return name;
        }

        public boolean isAvailable() {
            return available;
        }

        void markRented() {
            available = false;
        }

        void markAvailable() {
            available = true;
        }
    }

    static class StandardCar extends Vehicle {
        private static final double DAILY_RATE = 50.0;

        public StandardCar(String vehicleId, String name) {
            super(vehicleId, name);
        }

        @Override
        public String getCategory() {
            return "Standard";
        }

        @Override
        public double calculateCharge(int days) {
            return DAILY_RATE * days;
        }
    }

    static class LuxuryCar extends Vehicle {
        private static final double DAILY_RATE = 100.0;

        public LuxuryCar(String vehicleId, String name) {
            super(vehicleId, name);
        }

        @Override
        public String getCategory() {
            return "Luxury";
        }

        @Override
        public double calculateCharge(int days) {
            return DAILY_RATE * days;
        }
    }

    static class SUV extends Vehicle {
        private static final double DAILY_RATE = 75.0;
        private static final double CLEANING_FEE = 20.0;

        public SUV(String vehicleId, String name) {
            super(vehicleId, name);
        }

        @Override
        public String getCategory() {
            return "SUV";
        }

        @Override
        public double calculateCharge(int days) {
            return DAILY_RATE * days + CLEANING_FEE;
        }
    }

    static class Customer {
        private final String name;
        private final List<Rental> rentals = new ArrayList<>();

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        void addRental(Rental rental) {
            rentals.add(rental);
        }

        public List<Rental> getRentals() {
            return new ArrayList<>(rentals);
        }
    }

    static class Rental {
        private static int rentalCounter = 0;
        private final String rentalId;
        private final Customer customer;
        private final Vehicle vehicle;
        private final int days;
        private final double totalCharge;
        private boolean active = true;

        Rental(Customer customer, Vehicle vehicle, int days) {
            rentalCounter++;
            this.rentalId = "R-" + rentalCounter;
            this.customer = customer;
            this.vehicle = vehicle;
            this.days = days;
            this.totalCharge = vehicle.calculateCharge(days);
        }

        void close() {
            active = false;
        }

        public boolean isActive() {
            return active;
        }

        public String getRentalId() {
            return rentalId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public int getDays() {
            return days;
        }

        public double getTotalCharge() {
            return totalCharge;
        }
    }

    static class RentalService {
        private final List<Vehicle> fleet = new ArrayList<>();
        private final List<Rental> rentals = new ArrayList<>();

        public void addVehicle(Vehicle vehicle) {
            fleet.add(vehicle);
        }

        public Rental rent(Customer customer, Vehicle vehicle, int days) {
            if (days <= 0) {
                throw new IllegalArgumentException("Rental duration must be at least 1 day.");
            }
            if (!fleet.contains(vehicle)) {
                throw new IllegalArgumentException(vehicle.getName() + " is not part of the fleet.");
            }
            if (!vehicle.isAvailable() || findActiveRental(vehicle) != null) {
                throw new IllegalStateException("Rental failed: " + vehicle.getName() + " already has an active rental.");
            }
            Rental rental = new Rental(customer, vehicle, days);
            vehicle.markRented();
            rentals.add(rental);
            customer.addRental(rental);
            return rental;
        }

        public Rental returnVehicle(Vehicle vehicle) {
            Rental rental = findActiveRental(vehicle);
            if (rental == null) {
                throw new IllegalStateException("Return failed: " + vehicle.getName() + " is not currently rented.");
            }
            rental.close();
            vehicle.markAvailable();
            return rental;
        }

        private Rental findActiveRental(Vehicle vehicle) {
            for (Rental rental : rentals) {
                if (rental.getVehicle() == vehicle && rental.isActive()) {
                    return rental;
                }
            }
            return null;
        }

        public List<Vehicle> getAvailableVehicles() {
            List<Vehicle> available = new ArrayList<>();
            for (Vehicle vehicle : fleet) {
                if (vehicle.isAvailable()) {
                    available.add(vehicle);
                }
            }
            return available;
        }
    }

    static void rentAndPrint(RentalService service, Customer customer, Vehicle vehicle, int days) {
        try {
            Rental rental = service.rent(customer, vehicle, days);
            System.out.printf("%s rented for %d days. Total charge: $%.2f%n", vehicle.getName(), rental.getDays(), rental.getTotalCharge());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        RentalService service = new RentalService();
        Vehicle luxuryA = new LuxuryCar("V1", "Luxury Car A");
        Vehicle standardB = new StandardCar("V2", "Standard Car B");
        Vehicle suvC = new SUV("V3", "SUV C");
        service.addVehicle(luxuryA);
        service.addVehicle(standardB);
        service.addVehicle(suvC);

        Customer customer = new Customer("Arjun");
        Customer other = new Customer("Meera");

        rentAndPrint(service, customer, luxuryA, 3);
        rentAndPrint(service, customer, standardB, 5);
        rentAndPrint(service, other, luxuryA, 2);

        service.returnVehicle(luxuryA);
        System.out.println(luxuryA.getName() + " returned. Now available.");

        rentAndPrint(service, other, luxuryA, 2);
        rentAndPrint(service, other, suvC, 4);
    }
}

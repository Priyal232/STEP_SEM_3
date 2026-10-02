import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HotelBookingAndCancellationSystem {
    interface PricingStrategy {
        String getCategoryName();

        double calculatePrice(long nights);
    }

    static class StandardPricing implements PricingStrategy {
        @Override
        public String getCategoryName() {
            return "Standard";
        }

        @Override
        public double calculatePrice(long nights) {
            return 150.0 * nights;
        }
    }

    static class DeluxePricing implements PricingStrategy {
        @Override
        public String getCategoryName() {
            return "Deluxe";
        }

        @Override
        public double calculatePrice(long nights) {
            return 200.0 * nights;
        }
    }

    static class SuitePricing implements PricingStrategy {
        @Override
        public String getCategoryName() {
            return "Suite";
        }

        @Override
        public double calculatePrice(long nights) {
            double base = 350.0 * nights;
            return nights >= 7 ? base * 0.90 : base;
        }
    }

    static class Customer {
        private final String name;
        private final List<Reservation> reservations = new ArrayList<>();

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }

        public List<Reservation> getReservations() {
            return new ArrayList<>(reservations);
        }
    }

    static class Room {
        private final String roomNumber;
        private final PricingStrategy pricing;
        private final List<Reservation> reservations = new ArrayList<>();

        public Room(String roomNumber, PricingStrategy pricing) {
            this.roomNumber = roomNumber;
            this.pricing = pricing;
        }

        public boolean isAvailable(LocalDate from, LocalDate to) {
            for (Reservation reservation : reservations) {
                if (reservation.isActive() && reservation.overlaps(from, to)) {
                    return false;
                }
            }
            return true;
        }

        void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }

        public double priceFor(LocalDate from, LocalDate to) {
            return pricing.calculatePrice(ChronoUnit.DAYS.between(from, to));
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getDisplayName() {
            return pricing.getCategoryName() + " Room " + roomNumber;
        }
    }

    static class Reservation {
        private static final String ACTIVE = "ACTIVE";
        private static final String CANCELLED = "CANCELLED";
        private static int reservationCounter = 0;
        private final String reservationId;
        private final Room room;
        private final Customer customer;
        private final LocalDate checkIn;
        private final LocalDate checkOut;
        private final LocalDate cancellationDeadline;
        private final double totalPrice;
        private String status = ACTIVE;

        Reservation(Room room, Customer customer, LocalDate checkIn, LocalDate checkOut, LocalDate cancellationDeadline) {
            reservationCounter++;
            this.reservationId = "RES-" + reservationCounter;
            this.room = room;
            this.customer = customer;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.cancellationDeadline = cancellationDeadline;
            this.totalPrice = room.priceFor(checkIn, checkOut);
        }

        public boolean overlaps(LocalDate from, LocalDate to) {
            return checkIn.isBefore(to) && from.isBefore(checkOut);
        }

        void cancel(LocalDate requestDate) {
            if (!isActive()) {
                throw new IllegalStateException("Cancellation failed: reservation " + reservationId + " is already cancelled.");
            }
            if (requestDate.isAfter(cancellationDeadline)) {
                throw new IllegalStateException("Cancellation failed: deadline for " + room.getDisplayName() + " was " + cancellationDeadline + ".");
            }
            status = CANCELLED;
        }

        public boolean isActive() {
            return ACTIVE.equals(status);
        }

        public String getReservationId() {
            return reservationId;
        }

        public Room getRoom() {
            return room;
        }

        public Customer getCustomer() {
            return customer;
        }

        public LocalDate getCheckIn() {
            return checkIn;
        }

        public LocalDate getCheckOut() {
            return checkOut;
        }

        public LocalDate getCancellationDeadline() {
            return cancellationDeadline;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public String getStatus() {
            return status;
        }
    }

    static class Hotel {
        private final String name;
        private final int cancellationNoticeDays;
        private final Map<String, Room> rooms = new LinkedHashMap<>();
        private final List<Reservation> reservations = new ArrayList<>();

        public Hotel(String name, int cancellationNoticeDays) {
            this.name = name;
            this.cancellationNoticeDays = cancellationNoticeDays;
        }

        public void addRoom(Room room) {
            rooms.put(room.getRoomNumber(), room);
        }

        public Reservation book(Customer customer, String roomNumber, LocalDate from, LocalDate to) {
            Room room = rooms.get(roomNumber);
            if (room == null) {
                throw new IllegalArgumentException("Booking failed: Room " + roomNumber + " does not exist.");
            }
            if (!to.isAfter(from)) {
                throw new IllegalArgumentException("Booking failed: check-out must be after check-in.");
            }
            if (!room.isAvailable(from, to)) {
                throw new IllegalStateException("Booking failed: " + room.getDisplayName() + " is not available for " + from + " to " + to + ".");
            }
            Reservation reservation = new Reservation(room, customer, from, to, from.minusDays(cancellationNoticeDays));
            room.addReservation(reservation);
            customer.addReservation(reservation);
            reservations.add(reservation);
            return reservation;
        }

        public void cancel(Reservation reservation, LocalDate requestDate) {
            if (!reservations.contains(reservation)) {
                throw new IllegalArgumentException("Cancellation failed: unknown reservation.");
            }
            reservation.cancel(requestDate);
        }

        public String getName() {
            return name;
        }
    }

    static Reservation bookAndPrint(Hotel hotel, Customer customer, String roomNumber, String from, String to) {
        try {
            Reservation r = hotel.book(customer, roomNumber, LocalDate.parse(from), LocalDate.parse(to));
            System.out.printf("%s booked from %s to %s. Total price: $%.2f%n", r.getRoom().getDisplayName(), r.getCheckIn(), r.getCheckOut(), r.getTotalPrice());
            return r;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static void cancelAndPrint(Hotel hotel, Reservation reservation, String requestDate) {
        try {
            hotel.cancel(reservation, LocalDate.parse(requestDate));
            System.out.println("Reservation for " + reservation.getRoom().getDisplayName() + " cancelled successfully.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Grand Stay", 2);
        hotel.addRoom(new Room("101", new DeluxePricing()));
        hotel.addRoom(new Room("205", new StandardPricing()));
        hotel.addRoom(new Room("301", new SuitePricing()));

        Customer customer = new Customer("Priya");

        Reservation deluxe = bookAndPrint(hotel, customer, "101", "2024-12-01", "2024-12-05");
        Reservation standard = bookAndPrint(hotel, customer, "205", "2024-12-03", "2024-12-07");
        bookAndPrint(hotel, customer, "101", "2024-12-03", "2024-12-07");
        cancelAndPrint(hotel, deluxe, "2024-11-25");

        cancelAndPrint(hotel, standard, "2024-12-02");
        bookAndPrint(hotel, customer, "101", "2024-12-03", "2024-12-07");
    }
}

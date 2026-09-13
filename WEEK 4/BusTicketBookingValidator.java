import java.util.*;

public class BusTicketBookingValidator {

    static class BusTicket {
        private String passengerName;
        private String destination;
        private boolean checkedIn;

        public BusTicket(String passengerName, String destination) {
            if (!isMeaningful(passengerName) || !isMeaningful(destination)) {
                throw new IllegalArgumentException("Invalid booking");
            }
            this.passengerName = passengerName;
            this.destination = destination;
            this.checkedIn = false;
        }

        private boolean isMeaningful(String value) {
            if (value == null) return false;
            String trimmed = value.trim();
            if (trimmed.isEmpty()) return false;
            for (int i = 0; i < trimmed.length(); i++) {
                if (Character.isDigit(trimmed.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        void markCheckedIn() {
            if (checkedIn) {
                System.out.println("Ticket for " + passengerName + " is already checked in.");
            } else {
                checkedIn = true;
                System.out.println("Ticket for " + passengerName + " checked in.");
            }
        }

        static void processBatch(String[][] rawBookings) {
            int valid = 0, rejected = 0, duplicates = 0;
            Set<String> seen = new HashSet<>();
            for (int i = 0; i < rawBookings.length; i++) {
                String name = rawBookings[i][0];
                String dest = rawBookings[i][1];
                String key = name + "|" + dest;
                try {
                    if (seen.contains(key)) {
                        duplicates++;
                        continue;
                    }
                    new BusTicket(name, dest);
                    seen.add(key);
                    valid++;
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            System.out.println("Valid: " + valid + " | Rejected: " + rejected + " | Duplicates skipped: " + duplicates);
        }
    }

    public static void main(String[] args) {
        String[][] rawBookings = {
                {"Divya", "Chennai"}, {"", "Bangalore"}, {"Ravi123", "Pune"},
                {"Divya", "Chennai"}, {" ", " "}
        };
        BusTicket.processBatch(rawBookings);
    }
}

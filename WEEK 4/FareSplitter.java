import java.util.*;

public class FareSplitter {

    static class FareeSplitter {
        private String tripId;
        private double totalFare;
        private int passengerCount;

        public FareeSplitter(String tripId, double totalFare, int passengerCount) {
            if (totalFare < 0 || passengerCount <= 0) {
                throw new IllegalArgumentException("Invalid fare split");
            }
            this.tripId = tripId;
            this.totalFare = totalFare;
            this.passengerCount = passengerCount;
        }

        public FareeSplitter(String tripId, double totalFare) {
            this(tripId, totalFare, 1);
        }

        public FareeSplitter(String tripId) {
            this(tripId, 0, 2);
        }

        double[] fareBreakdown() {
            long totalCents = Math.round(totalFare * 100);
            long baseCents = totalCents / passengerCount;
            long remainder = totalCents % passengerCount;
            double[] shares = new double[passengerCount];
            for (int i = 0; i < passengerCount; i++) {
                shares[i] = baseCents / 100.0;
            }
            for (int i = 0; i < remainder; i++) {
                shares[passengerCount - 1 - i] += 0.01;
            }
            return shares;
        }

        boolean isConfirmationOverdue(int confirmed, int expected) {
            return confirmed < expected;
        }
    }

    public static void main(String[] args) {
        FareeSplitter splitter = new FareeSplitter("TRIP001", 100000, 3);
        System.out.println(Arrays.toString(splitter.fareBreakdown()));
        FareeSplitter splitter2 = new FareeSplitter("TRIP003");
        System.out.println(Arrays.toString(splitter2.fareBreakdown()));
    }
}

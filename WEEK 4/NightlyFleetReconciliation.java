public class NightlyFleetReconciliation {

    static final class BoardingPenaltyCalculator {
        private final double minimumPenaltyPercent;

        public BoardingPenaltyCalculator(double minimumPenaltyPercent) {
            this.minimumPenaltyPercent = minimumPenaltyPercent;
        }

        final double calculatePenalty(double ticketFare, int minutesLate) {
            if (ticketFare < 0 || minutesLate < 0) {
                throw new IllegalArgumentException("Invalid input");
            }
            if (minutesLate == 0) {
                return 0.0;
            }
            double tiered = 0;
            int bracket1 = Math.min(minutesLate, 5);
            tiered += bracket1 * 0.005 * ticketFare;
            if (minutesLate > 5) {
                int bracket2 = Math.min(minutesLate, 15) - 5;
                tiered += bracket2 * 0.01 * ticketFare;
            }
            if (minutesLate > 15) {
                int bracket3 = minutesLate - 15;
                tiered += bracket3 * 0.02 * ticketFare;
            }
            double floor = (minimumPenaltyPercent / 100) * ticketFare;
            return Math.max(tiered, floor);
        }
    }

    static class BusTicketAccount {
        private String bookingId;
        private double ticketFare;
        private static BoardingPenaltyCalculator calculator;

        static {
            calculator = new BoardingPenaltyCalculator(1);
        }

        public BusTicketAccount(String bookingId, double ticketFare) {
            this.bookingId = bookingId;
            this.ticketFare = ticketFare;
        }

        public BusTicketAccount(String bookingId) {
            this(bookingId, 0);
        }

        final double calculatePenalty(int minutesLate) {
            return calculator.calculatePenalty(ticketFare, minutesLate);
        }

        static void processBatch(BusTicketAccount[] accounts, double[] amounts, int[] minutesLateArray) {
            int processed = 0, nulls = 0, sleeper = 0, regular = 0;
            double grandTotal = 0;
            for (int i = 0; i < accounts.length; i++) {
                BusTicketAccount account = accounts[i];
                if (account == null) {
                    nulls++;
                    continue;
                }
                account.ticketFare = amounts[i];
                double penalty = account.calculatePenalty(minutesLateArray[i]);
                if (account instanceof Sleeper) {
                    penalty = penalty * 1.5;
                    sleeper++;
                } else {
                    regular++;
                }
                grandTotal += penalty;
                processed++;
            }
            System.out.println(processed + " processed | " + nulls + " null skipped | " + sleeper + " sleeper | "
                    + regular + " regular | grand total penalties = " + grandTotal);
        }
    }

    static class Sleeper extends BusTicketAccount {
        public Sleeper(String bookingId, double ticketFare) {
            super(bookingId, ticketFare);
        }
    }

    public static void main(String[] args) {
        BusTicketAccount[] accounts = {
                new Sleeper("BK001", 2000),
                null,
                new BusTicketAccount("BK002", 1200)
        };
        double[] amounts = {1200, 900, 700};
        int[] delays = {10, 5, 0};
        BusTicketAccount.processBatch(accounts, amounts, delays);
    }
}

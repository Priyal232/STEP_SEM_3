public class NightlyMultiKitchenReconciliation {

    static final class SurgeFeeCalculator {
        private final double minimumSurgePercent;

        public SurgeFeeCalculator(double minimumSurgePercent) {
            this.minimumSurgePercent = minimumSurgePercent;
        }

        final double calculateSurgeFee(double orderValue, int delayMinutes) {
            if (orderValue < 0 || delayMinutes < 0) {
                throw new IllegalArgumentException("Invalid input");
            }
            if (delayMinutes == 0) {
                return 0.0;
            }
            double tiered = 0;
            int bracket1 = Math.min(delayMinutes, 5);
            tiered += bracket1 * 0.005 * orderValue;
            if (delayMinutes > 5) {
                int bracket2 = Math.min(delayMinutes, 15) - 5;
                tiered += bracket2 * 0.01 * orderValue;
            }
            if (delayMinutes > 15) {
                int bracket3 = delayMinutes - 15;
                tiered += bracket3 * 0.02 * orderValue;
            }
            double floor = (minimumSurgePercent / 100) * orderValue;
            return Math.max(tiered, floor);
        }
    }

    static class DeliveryAccount {
        private String studentId;
        private double orderValue;
        private static SurgeFeeCalculator calculator;

        static {
            calculator = new SurgeFeeCalculator(1);
        }

        public DeliveryAccount(String studentId, double orderValue) {
            this.studentId = studentId;
            this.orderValue = orderValue;
        }

        public DeliveryAccount(String studentId) {
            this(studentId, 0);
        }

        final double calculateSurgeFee(int delayMinutes) {
            return calculator.calculateSurgeFee(orderValue, delayMinutes);
        }

        static void processBatch(DeliveryAccount[] accounts, double[] amounts, int[] delayMinutesArray) {
            int processed = 0, nulls = 0, premium = 0, regular = 0;
            double grandTotal = 0;
            for (int i = 0; i < accounts.length; i++) {
                DeliveryAccount account = accounts[i];
                if (account == null) {
                    nulls++;
                    continue;
                }
                account.orderValue = amounts[i];
                double fee = account.calculateSurgeFee(delayMinutesArray[i]);
                if (account instanceof Premium) {
                    fee = fee * 0.8;
                    premium++;
                } else {
                    regular++;
                }
                grandTotal += fee;
                processed++;
            }
            System.out.println(processed + " processed | " + nulls + " null skipped | " + premium + " premium | "
                    + regular + " regular | grand total surge fees = " + grandTotal);
        }
    }

    static class Premium extends DeliveryAccount {
        public Premium(String studentId, double orderValue) {
            super(studentId, orderValue);
        }
    }

    public static void main(String[] args) {
        DeliveryAccount[] accounts = {
                new Premium("STU001", 500),
                null,
                new DeliveryAccount("STU002", 300)
        };
        double[] amounts = {500, 400, 300};
        int[] delays = {10, 5, 0};
        DeliveryAccount.processBatch(accounts, amounts, delays);
    }
}

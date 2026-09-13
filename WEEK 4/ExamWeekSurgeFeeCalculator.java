public class ExamWeekSurgeFeeCalculator {

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

    public static void main(String[] args) {
        SurgeFeeCalculator calc = new SurgeFeeCalculator(1);
        System.out.println("Rs " + calc.calculateSurgeFee(500, 0));
        System.out.println("Rs " + calc.calculateSurgeFee(500, 1));
        System.out.println("Rs " + calc.calculateSurgeFee(500, 16));
    }
}

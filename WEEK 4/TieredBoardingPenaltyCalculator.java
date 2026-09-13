public class TieredBoardingPenaltyCalculator {

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

    public static void main(String[] args) {
        BoardingPenaltyCalculator calc = new BoardingPenaltyCalculator(1);
        System.out.println("Rs " + calc.calculatePenalty(1000, 0));
        System.out.println("Rs " + calc.calculatePenalty(1000, 1));
        System.out.println("Rs " + calc.calculatePenalty(1000, 16));
    }
}

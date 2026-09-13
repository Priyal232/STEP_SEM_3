import java.util.*;

public class LateRegistrationPenaltyOverride {

    static class EventTicket {
        private double basePrice;
        private double amountPaid;
        private double[] lateFeeHistory = new double[10];
        private int lateFeeCount = 0;

        public EventTicket(double basePrice) {
            this.basePrice = basePrice;
        }

        void pay(double amount) {
            amountPaid += amount;
        }

        double getBalanceDue() {
            return basePrice - amountPaid;
        }

        protected void applyLateFee(double amount) {
            amountPaid -= amount;
            if (lateFeeCount < lateFeeHistory.length) {
                lateFeeHistory[lateFeeCount] = amount;
                lateFeeCount++;
            }
        }

        double[] getLateFeeHistory() {
            double[] copy = new double[lateFeeCount];
            for (int i = 0; i < lateFeeCount; i++) copy[i] = lateFeeHistory[i];
            return copy;
        }
    }

    static class WorkshopTicket extends EventTicket {
        public WorkshopTicket(double basePrice) {
            super(basePrice);
        }

        @Override
        protected void applyLateFee(double amount) {
            super.applyLateFee(amount * 2);
        }
    }

    public static void main(String[] args) {
        WorkshopTicket w = new WorkshopTicket(1200);
        w.pay(1200);
        w.applyLateFee(100);
        System.out.println(w.getBalanceDue());

        double[] history = w.getLateFeeHistory();
        System.out.println(Arrays.toString(history));
        history[0] = 999;
        System.out.println(Arrays.toString(w.getLateFeeHistory()));
    }
}

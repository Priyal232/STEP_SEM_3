import java.util.Arrays;

public class LateFeeAuditTrail {

    static class RaceEntry {
        private final String bibNumber;
        private final double entryFee;
        private double amountPaid;
        private double lateFeesTotal;

        
        private final double[] lateFeeHistory = new double[10];
        private int lateFeeCount = 0;

        public RaceEntry(String bibNumber, double entryFee) {
            if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4) {
                throw new IllegalArgumentException("Rejected bib: \"" + bibNumber + "\"");
            }
            this.bibNumber = bibNumber.trim();
            this.entryFee  = entryFee;
        }

        public String getBibNumber()  { return bibNumber; }
        public void   pay(double amount) { if (amount > 0) amountPaid += amount; }
        public double getBalanceDue()    { return entryFee + lateFeesTotal - amountPaid; }

        
        protected void applyLateFee(double amount) {
            lateFeesTotal += amount;
            if (lateFeeCount < lateFeeHistory.length) {
                lateFeeHistory[lateFeeCount++] = amount;
            }
        }

       
        public double[] getLateFeeHistory() {
            return Arrays.copyOf(lateFeeHistory, lateFeeCount);
        }

        public String announce() {
            return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
        }
    }

    static class RunnerEntry extends RaceEntry {
        private final String category;

        public RunnerEntry(String bibNumber, double entryFee, String category) {
            super(bibNumber, entryFee);
            this.category = category;
        }

        public String getCategory() { return category; }

       
        @Override
        protected void applyLateFee(double amount) {
            super.applyLateFee(amount * 2);
        }

        @Override
        public String announce() {
            return "Runner Entry | Bib: " + getBibNumber()
                 + " | Category: " + category + " | Balance: " + getBalanceDue();
        }
    }

    public static void main(String[] args) {
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        r.applyLateFee(20);                    

        System.out.println("--- Overridden penalty ---");
        System.out.println("80 fee - 30 paid + 40 doubled penalty");
        System.out.println("Balance due: " + r.getBalanceDue());          

        System.out.println();
        System.out.println("--- Defensive copy proof ---");
        double[] history = r.getLateFeeHistory();
        System.out.println("Returned history : " + Arrays.toString(history));   
        history[0] = 999;                                                       
        System.out.println("Tampered copy    : " + Arrays.toString(history));   
        System.out.println("Real history     : " + Arrays.toString(r.getLateFeeHistory())); 
    }
}

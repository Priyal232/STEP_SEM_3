public class BatchBibValidator {

    
    static class RaceEntry {
        private final String bibNumber;
        private final double entryFee;
        private double amountPaid;

     
        public RaceEntry(String bibNumber, double entryFee) {
            if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4) {
                throw new IllegalArgumentException("Rejected bib: \"" + bibNumber
                        + "\" (must be non-blank and at least 4 characters)");
            }
            this.bibNumber = bibNumber.trim();
            this.entryFee  = entryFee;
        }

        public String getBibNumber() { return bibNumber; }
        public double getEntryFee()  { return entryFee;  }

        public void pay(double amount) {
            if (amount <= 0) {
                System.out.println("Payment rejected: amount must be positive");
                return;
            }
            amountPaid += amount;
        }

        public double getBalanceDue() { return entryFee - amountPaid; }

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
        public String announce() {
            return "Runner Entry | Bib: " + getBibNumber()
                 + " | Category: " + category
                 + " | Balance: " + getBalanceDue();
        }
    }


    static String registerBatch(String[] bibNumbers, double entryFee) {
        int registered = 0;
        int rejected   = 0;
        for (String bib : bibNumbers) {
            try {
                new RaceEntry(bib, entryFee);
                registered++;
            } catch (IllegalArgumentException e) {
                rejected++;                   
            }
        }
        return "Registered: " + registered + " | Rejected: " + rejected;
    }

    public static void main(String[] args) {
        System.out.println("--- Constructor validation ---");
        try {
            new RaceEntry("B1", 50);          
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected -> " + e.getMessage());
        }

        System.out.println();
        System.out.println("--- Inheritance + payment ---");
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        System.out.println("Balance due: " + r.getBalanceDue());      
        System.out.println(r.announce());

        System.out.println();
        System.out.println("--- Batch validator ---");
        System.out.println(registerBatch(new String[]{"BIB1", "B1", "BIB2"}, 80));
    }
}

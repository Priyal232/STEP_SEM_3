public class NightlySettlementEngine {

    static class RaceEntry {
        private static int bibCounter = 0;      

        private final String entryCode;         
        private final String bibNumber;
        private final double entryFee;
        private double amountPaid;


        public RaceEntry(String bibNumber, double entryFee) {
            if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4) {
                throw new IllegalArgumentException("Rejected bib: \"" + bibNumber + "\"");
            }
            bibCounter++;                            
            this.entryCode = "RC-" + bibCounter;     
            this.bibNumber = bibNumber.trim();
            this.entryFee  = entryFee;
        }

        public String getEntryCode()  { return entryCode; }      
        public String getBibNumber()  { return bibNumber; }
        public double getBalanceDue() { return entryFee - amountPaid; }

       
        public void pay(double amount) {
            if (amount <= 0) {
                System.out.println("Payment rejected: amount must be positive");
                return;
            }
            amountPaid += amount;
        }


        public void pay(double amount, String mode) {
            System.out.println("Paying via " + mode);
            pay(amount);
        }


        public static boolean isValidDiscountCode(String code) {
            if (code == null || code.length() != 5) return false;
            if (code.charAt(0) != 'M') return false;
            for (int i = 1; i <= 3; i++) {
                if (!Character.isDigit(code.charAt(i))) return false;
            }
            return Character.isUpperCase(code.charAt(4));
        }

        public static int getBibCounter() { return bibCounter; }

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

        @Override public String announce() {
            return "Runner Entry | Bib: " + getBibNumber()
                 + " | Category: " + category + " | Balance: " + getBalanceDue();
        }
    }

    static class EliteRunnerEntry extends RunnerEntry {
        private final double sponsorBonus;

        public EliteRunnerEntry(String bibNumber, double entryFee, String category, double sponsorBonus) {
            super(bibNumber, entryFee, category);
            this.sponsorBonus = sponsorBonus;
        }

        @Override public String announce() {
            return "Elite Runner | Bib: " + getBibNumber()
                 + " | Category: " + getCategory()
                 + " | Sponsor Bonus: " + sponsorBonus + " | Balance: " + getBalanceDue();
        }
    }

    static class RelayTeamEntry extends RaceEntry {
        private final int teamSize;

        public RelayTeamEntry(String bibNumber, double entryFee, int teamSize) {
            super(bibNumber, entryFee);
            this.teamSize = teamSize;
        }

        public int getTeamSize() { return teamSize; }

        @Override public String announce() {
            return "Relay Team | Bib: " + getBibNumber()
                 + " | Team Size: " + teamSize + " | Balance: " + getBalanceDue();
        }
    }


    static String settleNight(RaceEntry[] entries) {
        int processed = 0, nullSkipped = 0, relay = 0, individual = 0;
        for (RaceEntry e : entries) {
            if (e == null) { nullSkipped++; continue; }    
            processed++;
            if (e instanceof RelayTeamEntry) relay++;
            else individual++;
        }
        return processed + " processed | " + nullSkipped + " null skipped | "
             + relay + " relay | " + individual + " individual";
    }

    public static void main(String[] args) {
        System.out.println("--- Discount code validation (charAt only, no regex) ---");
        System.out.println("M123A -> " + RaceEntry.isValidDiscountCode("M123A"));   
        System.out.println("M12A  -> " + RaceEntry.isValidDiscountCode("M12A"));    
        System.out.println("X123A -> " + RaceEntry.isValidDiscountCode("X123A"));   
        System.out.println("M123a -> " + RaceEntry.isValidDiscountCode("M123a"));   

        RunnerEntry      r     = new RunnerEntry("BIB2001", 80, "Open 10K");
        EliteRunnerEntry elite = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry   relay = new RelayTeamEntry("BIB4001", 300, 4);
        RaceEntry        plain = new RaceEntry("BIB5001", 50);

        System.out.println();
        System.out.println("--- Unique, unreassignable entry codes ---");
        System.out.println(r.getEntryCode()     + " -> " + r.announce());
        System.out.println(elite.getEntryCode() + " -> " + elite.announce());
        System.out.println(relay.getEntryCode() + " -> " + relay.announce());
        System.out.println(plain.getEntryCode() + " -> " + plain.announce());

        System.out.println();
        System.out.println("--- Overloaded pay() ---");
        r.pay(10, "UPI");

        System.out.println();
        System.out.println("--- Nightly settlement (null-safe) ---");
        System.out.println(settleNight(new RaceEntry[]{ elite, null, relay }));

        System.out.println();
        System.out.println("--- Shared counter ---");
        try {
            new RaceEntry("B1", 50);           
        } catch (IllegalArgumentException ignored) { }
        System.out.println("getBibCounter() = " + RaceEntry.getBibCounter());   
    }
}

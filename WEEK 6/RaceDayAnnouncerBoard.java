public class RaceDayAnnouncerBoard {

    static class RaceEntry {
        private final String bibNumber;
        private final double entryFee;
        private double amountPaid;
        private double lateFeesTotal;

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

        protected void applyLateFee(double amount) { lateFeesTotal += amount; }

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

        @Override protected void applyLateFee(double amount) { super.applyLateFee(amount * 2); }

        @Override public String announce() {
            return "Runner Entry | Bib: " + getBibNumber()
                 + " | Category: " + category + " | Balance: " + getBalanceDue();
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

    static String announceAll(RaceEntry[] entries) {
        StringBuilder board = new StringBuilder();     
        for (RaceEntry e : entries) {
            board.append(e.announce());                

            if (e instanceof RelayTeamEntry) {             
                RelayTeamEntry team = (RelayTeamEntry) e;   
                board.append(" [Team size via downcast: ").append(team.getTeamSize()).append("]");
            }
            board.append(" | ");
        }
        return board.toString();
    }

    public static void main(String[] args) {
        RunnerEntry runner = new RunnerEntry("BIB2001", 80, "Open 10K");
        runner.pay(30);
        runner.applyLateFee(20);                       
        RelayTeamEntry relay = new RelayTeamEntry("BIB4001", 300, 4);

        System.out.println("--- Announcer board ---");
        RaceEntry[] fleet = { runner, relay };
        System.out.println(announceAll(fleet));

        System.out.println();
        System.out.println("--- Why the guard matters ---");
        RaceEntry plain = new RaceEntry("BIB5001", 50);
        try {
            RelayTeamEntry bad = (RelayTeamEntry) plain;
            System.out.println(bad.getTeamSize());
        } catch (ClassCastException ex) {
            System.out.println("ClassCastException at runtime -> " + ex.getMessage());
        }
    }
}

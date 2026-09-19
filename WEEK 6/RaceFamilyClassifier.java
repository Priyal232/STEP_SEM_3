public class RaceFamilyClassifier {

    static class RaceEntry {
        private final String bibNumber;
        private final double entryFee;
        private double amountPaid;

        public RaceEntry(String bibNumber, double entryFee) {
            if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4) {
                throw new IllegalArgumentException("Rejected bib: \"" + bibNumber + "\"");
            }
            this.bibNumber = bibNumber.trim();
            this.entryFee  = entryFee;
        }

        public String getBibNumber()  { return bibNumber; }
        public void   pay(double amount) { if (amount > 0) amountPaid += amount; }
        public double getBalanceDue()    { return entryFee - amountPaid; }

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
                 + " | Category: " + category + " | Balance: " + getBalanceDue();
        }
    }


    static class EliteRunnerEntry extends RunnerEntry {
        private final double sponsorBonus;

        public EliteRunnerEntry(String bibNumber, double entryFee, String category, double sponsorBonus) {
            super(bibNumber, entryFee, category);
            this.sponsorBonus = sponsorBonus;
        }

        public double getSponsorBonus() { return sponsorBonus; }

        @Override
        public String announce() {
            return "Elite Runner | Bib: " + getBibNumber()
                 + " | Category: " + getCategory()
                 + " | Sponsor Bonus: " + sponsorBonus
                 + " | Balance: " + getBalanceDue();
        }
    }


    static class RelayTeamEntry extends RaceEntry {
        private final int teamSize;

        public RelayTeamEntry(String bibNumber, double entryFee, int teamSize) {
            super(bibNumber, entryFee);
            this.teamSize = teamSize;
        }

        public int getTeamSize() { return teamSize; }

        @Override
        public String announce() {
            return "Relay Team | Bib: " + getBibNumber()
                 + " | Team Size: " + teamSize + " | Balance: " + getBalanceDue();
        }
    }


    static String classifyGeneration(RaceEntry entry) {
        if (entry instanceof EliteRunnerEntry) return "Multilevel descendant (3 generations deep)";
        if (entry instanceof RelayTeamEntry)   return "Hierarchical sibling (independent branch)";
        if (entry instanceof RunnerEntry)      return "Direct subclass (2 generations deep)";
        return "Base class (1 generation)";
    }

    
    static double getTotalBalanceDue(RaceEntry[] entries) {
        double total = 0.0;
        for (RaceEntry e : entries) total += e.getBalanceDue();
        return total;
    }

    public static void main(String[] args) {
        RunnerEntry      runner = new RunnerEntry("BIB2001", 80, "Open 10K");
        EliteRunnerEntry elite  = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry   relay  = new RelayTeamEntry("BIB4001", 300, 4);

        System.out.println("--- announce() overridden at every level ---");
        System.out.println(runner.announce());
        System.out.println(elite.announce());
        System.out.println(relay.announce());

        System.out.println();
        System.out.println("--- classifyGeneration() via instanceof ---");
        System.out.println(classifyGeneration(elite));
        System.out.println(classifyGeneration(relay));
        System.out.println(classifyGeneration(runner));

        System.out.println();
        System.out.println("--- Mixed-field total ---");
        RaceEntry[] field = { runner, elite, relay };
        System.out.println(getTotalBalanceDue(field));           
    }
}

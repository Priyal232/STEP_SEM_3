public class ThreeShapesFamilyTree {

    static class EventTicket {
        private String attendeeId;
        private double basePrice;
        private double amountPaid;

        public EventTicket(String attendeeId, double basePrice) {
            this.attendeeId = attendeeId;
            this.basePrice = basePrice;
        }

        void pay(double amount) {
            amountPaid += amount;
        }

        double getBalanceDue() {
            return basePrice - amountPaid;
        }

        String printTicket() {
            return "Standard Event Ticket | Balance Due: " + getBalanceDue();
        }

        static double getTotalBalanceDue(EventTicket[] tickets) {
            double total = 0;
            for (int i = 0; i < tickets.length; i++) {
                total += tickets[i].getBalanceDue();
            }
            return total;
        }

        static String classifyGeneration(EventTicket ticket) {
            if (ticket instanceof PremiumWorkshopTicket) {
                return "Multilevel descendant (3 generations deep)";
            }
            if (ticket instanceof HackathonTicket) {
                return "Hierarchical sibling (independent branch)";
            }
            if (ticket instanceof WorkshopTicket) {
                return "Direct subclass";
            }
            return "Base class";
        }
    }

    static class WorkshopTicket extends EventTicket {
        private String track;

        public WorkshopTicket(String attendeeId, double basePrice, String track) {
            super(attendeeId, basePrice);
            this.track = track;
        }

        @Override
        String printTicket() {
            return "Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue();
        }
    }

    static class PremiumWorkshopTicket extends WorkshopTicket {
        private double kitFee;

        public PremiumWorkshopTicket(String attendeeId, double basePrice, String track, double kitFee) {
            super(attendeeId, basePrice, track);
            this.kitFee = kitFee;
        }

        @Override
        String printTicket() {
            return "Premium Workshop Ticket | Kit Fee: " + kitFee + " | Balance Due: " + getBalanceDue();
        }
    }

    static class HackathonTicket extends EventTicket {
        private String teamName;

        public HackathonTicket(String attendeeId, double basePrice, String teamName) {
            super(attendeeId, basePrice);
            this.teamName = teamName;
        }

        @Override
        String printTicket() {
            return "Hackathon Ticket | Team: " + teamName + " | Balance Due: " + getBalanceDue();
        }
    }

    public static void main(String[] args) {
        EventTicket standardTicket = new EventTicket("STU1", 500);
        WorkshopTicket workshopTicket = new WorkshopTicket("STU2", 1200, "AI/ML");
        PremiumWorkshopTicket premiumTicket = new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300);
        HackathonTicket hackathonTicket = new HackathonTicket("STU4", 800, "Byte Force");

        System.out.println(standardTicket.printTicket());
        System.out.println(workshopTicket.printTicket());
        System.out.println(premiumTicket.printTicket());
        System.out.println(hackathonTicket.printTicket());

        System.out.println(EventTicket.classifyGeneration(premiumTicket));
        System.out.println(EventTicket.classifyGeneration(hackathonTicket));

        System.out.println(EventTicket.getTotalBalanceDue(new EventTicket[]{standardTicket, workshopTicket, premiumTicket, hackathonTicket}));
    }
}

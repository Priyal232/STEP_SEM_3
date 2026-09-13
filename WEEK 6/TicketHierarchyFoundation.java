public class TicketHierarchyFoundation {

    static class EventTicket {
        private String attendeeId;
        private double basePrice;
        private double amountPaid;

        public EventTicket(String attendeeId, double basePrice) {
            String trimmed = (attendeeId == null) ? "" : attendeeId.trim();
            if (trimmed.isEmpty() || trimmed.length() < 4) {
                throw new IllegalArgumentException("Invalid attendeeId");
            }
            this.attendeeId = attendeeId;
            this.basePrice = basePrice;
            this.amountPaid = 0;
        }

        void pay(double amount) {
            amountPaid += amount;
        }

        double getBalanceDue() {
            return basePrice - amountPaid;
        }

        static String registerBatch(String[] attendeeIds, double basePrice) {
            int registered = 0, rejected = 0;
            for (int i = 0; i < attendeeIds.length; i++) {
                try {
                    new EventTicket(attendeeIds[i], basePrice);
                    registered++;
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            return "Registered: " + registered + " | Rejected: " + rejected;
        }
    }

    static class WorkshopTicket extends EventTicket {
        private String track;

        public WorkshopTicket(String attendeeId, double basePrice, String track) {
            super(attendeeId, basePrice);
            this.track = track;
        }
    }

    public static void main(String[] args) {
        try {
            new EventTicket("ST1", 500);
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(500);
        System.out.println(w.getBalanceDue());

        System.out.println(EventTicket.registerBatch(new String[]{"STU1", "ST1", "STU2", " ", "STU3"}, 500));
    }
}

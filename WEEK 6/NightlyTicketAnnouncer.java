public class NightlyTicketAnnouncer {

    static class EventTicket {
        private double basePrice;
        private double amountPaid;

        public EventTicket(double basePrice) {
            this.basePrice = basePrice;
        }

        double getBalanceDue() {
            return basePrice - amountPaid;
        }

        String printTicket() {
            return "Standard | Balance: " + getBalanceDue();
        }

        static String batchPrint(EventTicket[] tickets) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tickets.length; i++) {
                EventTicket t = tickets[i];
                sb.append(t.printTicket()).append(" | ");
                if (t instanceof WorkshopTicket) {
                    WorkshopTicket w = (WorkshopTicket) t;
                    sb.append("[Track via downcast: ").append(w.getTrack()).append("] | ");
                }
            }
            return sb.toString();
        }
    }

    static class WorkshopTicket extends EventTicket {
        private String track;

        public WorkshopTicket(double basePrice, String track) {
            super(basePrice);
            this.track = track;
        }

        @Override
        String printTicket() {
            return "Workshop | Track: " + track + " | Balance: " + getBalanceDue();
        }

        String getTrack() {
            return track;
        }
    }

    public static void main(String[] args) {
        EventTicket plain = new EventTicket(500);
        WorkshopTicket ws = new WorkshopTicket(1200, "AI/ML");
        System.out.println(EventTicket.batchPrint(new EventTicket[]{plain, ws}));

        try {
            WorkshopTicket bad = (WorkshopTicket) plain;
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at runtime");
        }
    }
}

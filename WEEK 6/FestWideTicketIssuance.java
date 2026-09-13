public class FestWideTicketIssuance {

    static class EventTicket {
        private final int ticketId;
        private static int ticketCounter = 1000;
        private double basePrice;
        private double amountPaid;

        public EventTicket(double basePrice) {
            ticketCounter++;
            this.ticketId = ticketCounter;
            this.basePrice = basePrice;
        }

        void pay(double amount) {
            amountPaid += amount;
        }

        void pay(double amount, String mode) {
            System.out.println("Payment mode: " + mode);
            pay(amount);
        }

        double getBalanceDue() {
            return basePrice - amountPaid;
        }

        String getTicketIdString() {
            return "TCK-" + ticketId;
        }

        static int getTicketsIssued() {
            return ticketCounter - 1000;
        }

        static boolean isValidPromoCode(String code) {
            if (code == null || code.length() != 5) return false;
            if (code.charAt(0) != 'F') return false;
            for (int i = 1; i <= 3; i++) {
                if (!Character.isDigit(code.charAt(i))) return false;
            }
            return Character.isUpperCase(code.charAt(4));
        }

        static String processNightlySettlement(EventTicket[] tickets) {
            int processed = 0, nulls = 0, group = 0, individual = 0;
            for (int i = 0; i < tickets.length; i++) {
                EventTicket t = tickets[i];
                if (t == null) {
                    nulls++;
                    continue;
                }
                if (t instanceof GroupTicket) group++;
                else individual++;
                processed++;
            }
            return processed + " processed | " + nulls + " null skipped | " + group + " group | " + individual + " individual";
        }
    }

    static class GroupTicket extends EventTicket {
        private int groupSize;

        public GroupTicket(double basePrice, int groupSize) {
            super(basePrice);
            this.groupSize = groupSize;
        }
    }

    public static void main(String[] args) {
        EventTicket t1 = new EventTicket(500);
        System.out.println(t1.getTicketIdString());
        System.out.println(EventTicket.getTicketsIssued());

        System.out.println(EventTicket.isValidPromoCode("F123A"));
        System.out.println(EventTicket.isValidPromoCode("F12A"));
        System.out.println(EventTicket.isValidPromoCode("X123A"));

        t1.pay(200);
        t1.pay(200, "UPI");
        System.out.println(t1.getBalanceDue());

        EventTicket[] batch = {
                new GroupTicket(2000, 5),
                null,
                new EventTicket(500)
        };
        System.out.println(EventTicket.processNightlySettlement(batch));
    }
}

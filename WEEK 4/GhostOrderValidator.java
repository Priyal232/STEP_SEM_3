public class GhostOrderValidator {

    static class FoodOrder {
        private String studentName;
        private String dishName;
        private boolean delivered;

        public FoodOrder(String studentName, String dishName) {
            if (!isMeaningful(studentName) || !isMeaningful(dishName)) {
                throw new IllegalArgumentException("Invalid order");
            }
            this.studentName = studentName;
            this.dishName = dishName;
            this.delivered = false;
        }

        private boolean isMeaningful(String value) {
            return value != null && !value.trim().isEmpty();
        }

        void markDelivered() {
            if (delivered) {
                System.out.println("Order for " + studentName + " was already delivered.");
            } else {
                delivered = true;
                System.out.println("Order for " + studentName + " marked delivered.");
            }
        }

        static void processBatch(String[][] rawOrders) {
            int valid = 0, rejected = 0;
            for (int i = 0; i < rawOrders.length; i++) {
                try {
                    new FoodOrder(rawOrders[i][0], rawOrders[i][1]);
                    valid++;
                } catch (IllegalArgumentException e) {
                    rejected++;
                }
            }
            System.out.println("Valid: " + valid + " | Rejected: " + rejected);
        }
    }

    public static void main(String[] args) {
        String[][] rawOrders = {
                {"Ravi", "Paneer Butter Masala"}, {"", "Chole Bhature"},
                {"Meera", " "}, {"Divya", "Veg Biryani"}
        };
        FoodOrder.processBatch(rawOrders);
    }
}

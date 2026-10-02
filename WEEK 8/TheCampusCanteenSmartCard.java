import java.util.ArrayList;
import java.util.List;

public class TheCampusCanteenSmartCard {
    interface PricingPlan {
        String getPlanName();

        long priceFor(long basePricePaise);
    }

    static class DayScholarPlan implements PricingPlan {
        @Override
        public String getPlanName() {
            return "Day Scholar";
        }

        @Override
        public long priceFor(long basePricePaise) {
            return basePricePaise;
        }
    }

    static class HostellerPlan implements PricingPlan {
        @Override
        public String getPlanName() {
            return "Hosteller";
        }

        @Override
        public long priceFor(long basePricePaise) {
            return Math.round(basePricePaise * 90 / 100.0);
        }
    }

    static class StaffPlan implements PricingPlan {
        @Override
        public String getPlanName() {
            return "Staff";
        }

        @Override
        public long priceFor(long basePricePaise) {
            return Math.round(basePricePaise * 80 / 100.0);
        }
    }

    static class Money {
        static long toPaise(double rupees) {
            return Math.round(rupees * 100);
        }

        static String format(long paise) {
            return String.format("%,.2f", paise / 100.0);
        }
    }

    static class Transaction {
        public static final String TOP_UP = "TOP_UP";
        public static final String PURCHASE = "PURCHASE";
        public static final String REFUND = "REFUND";
        private final String transactionId;
        private final String type;
        private final long amountPaise;
        private final String description;
        private final String relatedTransactionId;

        Transaction(String transactionId, String type, long amountPaise, String description, String relatedTransactionId) {
            this.transactionId = transactionId;
            this.type = type;
            this.amountPaise = amountPaise;
            this.description = description;
            this.relatedTransactionId = relatedTransactionId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getType() {
            return type;
        }

        public long getAmountPaise() {
            return amountPaise;
        }

        public String getDescription() {
            return description;
        }

        public String getRelatedTransactionId() {
            return relatedTransactionId;
        }
    }

    static class SmartCard {
        private static final long MIN_TOP_UP = 10000;
        private static final long MAX_BALANCE = 500000;
        private final String cardId;
        private final String holderName;
        private final PricingPlan plan;
        private final List<Transaction> transactions = new ArrayList<>();
        private final List<String> refundedPurchaseIds = new ArrayList<>();
        private boolean blocked = false;
        private int transactionSequence = 0;

        public SmartCard(String cardId, String holderName, PricingPlan plan) {
            this.cardId = cardId;
            this.holderName = holderName;
            this.plan = plan;
        }

        public long getBalancePaise() {
            long sum = 0;
            for (Transaction transaction : transactions) {
                sum += transaction.getAmountPaise();
            }
            return sum;
        }

        public Transaction topUp(double rupees) {
            ensureActive();
            long amount = Money.toPaise(rupees);
            if (amount < MIN_TOP_UP) {
                throw new IllegalArgumentException("Minimum top-up is ₹" + Money.format(MIN_TOP_UP) + ".");
            }
            if (getBalancePaise() + amount > MAX_BALANCE) {
                throw new IllegalArgumentException("Balance cannot exceed ₹" + Money.format(MAX_BALANCE) + ".");
            }
            return record(Transaction.TOP_UP, amount, "Top-up", null);
        }

        public Transaction purchase(String itemName, double basePriceRupees) {
            ensureActive();
            long basePrice = Money.toPaise(basePriceRupees);
            if (basePrice <= 0) {
                throw new IllegalArgumentException("Item price must be positive.");
            }
            long charged = plan.priceFor(basePrice);
            long balance = getBalancePaise();
            if (charged > balance) {
                throw new IllegalStateException("Insufficient balance (required ₹" + Money.format(charged) + ", available ₹" + Money.format(balance) + ").");
            }
            return record(Transaction.PURCHASE, -charged, itemName, null);
        }

        public Transaction refund(String purchaseTransactionId) {
            Transaction purchase = findTransaction(purchaseTransactionId);
            if (purchase == null || !Transaction.PURCHASE.equals(purchase.getType())) {
                throw new IllegalArgumentException("No purchase " + purchaseTransactionId + " found on card " + cardId + ".");
            }
            if (refundedPurchaseIds.contains(purchaseTransactionId)) {
                throw new IllegalStateException(purchase.getDescription() + " has already been refunded.");
            }
            refundedPurchaseIds.add(purchaseTransactionId);
            return record(Transaction.REFUND, -purchase.getAmountPaise(), purchase.getDescription(), purchaseTransactionId);
        }

        public void block() {
            blocked = true;
        }

        public void unblock() {
            blocked = false;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public String miniStatement() {
            StringBuilder entries = new StringBuilder();
            for (Transaction transaction : transactions) {
                if (entries.length() > 0) {
                    entries.append(", ");
                }
                long amount = transaction.getAmountPaise();
                entries.append(amount >= 0 ? "+" : "-").append(Money.format(Math.abs(amount)));
            }
            return "Mini-statement for " + cardId + ": " + entries + " = ₹" + Money.format(getBalancePaise()) + ".";
        }

        private void ensureActive() {
            if (blocked) {
                throw new IllegalStateException("Card " + cardId + " is blocked.");
            }
        }

        private Transaction record(String type, long amount, String description, String relatedId) {
            transactionSequence++;
            Transaction transaction = new Transaction(cardId + "-T" + transactionSequence, type, amount, description, relatedId);
            transactions.add(transaction);
            return transaction;
        }

        private Transaction findTransaction(String transactionId) {
            for (Transaction transaction : transactions) {
                if (transaction.getTransactionId().equals(transactionId)) {
                    return transaction;
                }
            }
            return null;
        }

        public List<Transaction> getTransactions() {
            return new ArrayList<>(transactions);
        }

        public String getCardId() {
            return cardId;
        }

        public String getHolderName() {
            return holderName;
        }

        public PricingPlan getPlan() {
            return plan;
        }
    }

    static class CanteenCounter {
        public void topUp(SmartCard card, double rupees) {
            try {
                Transaction t = card.topUp(rupees);
                System.out.println(card.getCardId() + " topped up with ₹" + Money.format(t.getAmountPaise()) + ". Balance: ₹" + Money.format(card.getBalancePaise()) + ".");
            } catch (RuntimeException e) {
                System.out.println("Top-up failed: " + e.getMessage());
            }
        }

        public Transaction purchase(SmartCard card, String itemName, double basePrice) {
            try {
                Transaction t = card.purchase(itemName, basePrice);
                System.out.println(itemName + " purchased for ₹" + Money.format(-t.getAmountPaise()) + ". Balance: ₹" + Money.format(card.getBalancePaise()) + ".");
                return t;
            } catch (RuntimeException e) {
                System.out.println("Purchase failed: " + e.getMessage());
                return null;
            }
        }

        public void refund(SmartCard card, Transaction purchase) {
            try {
                Transaction t = card.refund(purchase.getTransactionId());
                System.out.println("Refund of ₹" + Money.format(t.getAmountPaise()) + " for " + t.getDescription() + " processed. Balance: ₹" + Money.format(card.getBalancePaise()) + ".");
            } catch (RuntimeException e) {
                System.out.println("Refund rejected: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        CanteenCounter counter = new CanteenCounter();
        SmartCard card = new SmartCard("C-2045", "Vikram", new HostellerPlan());

        counter.topUp(card, 500);
        Transaction vegThali = counter.purchase(card, "Veg Thali", 120);
        counter.purchase(card, "Cold Coffee", 60);
        counter.purchase(card, "Party Order", 400);
        counter.refund(card, vegThali);
        counter.refund(card, vegThali);
        System.out.println(card.miniStatement());

        card.block();
        counter.purchase(card, "Samosa", 20);
        counter.topUp(card, 200);
        card.unblock();
        counter.purchase(card, "Samosa", 20);
    }
}

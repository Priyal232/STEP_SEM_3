import java.util.*;

public class ImmutableLoanReceipt {

    static class LoanReceipt {
        private final String memberId;
        private final String[] bookIds;

        static {
            System.out.println("LoanReceipt system initialized.");
        }

        public LoanReceipt(String memberId, String[] bookIds) {
            for (int i = 0; i < bookIds.length; i++) {
                if (!isValidBookId(bookIds[i])) {
                    throw new IllegalArgumentException("Invalid book id: " + bookIds[i]);
                }
            }
            this.memberId = memberId;
            this.bookIds = Arrays.copyOf(bookIds, bookIds.length);
        }

        private static boolean isValidBookId(String id) {
            if (id == null || id.length() != 6) return false;
            if (!id.startsWith("BK-")) return false;
            for (int i = 3; i < 6; i++) {
                if (!Character.isDigit(id.charAt(i))) return false;
            }
            return true;
        }

        String[] getBookIds() {
            return Arrays.copyOf(bookIds, bookIds.length);
        }

        LoanReceipt withCorrectedBookId(int index, String newId) {
            String[] updated = Arrays.copyOf(bookIds, bookIds.length);
            updated[index] = newId;
            return new LoanReceipt(this.memberId, updated);
        }

        static String processNightlyCirculation(LoanReceipt[] receipts) {
            int processed = 0, nulls = 0, referenceOnly = 0, regular = 0;
            for (int i = 0; i < receipts.length; i++) {
                LoanReceipt r = receipts[i];
                if (r == null) {
                    nulls++;
                    continue;
                }
                if (r instanceof ReferenceOnlyLoanReceipt) referenceOnly++;
                else regular++;
                processed++;
            }
            return processed + " processed | " + nulls + " null skipped | " + referenceOnly + " reference-only | " + regular + " regular";
        }
    }

    static class ReferenceOnlyLoanReceipt extends LoanReceipt {
        private String roomNumber;

        public ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
            super(memberId, bookIds);
            this.roomNumber = roomNumber;
        }
    }

    public static void main(String[] args) {
        try {
            new LoanReceipt("LIB-8841", new String[]{"BK-100", "bad"});
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        LoanReceipt r = new LoanReceipt("LIB-8841", new String[]{"BK-100", "BK-101"});
        String[] ids = r.getBookIds();
        ids[0] = "HACKED";
        System.out.println(r.getBookIds()[0]);

        LoanReceipt[] batch = {
                new ReferenceOnlyLoanReceipt("LIB-001", new String[]{"BK-200"}, "Reading Room 3"),
                null,
                new LoanReceipt("LIB-002", new String[]{"BK-201"})
        };
        System.out.println(LoanReceipt.processNightlyCirculation(batch));
    }
}

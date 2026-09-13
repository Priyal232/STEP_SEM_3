public class BankTransactionReferenceGenerator {

    static String normalizeReference(String raw) {
        String trimmed = raw.trim();
        String prefix = trimmed.substring(0, 3).toUpperCase();
        String rest = trimmed.substring(3);
        return prefix + rest;
    }

    static String validateAndFormat(String reference) {
        if (reference.length() != 14) {
            return "Invalid: wrong length";
        }
        for (int i = 0; i < 3; i++) {
            if (!Character.isLetter(reference.charAt(i))) {
                return "Invalid: bank code must be 3 letters";
            }
        }
        for (int i = 3; i < 14; i++) {
            if (!Character.isDigit(reference.charAt(i))) {
                return "Invalid: body must be digits";
            }
        }
        String bankCode = reference.substring(0, 3);
        String date = reference.substring(3, 9);
        String seq = reference.substring(9, 14);
        String dd = date.substring(0, 2);
        String mm = date.substring(2, 4);
        String yy = date.substring(4, 6);
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(bankCode).append("] DATE: ").append(dd).append("/").append(mm).append("/").append(yy)
                .append(" | SEQ: ").append(seq);
        return sb.toString();
    }

    public static void main(String[] args) {
        String ref = normalizeReference(" hdf03022600042 ");
        System.out.println(validateAndFormat(ref));
        System.out.println(validateAndFormat("12F03022600042"));
    }
}

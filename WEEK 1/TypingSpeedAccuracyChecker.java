public class TypingSpeedAccuracyChecker {

    static void checkTypingAccuracy(String original, String typed) {
        int matched = 0;
        int firstMismatch = -1;
        int len = original.length();
        for (int i = 0; i < len; i++) {
            if (original.charAt(i) == typed.charAt(i)) {
                matched++;
            } else if (firstMismatch == -1) {
                firstMismatch = i;
            }
        }
        double accuracy = ((double) matched / len) * 100;
        String accStr = String.format("%.2f", accuracy);
        if (firstMismatch == -1) {
            System.out.println("Matched: " + matched + "/" + len + " | Accuracy: " + accStr + "% | No Mismatches");
        } else {
            System.out.println("Matched: " + matched + "/" + len + " | Accuracy: " + accStr + "% | First Mismatch at position "
                    + (firstMismatch + 1) + " ('" + original.charAt(firstMismatch) + "' vs '" + typed.charAt(firstMismatch) + "')");
        }
    }

    public static void main(String[] args) {
        checkTypingAccuracy("hello world", "hello worlt");
        checkTypingAccuracy("coding", "coding");
    }
}

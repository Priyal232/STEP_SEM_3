public class PalindromeChecker {

    static boolean isPalindromeIterative(String text) {
        int left = 0, right = text.length() - 1;
        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    static boolean isPalindromeRecursive(String text) {
        if (text.length() <= 1) return true;
        if (text.charAt(0) != text.charAt(text.length() - 1)) return false;
        return isPalindromeRecursive(text.substring(1, text.length() - 1));
    }

    static boolean isPalindromeArrayReversal(String text) {
        char[] arr = text.toCharArray();
        char[] reversed = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return new String(reversed).equals(text);
    }

    public static void main(String[] args) {
        String[] inputs = {"madam", "hello"};
        for (String text : inputs) {
            System.out.println(text + " -> Iterative: " + (isPalindromeIterative(text) ? "Palindrome" : "Not Palindrome")
                    + " | Recursive: " + (isPalindromeRecursive(text) ? "Palindrome" : "Not Palindrome")
                    + " | Array Reversal: " + (isPalindromeArrayReversal(text) ? "Palindrome" : "Not Palindrome"));
        }
    }
}

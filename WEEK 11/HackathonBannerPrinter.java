import java.util.ArrayDeque;
import java.util.Deque;

public class HackathonBannerPrinter {

    static String expandBanner(String code) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> texts = new ArrayDeque<>();
        StringBuilder current = new StringBuilder();
        int k = 0;
        for (char ch : code.toCharArray()) {
            if (Character.isDigit(ch)) {
                k = k * 10 + (ch - '0');
            } else if (ch == '[') {
                counts.push(k);
                texts.push(current);
                current = new StringBuilder();
                k = 0;
            } else if (ch == ']') {
                int times = counts.pop();
                StringBuilder outer = texts.pop();
                String inner = current.toString();
                for (int i = 0; i < times; i++) {
                    outer.append(inner);
                }
                current = outer;
            } else {
                current.append(ch);
            }
        }
        return current.toString();
    }

    public static void main(String[] args) {
        System.out.println("\"" + expandBanner("3[ab]2[c]") + "\"");
        System.out.println("\"" + expandBanner("2[x3[y]]z") + "\"");
    }
}

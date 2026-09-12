public class TrafficSignalStreakAnalyzer {

    static void findLongestStreak(String signalLog) {
        int maxLen = 1;
        char maxChar = signalLog.charAt(0);
        int curLen = 1;
        char curChar = signalLog.charAt(0);
        for (int i = 1; i < signalLog.length(); i++) {
            if (signalLog.charAt(i) == curChar) {
                curLen++;
            } else {
                curChar = signalLog.charAt(i);
                curLen = 1;
            }
            if (curLen > maxLen) {
                maxLen = curLen;
                maxChar = curChar;
            }
        }
        System.out.println("Longest Streak: '" + maxChar + "' repeated " + maxLen + " times");
    }

    public static void main(String[] args) {
        findLongestStreak("RRGGGYRR");
        findLongestStreak("RRRRYYGG");
    }
}

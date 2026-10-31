import java.util.ArrayList;
import java.util.List;

public class MallFootfallRangeReport {

    static List<Long> footfallReport(int[] visitors, int[][] queries) {
        long[] prefix = new long[visitors.length + 1];
        for (int i = 0; i < visitors.length; i++) {
            prefix[i + 1] = prefix[i] + visitors[i];
        }
        List<Long> result = new ArrayList<>();
        for (int[] q : queries) {
            result.add(prefix[q[1] + 1] - prefix[q[0]]);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] visitors = {12, 7, 3, 9, 15, 4, 8};
        int[][] queries = {{0, 2}, {2, 5}, {4, 6}, {3, 3}};
        System.out.println(footfallReport(visitors, queries));
    }
}

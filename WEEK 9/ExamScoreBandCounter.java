public class ExamScoreBandCounter {

    static int firstAtLeast(int[] scores, int value) {
        int lo = 0, hi = scores.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (scores[mid] < value) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    static int firstGreater(int[] scores, int value) {
        int lo = 0, hi = scores.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (scores[mid] <= value) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    static int countInBand(int[] scores, int low, int high) {
        return firstGreater(scores, high) - firstAtLeast(scores, low);
    }

    public static void main(String[] args) {
        int[] scores = {35, 42, 42, 50, 58, 58, 58, 63, 71, 88};
        System.out.println(countInBand(scores, 42, 58));
        System.out.println(countInBand(scores, 90, 100));
    }
}

public class ProductPriceFinder {

    static int lowerBound(int[] prices, int target) {
        int lo = 0, hi = prices.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (prices[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    static int upperBound(int[] prices, int target) {
        int lo = 0, hi = prices.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (prices[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    static int countInRange(int[] prices, int low, int high) {
        return upperBound(prices, high) - lowerBound(prices, low);
    }

    public static void main(String[] args) {
        int[] prices = {100, 150, 150, 200, 300, 450};
        int low = 150, high = 300;
        int lb = lowerBound(prices, low);
        int ub = upperBound(prices, high);
        System.out.println("Lower bound index " + lb);
        System.out.println("Upper bound index " + ub);
        StringBuilder sb = new StringBuilder();
        for (int i = lb; i < ub; i++) {
            sb.append(prices[i]);
            if (i < ub - 1) {
                sb.append(", ");
            }
        }
        System.out.println("Count " + countInRange(prices, low, high) + " (products with prices " + sb + ")");
    }
}

public class WarehouseBinGridScan {

    static long[] warehouseSummary(int[][] grid) {
        long total = 0;
        int maxRow = 0, maxCol = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                total += grid[r][c];
                if (grid[r][c] > grid[maxRow][maxCol]) {
                    maxRow = r;
                    maxCol = c;
                }
            }
        }
        return new long[]{total, maxRow, maxCol};
    }

    public static void main(String[] args) {
        int[][] grid = {
            {4, 9, 2},
            {7, 1, 6},
            {3, 12, 5}
        };
        long[] s = warehouseSummary(grid);
        System.out.println("total = " + s[0] + ", maxCoordinate = (" + s[1] + ", " + s[2] + ")");
    }
}

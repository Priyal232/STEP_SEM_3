import java.util.ArrayDeque;
import java.util.Deque;

public class FireDrillExits {

    static int[][] exitDistance(char[][] plan) {
        int rows = plan.length, cols = plan[0].length;
        int[][] dist = new int[rows][cols];
        Deque<int[]> queue = new ArrayDeque<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                dist[r][c] = -1;
                if (plan[r][c] == 'E') {
                    dist[r][c] = 0;
                    queue.offer(new int[]{r, c});
                }
            }
        }
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : dirs) {
                int nr = cell[0] + d[0], nc = cell[1] + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && plan[nr][nc] == '.' && dist[nr][nc] == -1) {
                    dist[nr][nc] = dist[cell[0]][cell[1]] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        char[][] plan = {
            ".#.E".toCharArray(),
            "...#".toCharArray(),
            "E#..".toCharArray()
        };
        int[][] dist = exitDistance(plan);
        for (int[] row : dist) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < row.length; c++) {
                sb.append(row[c]);
                if (c < row.length - 1) {
                    sb.append(" ");
                }
            }
            System.out.println(sb);
        }
    }
}

public class StudentMarksGrid {

    static int[] totals(int[][] marks) {
        int[] result = new int[marks.length];
        for (int i = 0; i < marks.length; i++) {
            int sum = 0;
            for (int m : marks[i]) {
                sum += m;
            }
            result[i] = sum;
        }
        return result;
    }

    static int[] toppers(int[][] marks, int subjectCount) {
        int[] topper = new int[subjectCount];
        for (int s = 0; s < subjectCount; s++) {
            topper[s] = -1;
            for (int i = 0; i < marks.length; i++) {
                if (s < marks[i].length && (topper[s] == -1 || marks[i][s] > marks[topper[s]][s])) {
                    topper[s] = i;
                }
            }
        }
        return topper;
    }

    public static void main(String[] args) {
        String[] subjects = {"Math", "Sci", "Eng"};
        String[] students = {"Anu", "Ravi", "Meena"};
        int[][] marks = {
            {80, 90, 70},
            {60, 85},
            {95, 75, 88}
        };

        int[] total = totals(marks);
        StringBuilder sb = new StringBuilder("Totals ");
        for (int i = 0; i < students.length; i++) {
            sb.append(students[i]).append(" ").append(total[i]);
            if (i < students.length - 1) {
                sb.append(", ");
            }
        }
        System.out.println(sb);

        int[] top = toppers(marks, subjects.length);
        sb = new StringBuilder("Toppers ");
        for (int s = 0; s < subjects.length; s++) {
            sb.append(subjects[s]).append(" ");
            if (top[s] == -1) {
                sb.append("none");
            } else {
                sb.append(students[top[s]]).append(" ").append(marks[top[s]][s]);
            }
            if (s < subjects.length - 1) {
                sb.append(", ");
            }
        }
        System.out.println(sb);
    }
}

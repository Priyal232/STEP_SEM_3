public class ExamHallSeatDuplicationChecker {

    static void checkDuplicateSeats(int[] seatNumbers) {
        boolean found = false;
        boolean[] reported = new boolean[seatNumbers.length];
        for (int i = 0; i < seatNumbers.length; i++) {
            if (reported[i]) continue;
            for (int j = i + 1; j < seatNumbers.length; j++) {
                if (seatNumbers[i] == seatNumbers[j]) {
                    System.out.println("Duplicate Seat Number Found: " + seatNumbers[i]);
                    found = true;
                    reported[j] = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("No Duplicate Seats Found");
        }
    }

    public static void main(String[] args) {
        checkDuplicateSeats(new int[]{101, 102, 103, 102, 105});
        checkDuplicateSeats(new int[]{101, 102, 103, 104, 105});
    }
}

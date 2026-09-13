public class FeeHostelCapstone {

    static class FeeAccount {
        private String regNo;
        private double totalFee;
        private double amountPaid;

        FeeAccount(String regNo, double totalFee, double amountPaid) {
            this.regNo = regNo;
            this.totalFee = totalFee;
            this.amountPaid = amountPaid;
        }

        void pay(double amount) {
            if (amount <= 0) return;
            amountPaid += amount;
        }

        double getDue() {
            return totalFee - amountPaid;
        }
    }

    static class HostelFeeAccount extends FeeAccount {
        HostelFeeAccount(String regNo, double totalFee, double amountPaid) {
            super(regNo, totalFee, amountPaid);
        }
    }

    static class HostelRoom {
        String roomNo;
        int beds;
        int occupied;

        HostelRoom(String roomNo, int beds, int occupied) {
            this.roomNo = roomNo;
            this.beds = beds;
            this.occupied = occupied;
        }

        void allot(String name) {
            occupied++;
        }

        static HostelRoom findAvailableRoom(HostelRoom[] rooms) {
            for (int i = 0; i < rooms.length; i++) {
                if (rooms[i].occupied < rooms[i].beds) {
                    return rooms[i];
                }
            }
            return null;
        }
    }

    static class StudentRecord {
        String name;
        String regNo;
        HostelFeeAccount feeAccount;
        HostelRoom room;
        static int totalStudents = 0;

        StudentRecord(String name, String regNo, HostelFeeAccount feeAccount, HostelRoom room) {
            this.name = name;
            this.regNo = regNo;
            this.feeAccount = feeAccount;
            this.room = room;
            totalStudents++;
        }

        String fullStatus() {
            String roomText = (room == null) ? "unallotted" : room.roomNo;
            return name + " | Due: Rs " + feeAccount.getDue() + " | Room: " + roomText;
        }
    }

    public static void main(String[] args) {
        HostelFeeAccount fee1 = new HostelFeeAccount("R1", 200000, 60000);
        HostelFeeAccount fee2 = new HostelFeeAccount("R2", 200000, 20000);
        HostelFeeAccount fee3 = new HostelFeeAccount("R3", 200000, 0);
        HostelRoom[] rooms = {new HostelRoom("C-214", 1, 0), new HostelRoom("C-507", 1, 0)};

        HostelRoom room1 = HostelRoom.findAvailableRoom(rooms);
        room1.allot("Ravi");
        StudentRecord rec1 = new StudentRecord("Ravi", "R1", fee1, room1);

        HostelRoom room2 = HostelRoom.findAvailableRoom(rooms);
        room2.allot("Anitha");
        StudentRecord rec2 = new StudentRecord("Anitha", "R2", fee2, room2);

        StudentRecord rec3 = new StudentRecord("Karthik", "R3", fee3, null);

        System.out.println(rec1.fullStatus());
        System.out.println(rec2.fullStatus());
        System.out.println(rec3.fullStatus());
        System.out.println("Total students: " + StudentRecord.totalStudents);
    }
}

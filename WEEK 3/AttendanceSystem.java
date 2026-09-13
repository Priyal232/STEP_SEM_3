public class AttendanceSystem {

    static class SrmStudent {
        String name;
        String regNo;
        int attendance;

        SrmStudent(String name, String regNo, int attendance) {
            this.name = name;
            this.regNo = regNo;
            this.attendance = attendance;
        }

        void addAttendanceUpdate(int newAttendance) {
            this.attendance = newAttendance;
        }

        boolean isEligible() {
            return attendance >= 75;
        }

        static double classAverage(SrmStudent[] students) {
            int total = 0;
            for (int i = 0; i < students.length; i++) {
                total += students[i].attendance;
            }
            return (double) total / students.length;
        }
    }

    public static void main(String[] args) {
        SrmStudent[] students = {
                new SrmStudent("Ravi", "REG1", 82),
                new SrmStudent("Anitha", "REG2", 68),
                new SrmStudent("Karthik", "REG3", 91),
                new SrmStudent("Meera", "REG4", 74),
                new SrmStudent("Suresh", "REG5", 60)
        };
        for (int i = 0; i < students.length; i++) {
            String status = students[i].isEligible() ? "Eligible" : "Detained";
            System.out.println(students[i].name + " - " + students[i].attendance + "% - " + status);
        }
        System.out.println("Class average: " + SrmStudent.classAverage(students) + "%");
    }
}

public class InstanceVsStaticSrmStudent {

    static class SrmStudentBroken {
        static String name;
        static String regNo;
        static int attendance;

        SrmStudentBroken(String name, String regNo, int attendance) {
            SrmStudentBroken.name = name;
            SrmStudentBroken.regNo = regNo;
            SrmStudentBroken.attendance = attendance;
        }
    }

    static class SrmStudent {
        String name;
        String regNo;
        int attendance;
        static String university = "SRM";
        static int admissionCount = 0;

        SrmStudent(String name, int attendance) {
            this.name = name;
            admissionCount++;
            this.regNo = "RA23110030101" + admissionCount;
            this.attendance = attendance;
        }

        void printIdCard() {
            System.out.println(name + " | " + regNo);
        }

        static void printTotalAdmissions() {
            System.out.println("Students admitted so far: " + admissionCount);
        }
    }

    public static void main(String[] args) {
        new SrmStudentBroken("Ravi", "R1", 80);
        new SrmStudentBroken("Meera", "R2", 90);
        System.out.println(SrmStudentBroken.name);
        System.out.println(SrmStudentBroken.name);

        SrmStudent s1 = new SrmStudent("Ravi", 80);
        SrmStudent s2 = new SrmStudent("Meera", 90);
        s1.printIdCard();
        s2.printIdCard();
        SrmStudent.printTotalAdmissions();
    }
}

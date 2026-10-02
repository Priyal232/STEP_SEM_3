public class QuarterlyBonusCalculator {
    static abstract class StaffMember {
        private double baseSalary;
        private final double bonusRate;

        public StaffMember(double baseSalary) {
            this(baseSalary, 0.10);
        }

        public StaffMember(double baseSalary, double bonusRate) {
            this.baseSalary = baseSalary;
            this.bonusRate = bonusRate;
        }

        public abstract double calculateBonus();

        public double getSalary() {
            return baseSalary;
        }

        public void setSalary(double baseSalary) {
            if (baseSalary < 0) {
                System.out.println("Rejected: salary cannot be negative. Salary unchanged at $" + this.baseSalary);
                return;
            }
            this.baseSalary = baseSalary;
        }

        public double getBonusRate() {
            return bonusRate;
        }
    }

    interface Auditable {
        String auditRecord();
    }

    static class TeamLead extends StaffMember implements Auditable {
        private final int teamSize;

        public TeamLead(double baseSalary, int teamSize) {
            super(baseSalary);
            this.teamSize = teamSize;
        }

        public TeamLead(double baseSalary, double bonusRate, int teamSize) {
            super(baseSalary, bonusRate);
            this.teamSize = teamSize;
        }

        @Override
        public double calculateBonus() {
            return getSalary() * getBonusRate();
        }

        @Override
        public String auditRecord() {
            return "TeamLead audit: " + teamSize + " team members, salary $" + getSalary();
        }
    }

    static class Intern extends StaffMember {
        public Intern(double baseSalary) {
            super(baseSalary, 0.05);
        }

        @Override
        public double calculateBonus() {
            return getSalary() * getBonusRate();
        }
    }

    static String getAuditIfApplicable(StaffMember s) {
        if (s instanceof Auditable) {
            Auditable auditable = (Auditable) s;
            return auditable.auditRecord();
        }
        return "No audit required";
    }

    public static void main(String[] args) {
        TeamLead t = new TeamLead(60000, 5);
        System.out.println(t.calculateBonus());

        TeamLead t2 = new TeamLead(60000, 0.20, 5);
        System.out.println(t2.calculateBonus());

        t.setSalary(-5000);
        System.out.println("Salary: " + t.getSalary());

        StaffMember ref = t;
        System.out.println(getAuditIfApplicable(ref));

        StaffMember intern = new Intern(20000);
        System.out.println(getAuditIfApplicable(intern));
    }
}

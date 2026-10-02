import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TheElectiveSeatRush {
    interface CreditPolicy {
        String getTypeName();

        int getCreditLimit();
    }

    static class RegularCreditPolicy implements CreditPolicy {
        @Override
        public String getTypeName() {
            return "Regular";
        }

        @Override
        public int getCreditLimit() {
            return 24;
        }
    }

    static class HonorsCreditPolicy implements CreditPolicy {
        @Override
        public String getTypeName() {
            return "Honors";
        }

        @Override
        public int getCreditLimit() {
            return 28;
        }
    }

    static class ExchangeCreditPolicy implements CreditPolicy {
        @Override
        public String getTypeName() {
            return "Exchange";
        }

        @Override
        public int getCreditLimit() {
            return 20;
        }
    }

    static class Student {
        private final String name;
        private final CreditPolicy creditPolicy;
        private int currentCredits;

        public Student(String name, CreditPolicy creditPolicy, int currentCredits) {
            this.name = name;
            this.creditPolicy = creditPolicy;
            this.currentCredits = currentCredits;
        }

        public boolean canAdd(int credits) {
            return currentCredits + credits <= creditPolicy.getCreditLimit();
        }

        void addCredits(int credits) {
            currentCredits += credits;
        }

        void removeCredits(int credits) {
            currentCredits -= credits;
        }

        public String creditSummary() {
            return currentCredits + "/" + creditPolicy.getCreditLimit();
        }

        public String projectedSummary(int extraCredits) {
            return (currentCredits + extraCredits) + "/" + creditPolicy.getCreditLimit();
        }

        public String getName() {
            return name;
        }

        public CreditPolicy getCreditPolicy() {
            return creditPolicy;
        }

        public int getCurrentCredits() {
            return currentCredits;
        }
    }

    static class Enrollment {
        private final Student student;
        private final Elective elective;

        Enrollment(Student student, Elective elective) {
            this.student = student;
            this.elective = elective;
        }

        public Student getStudent() {
            return student;
        }

        public Elective getElective() {
            return elective;
        }
    }

    static class DropOutcome {
        private final Student promoted;
        private final List<Student> skipped;

        DropOutcome(Student promoted, List<Student> skipped) {
            this.promoted = promoted;
            this.skipped = skipped;
        }

        public Student getPromoted() {
            return promoted;
        }

        public List<Student> getSkipped() {
            return new ArrayList<>(skipped);
        }
    }

    static class Elective {
        private final String name;
        private final int credits;
        private final int capacity;
        private final List<Enrollment> enrollments = new ArrayList<>();
        private final Queue<Student> waitlist = new LinkedList<>();

        public Elective(String name, int credits, int capacity) {
            if (capacity <= 0 || credits <= 0) {
                throw new IllegalArgumentException("Capacity and credits must be positive.");
            }
            this.name = name;
            this.credits = credits;
            this.capacity = capacity;
        }

        public boolean isEnrolled(Student student) {
            return findEnrollment(student) != null;
        }

        public boolean isWaitlisted(Student student) {
            return waitlist.contains(student);
        }

        public boolean isFull() {
            return enrollments.size() >= capacity;
        }

        Enrollment enroll(Student student) {
            if (isFull()) {
                throw new IllegalStateException(name + " is full.");
            }
            if (!student.canAdd(credits)) {
                throw new IllegalStateException(student.getName() + " would exceed the credit limit.");
            }
            Enrollment enrollment = new Enrollment(student, this);
            enrollments.add(enrollment);
            student.addCredits(credits);
            return enrollment;
        }

        int addToWaitlist(Student student) {
            waitlist.add(student);
            return waitlist.size();
        }

        DropOutcome dropAndPromote(Student student) {
            Enrollment enrollment = findEnrollment(student);
            if (enrollment == null) {
                throw new IllegalStateException("Drop failed: " + student.getName() + " is not enrolled in " + name + ".");
            }
            enrollments.remove(enrollment);
            student.removeCredits(credits);
            List<Student> skipped = new ArrayList<>();
            Student promoted = null;
            while (!waitlist.isEmpty() && promoted == null) {
                Student next = waitlist.poll();
                if (next.canAdd(credits)) {
                    enroll(next);
                    promoted = next;
                } else {
                    skipped.add(next);
                }
            }
            return new DropOutcome(promoted, skipped);
        }

        private Enrollment findEnrollment(Student student) {
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudent() == student) {
                    return enrollment;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public int getCredits() {
            return credits;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getEnrolledCount() {
            return enrollments.size();
        }

        public int getWaitlistSize() {
            return waitlist.size();
        }
    }

    static class EnrollmentService {
        public void enroll(Student student, Elective elective) {
            if (elective.isEnrolled(student)) {
                System.out.println("Enrollment failed: " + student.getName() + " is already enrolled in " + elective.getName() + ".");
                return;
            }
            if (elective.isWaitlisted(student)) {
                System.out.println("Enrollment failed: " + student.getName() + " is already on the waitlist for " + elective.getName() + ".");
                return;
            }
            if (!student.canAdd(elective.getCredits())) {
                System.out.println("Enrollment failed: " + student.getName() + " would exceed the " + student.getCreditPolicy().getTypeName() + " credit limit (" + student.projectedSummary(elective.getCredits()) + ").");
                return;
            }
            if (elective.isFull()) {
                int position = elective.addToWaitlist(student);
                System.out.println(elective.getName() + " is full. " + student.getName() + " added to waitlist (position " + position + ").");
                return;
            }
            elective.enroll(student);
            System.out.println(student.getName() + " enrolled in " + elective.getName() + " (credits: " + student.creditSummary() + ").");
        }

        public void drop(Student student, Elective elective) {
            try {
                DropOutcome outcome = elective.dropAndPromote(student);
                System.out.println(student.getName() + " dropped " + elective.getName() + " (credits: " + student.creditSummary() + ").");
                for (Student skipped : outcome.getSkipped()) {
                    System.out.println(skipped.getName() + " removed from waitlist: would exceed the " + skipped.getCreditPolicy().getTypeName() + " credit limit (" + skipped.projectedSummary(elective.getCredits()) + ").");
                }
                Student promoted = outcome.getPromoted();
                if (promoted != null) {
                    System.out.println(promoted.getName() + " promoted from waitlist and enrolled in " + elective.getName() + " (credits: " + promoted.creditSummary() + ").");
                }
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        EnrollmentService service = new EnrollmentService();
        Elective cloud = new Elective("Cloud Computing", 4, 2);

        Student asha = new Student("Asha", new RegularCreditPolicy(), 20);
        Student ravi = new Student("Ravi", new HonorsCreditPolicy(), 22);
        Student neha = new Student("Neha", new ExchangeCreditPolicy(), 12);
        Student kiran = new Student("Kiran", new RegularCreditPolicy(), 22);

        service.enroll(asha, cloud);
        service.enroll(ravi, cloud);
        service.enroll(neha, cloud);
        service.enroll(kiran, cloud);
        service.drop(asha, cloud);

        service.enroll(ravi, cloud);
        System.out.println("Enrolled: " + cloud.getEnrolledCount() + "/" + cloud.getCapacity() + ", waitlist: " + cloud.getWaitlistSize());
    }
}

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmployeeLeaveRequestManagement {
    static abstract class Employee {
        private final String employeeId;
        private final String name;
        private final List<LeaveRequest> leaveRequests = new ArrayList<>();

        protected Employee(String employeeId, String name) {
            this.employeeId = employeeId;
            this.name = name;
        }

        public abstract String getEmployeeType();

        public abstract int getMaxDaysPerRequest();

        public void validateLeave(long requestedDays) {
            if (requestedDays > getMaxDaysPerRequest()) {
                throw new IllegalArgumentException("Leave request not submitted: " + getEmployeeType() + " employees can request at most " + getMaxDaysPerRequest() + " days at a time.");
            }
        }

        void addLeaveRequest(LeaveRequest request) {
            leaveRequests.add(request);
        }

        public List<LeaveRequest> getLeaveRequests() {
            return new ArrayList<>(leaveRequests);
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public String getName() {
            return name;
        }
    }

    static class FullTimeEmployee extends Employee {
        public FullTimeEmployee(String employeeId, String name) {
            super(employeeId, name);
        }

        @Override
        public String getEmployeeType() {
            return "Full-time";
        }

        @Override
        public int getMaxDaysPerRequest() {
            return 15;
        }
    }

    static class PartTimeEmployee extends Employee {
        public PartTimeEmployee(String employeeId, String name) {
            super(employeeId, name);
        }

        @Override
        public String getEmployeeType() {
            return "Part-time";
        }

        @Override
        public int getMaxDaysPerRequest() {
            return 7;
        }
    }

    static class ContractEmployee extends Employee {
        public ContractEmployee(String employeeId, String name) {
            super(employeeId, name);
        }

        @Override
        public String getEmployeeType() {
            return "Contract";
        }

        @Override
        public int getMaxDaysPerRequest() {
            return 3;
        }
    }

    static class Manager {
        private final String name;

        public Manager(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class LeaveRequest {
        public static final String PENDING = "Pending";
        public static final String APPROVED = "Approved";
        public static final String REJECTED = "Rejected";
        private static int requestCounter = 0;
        private final String requestId;
        private final Employee employee;
        private final LocalDate fromDate;
        private final LocalDate toDate;
        private String status = PENDING;
        private Manager reviewedBy;

        LeaveRequest(Employee employee, LocalDate fromDate, LocalDate toDate) {
            requestCounter++;
            this.requestId = "LR-" + requestCounter;
            this.employee = employee;
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        private boolean canTransitionTo(String newStatus) {
            return PENDING.equals(status) && (APPROVED.equals(newStatus) || REJECTED.equals(newStatus));
        }

        void changeStatus(String newStatus, Manager reviewer) {
            if (!canTransitionTo(newStatus)) {
                String action = PENDING.equals(newStatus) ? "revert to Pending" : "change to " + newStatus;
                throw new IllegalStateException("Cannot change status: " + status + " request cannot " + action + ".");
            }
            status = newStatus;
            reviewedBy = reviewer;
        }

        public long getDays() {
            return ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        }

        public String getRequestId() {
            return requestId;
        }

        public Employee getEmployee() {
            return employee;
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public String getStatus() {
            return status;
        }

        public Manager getReviewedBy() {
            return reviewedBy;
        }
    }

    static class LeaveManager {
        private final List<LeaveRequest> requests = new ArrayList<>();

        public LeaveRequest submit(Employee employee, LocalDate from, LocalDate to) {
            if (to.isBefore(from)) {
                throw new IllegalArgumentException("Leave request not submitted: end date is before start date.");
            }
            LeaveRequest request = new LeaveRequest(employee, from, to);
            employee.validateLeave(request.getDays());
            requests.add(request);
            employee.addLeaveRequest(request);
            System.out.println("Leave request submitted by " + employee.getName() + " for " + from + " to " + to + ". Status: " + request.getStatus() + ".");
            return request;
        }

        public void approve(Manager manager, LeaveRequest request) {
            review(manager, request, LeaveRequest.APPROVED);
        }

        public void reject(Manager manager, LeaveRequest request) {
            review(manager, request, LeaveRequest.REJECTED);
        }

        public void review(Manager manager, LeaveRequest request, String decision) {
            try {
                request.changeStatus(decision, manager);
                System.out.println("Leave request for " + request.getEmployee().getName() + " " + decision.toLowerCase() + ". Status: " + decision + ".");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }

        public List<LeaveRequest> getPendingRequests() {
            List<LeaveRequest> pending = new ArrayList<>();
            for (LeaveRequest request : requests) {
                if (LeaveRequest.PENDING.equals(request.getStatus())) {
                    pending.add(request);
                }
            }
            return pending;
        }
    }

    public static void main(String[] args) {
        LeaveManager leaveManager = new LeaveManager();
        Manager manager = new Manager("Rakesh");

        Employee john = new FullTimeEmployee("E1", "John Doe");
        Employee jane = new PartTimeEmployee("E2", "Jane Smith");
        Employee sam = new ContractEmployee("E3", "Sam Lee");

        LeaveRequest johnRequest = leaveManager.submit(john, LocalDate.parse("2024-10-10"), LocalDate.parse("2024-10-12"));
        leaveManager.approve(manager, johnRequest);

        leaveManager.submit(jane, LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-05"));

        leaveManager.review(manager, johnRequest, LeaveRequest.PENDING);

        try {
            leaveManager.submit(sam, LocalDate.parse("2024-12-01"), LocalDate.parse("2024-12-10"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Pending requests: " + leaveManager.getPendingRequests().size());
    }
}

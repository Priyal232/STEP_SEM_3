public class HRParkingCapstone {

    static class Employee {
        private String empId;
        private String empName;
        private double salary;

        Employee(String empId, String empName, double salary) {
            this.empId = empId;
            this.empName = empName;
            this.salary = salary;
        }

        double getSalary() {
            return salary;
        }
    }

    static class ManagerEmployee extends Employee {
        private double teamBonus;

        ManagerEmployee(String empId, String empName, double salary, double teamBonus) {
            super(empId, empName, salary);
            this.teamBonus = teamBonus;
        }

        double effectiveSalary() {
            return getSalary() + teamBonus;
        }
    }

    static class ParkingSlot {
        String slotNo;
        int capacity;
        int occupiedCount;

        ParkingSlot(String slotNo, int capacity, int occupiedCount) {
            this.slotNo = slotNo;
            this.capacity = capacity;
            this.occupiedCount = occupiedCount;
        }

        void allot(String vehicleNo) {
            occupiedCount++;
        }

        static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
            for (int i = 0; i < slots.length; i++) {
                if (slots[i].occupiedCount < slots[i].capacity) {
                    return slots[i];
                }
            }
            return null;
        }
    }

    static class CompanyEmployeeRecord {
        String name;
        String empId;
        Employee employee;
        ParkingSlot slot;
        static int totalRecords = 0;

        CompanyEmployeeRecord(String name, String empId, Employee employee, ParkingSlot slot) {
            this.name = name;
            this.empId = empId;
            this.employee = employee;
            this.slot = slot;
            totalRecords++;
        }

        String fullProfile() {
            double pay = (employee instanceof ManagerEmployee) ? ((ManagerEmployee) employee).effectiveSalary() : employee.getSalary();
            String slotText = (slot == null) ? "no parking assigned" : slot.slotNo;
            return name + " | Pay: Rs " + pay + " | Slot: " + slotText;
        }
    }

    public static void main(String[] args) {
        Employee empDivya = new ManagerEmployee("E1", "Divya", 70000, 8000);
        Employee empKaran = new Employee("E2", "Karan", 40000);
        Employee empMeera = new Employee("E3", "Meera", 10000);
        ParkingSlot[] slots = {new ParkingSlot("A1", 1, 0), new ParkingSlot("A2", 1, 0)};

        CompanyEmployeeRecord r1 = new CompanyEmployeeRecord("Divya", "E1", empDivya, slots[0]);
        slots[0].allot("v1");
        CompanyEmployeeRecord r2 = new CompanyEmployeeRecord("Karan", "E2", empKaran, slots[1]);
        slots[1].allot("v2");
        CompanyEmployeeRecord r3 = new CompanyEmployeeRecord("Meera", "E3", empMeera, null);

        System.out.println(r1.fullProfile());
        System.out.println(r2.fullProfile());
        System.out.println(r3.fullProfile());
        System.out.println("Total records: " + CompanyEmployeeRecord.totalRecords);
    }
}

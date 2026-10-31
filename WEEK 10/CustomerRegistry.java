import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CustomerRegistry {

    static class Customer {
        private final int id;
        private final String name;

        Customer(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Customer)) {
                return false;
            }
            Customer other = (Customer) o;
            return id == other.id && Objects.equals(name, other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }

        @Override
        public String toString() {
            return "(" + id + ", " + name + ")";
        }
    }

    private final Set<Customer> customers = new HashSet<>();

    boolean add(Customer c) {
        return customers.add(c);
    }

    boolean contains(Customer c) {
        return customers.contains(c);
    }

    int uniqueCount() {
        return customers.size();
    }

    public static void main(String[] args) {
        CustomerRegistry registry = new CustomerRegistry();
        System.out.println(registry.add(new Customer(101, "Asha")));
        boolean second = registry.add(new Customer(101, "Asha"));
        System.out.println(second ? "true" : "false (duplicate rejected)");
        System.out.println(registry.add(new Customer(102, "Ravi")));
        System.out.println("unique count " + registry.uniqueCount());
        System.out.println("contains: " + registry.contains(new Customer(101, "Asha")));
    }
}

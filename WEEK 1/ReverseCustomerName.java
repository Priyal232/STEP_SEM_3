public class ReverseCustomerName {

    static String reverseCustomerName(String customerName) {
        StringBuilder sb = new StringBuilder(customerName);
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        String name = "Sunil";
        System.out.println("Original Name: " + name);
        System.out.println("Reversed Name: " + reverseCustomerName(name));
    }
}

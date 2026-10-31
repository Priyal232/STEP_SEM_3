public class AddTwoNumbersLinkedList {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node addNumbers(Node a, Node b) {
        Node dummy = new Node(0), tail = dummy;
        int carry = 0;
        while (a != null || b != null || carry != 0) {
            int sum = carry;
            if (a != null) {
                sum += a.data;
                a = a.next;
            }
            if (b != null) {
                sum += b.data;
                b = b.next;
            }
            carry = sum / 10;
            tail.next = new Node(sum % 10);
            tail = tail.next;
        }
        return dummy.next;
    }

    static Node build(int... values) {
        Node dummy = new Node(0), tail = dummy;
        for (int v : values) {
            tail.next = new Node(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    static String print(Node head) {
        StringBuilder sb = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data);
            if (cur.next != null) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(print(addNumbers(build(2, 4, 3), build(5, 6, 4))));
        System.out.println(print(addNumbers(build(9, 9), build(1))));
    }
}

public class MergingTwoTicketCounters {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node mergeSorted(Node a, Node b) {
        Node dummy = new Node(0), tail = dummy;
        while (a != null && b != null) {
            if (a.data <= b.data) {
                tail.next = a;
                a = a.next;
            } else {
                tail.next = b;
                b = b.next;
            }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;
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
        if (head == null) {
            return "(empty)";
        }
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
        System.out.println(print(mergeSorted(build(3, 8, 15), build(5, 8, 12, 20))));
        System.out.println(print(mergeSorted(build(), build(4))));
    }
}

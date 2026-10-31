public class CancellingTheKthLastOrder {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node removeKthFromEnd(Node head, int k) {
        Node dummy = new Node(0);
        dummy.next = head;
        Node lead = dummy, trail = dummy;
        for (int i = 0; i <= k; i++) {
            lead = lead.next;
        }
        while (lead != null) {
            lead = lead.next;
            trail = trail.next;
        }
        trail.next = trail.next.next;
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
        System.out.println(print(removeKthFromEnd(build(10, 20, 30, 40, 50), 2)));
        System.out.println(print(removeKthFromEnd(build(10, 20, 30, 40, 50), 5)));
    }
}

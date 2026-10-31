public class RemoveNthNodeFromEnd {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node removeNthFromEnd(Node head, int n) {
        Node dummy = new Node(0);
        dummy.next = head;
        Node fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
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
            return "(empty list)";
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
        System.out.println(print(removeNthFromEnd(build(1, 2, 3, 4, 5), 2)));
        System.out.println(print(removeNthFromEnd(build(1), 1)));
    }
}

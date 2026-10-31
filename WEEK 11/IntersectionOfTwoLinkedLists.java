public class IntersectionOfTwoLinkedLists {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static int length(Node head) {
        int len = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            len++;
        }
        return len;
    }

    static Node findIntersection(Node a, Node b) {
        int lenA = length(a), lenB = length(b);
        while (lenA > lenB) {
            a = a.next;
            lenA--;
        }
        while (lenB > lenA) {
            b = b.next;
            lenB--;
        }
        while (a != b) {
            a = a.next;
            b = b.next;
        }
        return a;
    }

    static Node build(int... values) {
        Node dummy = new Node(0), tail = dummy;
        for (int v : values) {
            tail.next = new Node(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    static Node tailOf(Node head) {
        while (head.next != null) {
            head = head.next;
        }
        return head;
    }

    public static void main(String[] args) {
        Node shared = build(8, 4, 5);
        Node a = build(4, 1);
        tailOf(a).next = shared;
        Node b = build(5, 6, 1);
        tailOf(b).next = shared;
        Node r1 = findIntersection(a, b);
        System.out.println(r1 == null ? "null" : "Node with value " + r1.data);

        Node r2 = findIntersection(build(2, 6, 4), build(1, 5));
        System.out.println(r2 == null ? "null" : "Node with value " + r2.data);
    }
}

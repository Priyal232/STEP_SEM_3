public class LinkedListPalindromeCheck {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node reverse(Node head) {
        Node prev = null, cur = head;
        while (cur != null) {
            Node next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    static boolean isPalindrome(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node secondHead = reverse(slow.next);
        Node p1 = head, p2 = secondHead;
        boolean result = true;
        while (p2 != null) {
            if (p1.data != p2.data) {
                result = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        slow.next = reverse(secondHead);
        return result;
    }

    static Node build(int... values) {
        Node dummy = new Node(0), tail = dummy;
        for (int v : values) {
            tail.next = new Node(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome(build(1, 2, 2, 1)));
        System.out.println(isPalindrome(build(1, 2, 3)));
    }
}

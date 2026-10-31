public class PolynomialCalculator {

    static class Node {
        int coeff;
        int exp;
        Node next;

        Node(int coeff, int exp) {
            this.coeff = coeff;
            this.exp = exp;
        }
    }

    static Node addPolynomials(Node p, Node q) {
        Node dummy = new Node(0, 0), tail = dummy;
        while (p != null && q != null) {
            if (p.exp > q.exp) {
                tail.next = new Node(p.coeff, p.exp);
                tail = tail.next;
                p = p.next;
            } else if (q.exp > p.exp) {
                tail.next = new Node(q.coeff, q.exp);
                tail = tail.next;
                q = q.next;
            } else {
                int sum = p.coeff + q.coeff;
                if (sum != 0) {
                    tail.next = new Node(sum, p.exp);
                    tail = tail.next;
                }
                p = p.next;
                q = q.next;
            }
        }
        Node rest = (p != null) ? p : q;
        while (rest != null) {
            tail.next = new Node(rest.coeff, rest.exp);
            tail = tail.next;
            rest = rest.next;
        }
        return dummy.next;
    }

    static String toStandardForm(Node head) {
        if (head == null) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Node cur = head; cur != null; cur = cur.next) {
            int c = cur.coeff;
            if (first) {
                if (c < 0) {
                    sb.append("-");
                }
            } else {
                sb.append(c < 0 ? " - " : " + ");
            }
            int abs = Math.abs(c);
            if (cur.exp == 0) {
                sb.append(abs);
            } else {
                if (abs != 1) {
                    sb.append(abs);
                }
                sb.append("x");
                if (cur.exp > 1) {
                    sb.append("^").append(cur.exp);
                }
            }
            first = false;
        }
        return sb.toString();
    }

    static Node build(int[][] terms) {
        Node dummy = new Node(0, 0), tail = dummy;
        for (int[] t : terms) {
            tail.next = new Node(t[0], t[1]);
            tail = tail.next;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node p1 = build(new int[][]{{5, 3}, {4, 2}, {2, 0}});
        Node q1 = build(new int[][]{{5, 2}, {-3, 1}, {1, 0}});
        System.out.println(toStandardForm(addPolynomials(p1, q1)));

        Node p2 = build(new int[][]{{3, 2}, {1, 0}});
        Node q2 = build(new int[][]{{-3, 2}, {4, 1}});
        System.out.println(toStandardForm(addPolynomials(p2, q2)));
    }
}

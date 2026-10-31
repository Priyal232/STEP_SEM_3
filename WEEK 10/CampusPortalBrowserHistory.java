public class CampusPortalBrowserHistory {

    static class Node {
        String url;
        Node prev, next;

        Node(String url) {
            this.url = url;
        }
    }

    static class BrowserHistory {
        private Node current;

        BrowserHistory(String homepage) {
            current = new Node(homepage);
        }

        void visit(String url) {
            Node node = new Node(url);
            if (current.next != null) {
                current.next.prev = null;
            }
            current.next = node;
            node.prev = current;
            current = node;
        }

        String back(int steps) {
            while (steps > 0 && current.prev != null) {
                current = current.prev;
                steps--;
            }
            return current.url;
        }

        String forward(int steps) {
            while (steps > 0 && current.next != null) {
                current = current.next;
                steps--;
            }
            return current.url;
        }
    }

    public static void main(String[] args) {
        BrowserHistory history = new BrowserHistory("srm.edu");
        history.visit("a.com");
        history.visit("b.com");
        history.visit("c.com");
        StringBuilder out = new StringBuilder();
        out.append(history.back(1)).append(", ");
        out.append(history.back(1)).append(", ");
        out.append(history.forward(1)).append(", ");
        history.visit("d.com");
        out.append(history.forward(2)).append(", ");
        out.append(history.back(2)).append(", ");
        out.append(history.back(7));
        System.out.println(out);
    }
}

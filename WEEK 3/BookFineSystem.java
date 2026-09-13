public class BookFineSystem {

    static class BookIssue {
        String title;
        String borrowerName;
        int daysOverdue;

        BookIssue(String title, String borrowerName, int daysOverdue) {
            this.title = title;
            this.borrowerName = borrowerName;
            this.daysOverdue = daysOverdue;
        }

        double fineAmount() {
            if (daysOverdue > 0) {
                return daysOverdue * 5;
            }
            return 0;
        }

        boolean isSeverelyOverdue() {
            return daysOverdue > 14;
        }

        static double totalFineCollected(BookIssue[] issues) {
            double total = 0;
            for (int i = 0; i < issues.length; i++) {
                total += issues[i].fineAmount();
            }
            return total;
        }
    }

    public static void main(String[] args) {
        BookIssue[] issues = {
                new BookIssue("Clean Code", "A", 18),
                new BookIssue("Effective Java", "B", 5),
                new BookIssue("Refactoring", "C", 0),
                new BookIssue("DSA Handbook", "D", 21),
                new BookIssue("Design Patterns", "E", 9)
        };
        for (int i = 0; i < issues.length; i++) {
            String status = issues[i].isSeverelyOverdue() ? "Severely overdue" : "OK";
            System.out.println(issues[i].title + " - " + issues[i].daysOverdue + " days - " + status);
        }
        System.out.println("Total fine collected: Rs " + BookIssue.totalFineCollected(issues));
    }
}

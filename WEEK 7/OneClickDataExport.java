public class OneClickDataExport {
    interface Exportable {
        String exportData();
    }

    static class ExportTracker {
        private static int totalExports = 0;

        private ExportTracker() {
        }

        static void recordExport() {
            totalExports++;
        }

        static int getTotalExports() {
            return totalExports;
        }
    }

    static class ReportGenerator implements Exportable {
        private final String reportName;

        public ReportGenerator(String reportName) {
            this.reportName = reportName;
        }

        @Override
        public String exportData() {
            ExportTracker.recordExport();
            return "Exported report: " + reportName;
        }
    }

    static class UserProfile implements Exportable {
        private final String username;

        public UserProfile(String username) {
            this.username = username;
        }

        @Override
        public String exportData() {
            ExportTracker.recordExport();
            return "Exported profile: " + username;
        }
    }

    static int getTotalExports() {
        return ExportTracker.getTotalExports();
    }

    static void exportAll(Exportable[] items) {
        for (Exportable item : items) {
            System.out.println(item.exportData());
        }
    }

    public static void main(String[] args) {
        ReportGenerator r = new ReportGenerator("Sales Q1");
        UserProfile u = new UserProfile("jane_doe");

        Exportable ref = r;
        exportAll(new Exportable[]{ ref, u });
        System.out.println(getTotalExports());
    }
}

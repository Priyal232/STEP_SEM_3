import java.util.*;

public class MembershipFieldReachChecker {

    static class AccessChecker {
        static String classifyAccess(String fieldModifier, String accessorContext) {
            if (fieldModifier.equals("public")) return "ALLOWED";
            if (fieldModifier.equals("private")) return accessorContext.equals("SAME_CLASS") ? "ALLOWED" : "DENIED";
            if (fieldModifier.equals("default")) {
                return (accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE")) ? "ALLOWED" : "DENIED";
            }
            if (fieldModifier.equals("protected")) {
                return (accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE")) ? "ALLOWED" : "DENIED";
            }
            return "DENIED";
        }

        static String summarizeByModifier(String[][] attempts) {
            LinkedHashMap<String, int[]> counts = new LinkedHashMap<>();
            counts.put("private", new int[]{0, 0});
            counts.put("default", new int[]{0, 0});
            counts.put("protected", new int[]{0, 0});
            counts.put("public", new int[]{0, 0});
            for (int i = 0; i < attempts.length; i++) {
                String modifier = attempts[i][0];
                String result = classifyAccess(modifier, attempts[i][1]);
                int[] pair = counts.get(modifier);
                if (result.equals("ALLOWED")) pair[0]++;
                else pair[1]++;
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, int[]> entry : counts.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()[0]).append(" allowed / ")
                        .append(entry.getValue()[1]).append(" denied");
                if (i != counts.size() - 1) sb.append(" | ");
                i++;
            }
            return sb.toString();
        }
    }

    static class LibraryMember {
        private String membershipId;
        String branchCode;
        protected double finesOwed;
        public String displayName;

        public LibraryMember(String membershipId, String branchCode, double finesOwed, String displayName) {
            String trimmed = (membershipId == null) ? "" : membershipId.trim();
            if (trimmed.isEmpty() || trimmed.length() < 4) {
                throw new IllegalArgumentException("Invalid membershipId");
            }
            this.membershipId = membershipId;
            this.branchCode = branchCode;
            this.finesOwed = finesOwed;
            this.displayName = displayName;
        }
    }

    public static void main(String[] args) {
        System.out.println(AccessChecker.classifyAccess("private", "SAME_CLASS"));
        System.out.println(AccessChecker.classifyAccess("protected", "DIFFERENT_PACKAGE"));
        System.out.println(AccessChecker.summarizeByModifier(new String[][]{
                {"private", "SAME_CLASS"}, {"private", "SAME_PACKAGE"},
                {"default", "SAME_PACKAGE"}, {"default", "DIFFERENT_PACKAGE"},
                {"protected", "SAME_PACKAGE"}, {"protected", "SAME_CLASS"},
                {"public", "DIFFERENT_PACKAGE"}
        }));
        try {
            new LibraryMember("LB9", "BR1", 0, "Priya Nair");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
    }
}

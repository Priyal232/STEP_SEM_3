public class FieldVisibilityIntakeValidator {

    static class AccessRuleEngine {
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

        static String summarizeBatch(String[][] attempts) {
            int allowed = 0, denied = 0;
            for (int i = 0; i < attempts.length; i++) {
                if (classifyAccess(attempts[i][0], attempts[i][1]).equals("ALLOWED")) allowed++;
                else denied++;
            }
            return "Allowed: " + allowed + " | Denied: " + denied;
        }
    }

    static class PatientRecord {
        private String patientId;
        String wardCode;
        protected double vitalsScore;
        public String facilityName;

        public PatientRecord(String patientId, String wardCode, double vitalsScore, String facilityName) {
            String trimmed = (patientId == null) ? "" : patientId.trim();
            if (trimmed.isEmpty() || trimmed.length() < 4) {
                throw new IllegalArgumentException("Invalid patientId");
            }
            this.patientId = patientId;
            this.wardCode = wardCode;
            this.vitalsScore = vitalsScore;
            this.facilityName = facilityName;
        }
    }

    public static void main(String[] args) {
        System.out.println(AccessRuleEngine.classifyAccess("private", "SAME_CLASS"));
        System.out.println(AccessRuleEngine.classifyAccess("default", "DIFFERENT_PACKAGE"));
        System.out.println(AccessRuleEngine.summarizeBatch(new String[][]{
                {"protected", "SAME_PACKAGE"}, {"protected", "DIFFERENT_PACKAGE"}, {"public", "DIFFERENT_PACKAGE"}
        }));
        try {
            new PatientRecord("MT9", "W3", 98.2, "MediTrack Central");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
    }
}

public class ReferenceDeskSubclassReach {

    static class AccessChecker {
        static String classifyAccess(String fieldModifier, String accessorContext) {
            if (fieldModifier.equals("public")) return "ALLOWED";
            if (fieldModifier.equals("private")) return accessorContext.equals("SAME_CLASS") ? "ALLOWED" : "DENIED";
            if (fieldModifier.equals("default")) {
                return (accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE")) ? "ALLOWED" : "DENIED";
            }
            if (fieldModifier.equals("protected")) {
                if (accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE")
                        || accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE")) {
                    return "ALLOWED";
                }
                return "DENIED";
            }
            return "DENIED";
        }

        static String describeContext(String accessorContext) {
            String[] parts = accessorContext.split("_");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                String word = parts[i].toLowerCase();
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
                if (i != parts.length - 1) sb.append(" ");
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(AccessChecker.classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));
        System.out.println(AccessChecker.classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"));
        System.out.println(AccessChecker.describeContext("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));
    }
}

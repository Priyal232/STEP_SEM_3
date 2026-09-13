public class LibraryMemberJavaBean {

    static class LibraryMember {
        private String membershipId;
        private String name;
        private boolean premiumMember;
        private String securityAnswerHash;
        private boolean membershipIdSet;

        public LibraryMember() {
            this.membershipIdSet = false;
        }

        public LibraryMember(String name) {
            this();
            this.name = name;
        }

        public LibraryMember(String membershipId, String name) {
            this(name);
            this.membershipId = membershipId;
            this.membershipIdSet = true;
        }

        String getMembershipId() {
            return membershipId;
        }

        void setMembershipId(String id) {
            if (!membershipIdSet) {
                this.membershipId = id;
                this.membershipIdSet = true;
            }
        }

        boolean isPremiumMember() {
            return premiumMember;
        }

        void setPremiumMember(boolean premium) {
            this.premiumMember = premium;
        }

        void setSecurityAnswer(String answer) {
            this.securityAnswerHash = String.valueOf(answer.hashCode());
        }
    }

    public static void main(String[] args) {
        System.out.println(new LibraryMember("Priya Nair").getMembershipId());
        System.out.println(new LibraryMember("LIB-8841", "Priya Nair").getMembershipId());
        LibraryMember m = new LibraryMember();
        m.setMembershipId("LIB-8841");
        m.setMembershipId("FAKE-0000");
        System.out.println(m.getMembershipId());
    }
}

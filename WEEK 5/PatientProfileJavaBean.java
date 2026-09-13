public class PatientProfileJavaBean {

    static class PatientProfile {
        private String patientId;
        private String name;
        private boolean discharged;
        private String lockerPinHash;
        private boolean patientIdSet;

        public PatientProfile() {
            this.patientIdSet = false;
        }

        public PatientProfile(String name) {
            this();
            this.name = name;
        }

        public PatientProfile(String patientId, String name) {
            this(name);
            this.patientId = patientId;
            this.patientIdSet = true;
        }

        String getPatientId() {
            return patientId;
        }

        void setPatientId(String id) {
            if (!patientIdSet) {
                this.patientId = id;
                this.patientIdSet = true;
            }
        }

        boolean isDischarged() {
            return discharged;
        }

        void setDischarged(boolean discharged) {
            this.discharged = discharged;
        }

        void setLockerPin(String pin) {
            this.lockerPinHash = String.valueOf(pin.hashCode());
        }
    }

    public static void main(String[] args) {
        System.out.println(new PatientProfile("Arjun Iyer").getPatientId());
        System.out.println(new PatientProfile("MT2026-0142", "Arjun Iyer").getPatientId());
        PatientProfile p = new PatientProfile();
        p.setPatientId("MT2026-0142");
        p.setPatientId("HACKED-0000");
        System.out.println(p.getPatientId());
    }
}

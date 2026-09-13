import java.util.*;

public class ImmutableDischargeSummary {

    static class DischargeSummary {
        private final String patientId;
        private final String[] medicationCodes;
        private static String requiredPrefix;

        static {
            requiredPrefix = "MED-";
        }

        public DischargeSummary(String patientId, String[] medicationCodes) {
            for (int i = 0; i < medicationCodes.length; i++) {
                if (!isValidCode(medicationCodes[i])) {
                    throw new IllegalArgumentException("Invalid medication code: " + medicationCodes[i]);
                }
            }
            this.patientId = patientId;
            this.medicationCodes = Arrays.copyOf(medicationCodes, medicationCodes.length);
        }

        private static boolean isValidCode(String code) {
            if (code == null || code.length() != 5) return false;
            if (!code.startsWith(requiredPrefix)) return false;
            return Character.isUpperCase(code.charAt(4));
        }

        String[] getMedicationCodes() {
            return Arrays.copyOf(medicationCodes, medicationCodes.length);
        }

        DischargeSummary withCorrectedMedication(int index, String newCode) {
            String[] updated = Arrays.copyOf(medicationCodes, medicationCodes.length);
            updated[index] = newCode;
            return new DischargeSummary(this.patientId, updated);
        }

        static String processNightlyBatch(DischargeSummary[] summaries) {
            int processed = 0, nulls = 0, critical = 0, routine = 0;
            for (int i = 0; i < summaries.length; i++) {
                DischargeSummary s = summaries[i];
                if (s == null) {
                    nulls++;
                    continue;
                }
                if (s instanceof CriticalCareDischargeSummary) critical++;
                else routine++;
                processed++;
            }
            return processed + " processed | " + nulls + " null skipped | " + critical + " critical-care | " + routine + " routine";
        }
    }

    static class CriticalCareDischargeSummary extends DischargeSummary {
        private int icuDays;

        public CriticalCareDischargeSummary(String patientId, String[] medicationCodes, int icuDays) {
            super(patientId, medicationCodes);
            this.icuDays = icuDays;
        }
    }

    public static void main(String[] args) {
        try {
            new DischargeSummary("MT2026-0142", new String[]{"MED-A", "bad"});
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        DischargeSummary d = new DischargeSummary("MT2026-0142", new String[]{"MED-A", "MED-B"});
        String[] codes = d.getMedicationCodes();
        codes[0] = "TAMPERED";
        System.out.println(d.getMedicationCodes()[0]);

        DischargeSummary[] batch = {
                new CriticalCareDischargeSummary("MT001", new String[]{"MED-X"}, 4),
                null,
                new DischargeSummary("MT002", new String[]{"MED-Y"})
        };
        System.out.println(DischargeSummary.processNightlyBatch(batch));
    }
}

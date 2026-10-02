public class FleetMaintenanceTracker {
    static abstract class ServiceableVehicle {
        private double mileage;

        public ServiceableVehicle() {
            this.mileage = 0.0;
        }

        public abstract String performMaintenance();

        public double getMileage() {
            return mileage;
        }

        public void addMileage(double km) {
            if (km < 0) {
                System.out.println("Rejected: distance cannot be negative. Mileage unchanged at " + mileage);
                return;
            }
            mileage += km;
        }
    }

    interface Insurable {
        String getInsuranceInfo();
    }

    static class Forklift extends ServiceableVehicle implements Insurable {
        private final String assetTag;

        public Forklift(String assetTag) {
            super();
            this.assetTag = assetTag;
        }

        public String getAssetTag() {
            return assetTag;
        }

        @Override
        public String performMaintenance() {
            return "Forklift " + assetTag + ": hydraulic and fork inspection complete";
        }

        @Override
        public String getInsuranceInfo() {
            return "Insured under fleet policy - Asset " + assetTag;
        }
    }

    static class HeavyDutyForklift extends Forklift {
        public HeavyDutyForklift(String assetTag) {
            super(assetTag);
        }

        @Override
        public String performMaintenance() {
            return super.performMaintenance() + " | high-pressure hydraulic check complete";
        }
    }

    static class Compactor extends ServiceableVehicle {
        private final String assetTag;

        public Compactor(String assetTag) {
            super();
            this.assetTag = assetTag;
        }

        @Override
        public String performMaintenance() {
            return "Compactor " + assetTag + ": drum and vibration check complete";
        }
    }

    static String getInsuranceIfApplicable(ServiceableVehicle v) {
        if (v instanceof Insurable) {
            Insurable insurable = (Insurable) v;
            return insurable.getInsuranceInfo();
        }
        return "No insurance record exists";
    }

    public static void main(String[] args) {
        Forklift f = new Forklift("FL-22");
        f.addMileage(120);
        System.out.println(f.getMileage());
        f.addMileage(-30);

        System.out.println(f.performMaintenance());

        HeavyDutyForklift hd = new HeavyDutyForklift("HD-9");
        System.out.println(hd.performMaintenance());

        System.out.println(getInsuranceIfApplicable(f));
        System.out.println(getInsuranceIfApplicable(hd));
        System.out.println(getInsuranceIfApplicable(new Compactor("CP-3")));
    }
}

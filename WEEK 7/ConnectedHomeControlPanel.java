public class ConnectedHomeControlPanel {
    static abstract class HomeDevice {
        private static int serialCounter = 1000;
        private final String serialNumber;

        public HomeDevice() {
            serialCounter++;
            this.serialNumber = "HD-" + serialCounter;
        }

        public abstract String activate();

        public String getSerialNumber() {
            return serialNumber;
        }
    }

    interface RemoteControllable {
        String connect(String appId);
    }

    interface EnergyTrackable {
        double getConsumptionWatts();
    }

    static class WashingMachine extends HomeDevice implements RemoteControllable, EnergyTrackable {
        private final double consumptionWatts;

        public WashingMachine(double consumptionWatts) {
            super();
            this.consumptionWatts = consumptionWatts;
        }

        @Override
        public String activate() {
            return "Washing machine " + getSerialNumber() + " started a cycle";
        }

        @Override
        public String connect(String appId) {
            return getSerialNumber() + " connected to " + appId;
        }

        @Override
        public double getConsumptionWatts() {
            return consumptionWatts;
        }
    }

    static class Refrigerator extends HomeDevice implements EnergyTrackable {
        private final double consumptionWatts;

        public Refrigerator(double consumptionWatts) {
            super();
            this.consumptionWatts = consumptionWatts;
        }

        @Override
        public String activate() {
            return "Refrigerator " + getSerialNumber() + " started cooling";
        }

        @Override
        public double getConsumptionWatts() {
            return consumptionWatts;
        }
    }

    static class MobileApp implements RemoteControllable {
        private final String appName;

        public MobileApp(String appName) {
            this.appName = appName;
        }

        @Override
        public String connect(String appId) {
            return appName + " connected to " + appId;
        }
    }

    static void connectAll(RemoteControllable[] items, String appId) {
        for (RemoteControllable item : items) {
            System.out.println(item.connect(appId));
        }
    }

    static double getConsumptionIfTrackable(HomeDevice d) {
        if (d instanceof EnergyTrackable) {
            EnergyTrackable trackable = (EnergyTrackable) d;
            return trackable.getConsumptionWatts();
        }
        System.out.println(d.getSerialNumber() + " does not track energy");
        return 0.0;
    }

    public static void main(String[] args) {
        WashingMachine wm = new WashingMachine(500.0);
        System.out.println(wm.activate());
        System.out.println(wm.connect("HomeConnect"));

        Refrigerator fridge = new Refrigerator(150.0);
        System.out.println(getConsumptionIfTrackable(fridge));

        MobileApp app = new MobileApp("HomeConnect App");
        System.out.println(app.connect("HomeConnect"));

        HomeDevice ref = wm;
        System.out.println(getConsumptionIfTrackable(ref));

        System.out.println("--- Connect all ---");
        connectAll(new RemoteControllable[]{ wm, app }, "HomeConnect");
    }
}

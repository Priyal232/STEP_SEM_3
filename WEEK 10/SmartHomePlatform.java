import java.util.LinkedHashMap;
import java.util.Map;

public class SmartHomePlatform {

    interface Dimmable {
        void dim(int level);
    }

    interface Schedulable {
        void schedule(String time);
    }

    interface EnergyMonitor {
        int energyUsage();
    }

    static abstract class Device {
        private final String id;
        private boolean on;

        Device(String id) {
            this.id = id;
        }

        String getId() {
            return id;
        }

        void turnOn() {
            on = true;
        }

        void turnOff() {
            on = false;
        }

        boolean isOn() {
            return on;
        }
    }

    static class Light extends Device implements Dimmable, Schedulable {
        private int level;
        private String scheduledAt;

        Light(String id) {
            super(id);
        }

        public void dim(int level) {
            this.level = level;
        }

        public void schedule(String time) {
            this.scheduledAt = time;
        }
    }

    static class Fan extends Device implements Schedulable {
        private String scheduledAt;

        Fan(String id) {
            super(id);
        }

        public void schedule(String time) {
            this.scheduledAt = time;
        }
    }

    static class Plug extends Device implements EnergyMonitor {
        private final int energy;

        Plug(String id, int energy) {
            super(id);
            this.energy = energy;
        }

        public int energyUsage() {
            return energy;
        }
    }

    private final Map<String, Device> devices = new LinkedHashMap<>();

    void addDevice(Device d) {
        devices.put(d.getId(), d);
    }

    String process(String command) {
        String[] parts = command.trim().split("\\s+");
        String action = parts[0].toUpperCase();
        String id = parts[1];
        Device d = devices.get(id);
        if (d == null) {
            return id + " rejected: unknown device";
        }
        switch (action) {
            case "ON":
                d.turnOn();
                return id + " is ON";
            case "OFF":
                d.turnOff();
                return id + " is OFF";
            case "DIM":
                if (d instanceof Dimmable dm) {
                    int level = Integer.parseInt(parts[2]);
                    dm.dim(level);
                    return id + " dimmed to " + level;
                }
                return id + " rejected: DIM unsupported";
            case "SCHEDULE":
                if (d instanceof Schedulable s) {
                    s.schedule(parts[2]);
                    return id + " scheduled " + parts[2];
                }
                return id + " rejected: SCHEDULE unsupported";
            case "ENERGY":
                if (d instanceof EnergyMonitor e) {
                    return id + " energy " + e.energyUsage() + " kWh";
                }
                return id + " rejected: ENERGY unsupported";
            default:
                return id + " rejected: " + action + " unsupported";
        }
    }

    public static void main(String[] args) {
        SmartHomePlatform home = new SmartHomePlatform();
        home.addDevice(new Light("L1"));
        home.addDevice(new Fan("F1"));
        home.addDevice(new Plug("P1", 12));
        String[] commands = {
            "ON L1",
            "DIM L1 40",
            "DIM F1 30",
            "SCHEDULE F1 22:00",
            "ENERGY P1",
            "ENERGY L1"
        };
        for (String c : commands) {
            System.out.println(home.process(c));
        }
    }
}

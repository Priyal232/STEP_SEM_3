import java.util.ArrayList;
import java.util.List;

public class TheSmartLabControlPanel {
    interface Capability {
        String getName();
    }

    static class PowerCapability implements Capability {
        private boolean on;

        @Override
        public String getName() {
            return "Power";
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public boolean isOn() {
            return on;
        }
    }

    static class BrightnessCapability implements Capability {
        public static final int MIN = 0;
        public static final int MAX = 100;
        private int level = MAX;

        @Override
        public String getName() {
            return "Brightness";
        }

        public void setLevel(int level) {
            if (level < MIN || level > MAX) {
                throw new IllegalArgumentException("brightness must be between " + MIN + "% and " + MAX + "%");
            }
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    static class TemperatureCapability implements Capability {
        public static final int MIN = 16;
        public static final int MAX = 30;
        private int temperature = 24;

        @Override
        public String getName() {
            return "Temperature";
        }

        public void setTemperature(int temperature) {
            if (temperature < MIN || temperature > MAX) {
                throw new IllegalArgumentException("temperature must be between " + MIN + "°C and " + MAX + "°C");
            }
            this.temperature = temperature;
        }

        public int getTemperature() {
            return temperature;
        }
    }

    static class Device {
        private final String name;
        private final List<Capability> capabilities = new ArrayList<>();

        public Device(String name) {
            this.name = name;
        }

        public void addCapability(Capability capability) {
            if (hasCapability(capability.getName())) {
                throw new IllegalArgumentException(name + " already has " + capability.getName() + " capability.");
            }
            capabilities.add(capability);
        }

        public boolean hasCapability(String capabilityName) {
            return getCapability(capabilityName) != null;
        }

        public Capability getCapability(String capabilityName) {
            for (Capability capability : capabilities) {
                if (capability.getName().equals(capabilityName)) {
                    return capability;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }
    }

    static abstract class SceneStep {
        public abstract String getCapabilityName();

        protected abstract String execute(String deviceName, Capability capability);

        public boolean appliesTo(Device device) {
            return device.hasCapability(getCapabilityName());
        }

        public String applyTo(Device device) {
            Capability capability = device.getCapability(getCapabilityName());
            if (capability == null) {
                throw new IllegalArgumentException("does not support " + getCapabilityName());
            }
            return execute(device.getName(), capability);
        }
    }

    static class PowerStep extends SceneStep {
        private final boolean turnOn;

        public PowerStep(boolean turnOn) {
            this.turnOn = turnOn;
        }

        @Override
        public String getCapabilityName() {
            return "Power";
        }

        @Override
        protected String execute(String deviceName, Capability capability) {
            PowerCapability power = (PowerCapability) capability;
            power.setOn(turnOn);
            return deviceName + ": " + (turnOn ? "ON" : "OFF");
        }
    }

    static class BrightnessStep extends SceneStep {
        private final int level;

        public BrightnessStep(int level) {
            this.level = level;
        }

        @Override
        public String getCapabilityName() {
            return "Brightness";
        }

        @Override
        protected String execute(String deviceName, Capability capability) {
            BrightnessCapability brightness = (BrightnessCapability) capability;
            brightness.setLevel(level);
            return deviceName + ": brightness set to " + level + "%";
        }
    }

    static class TemperatureStep extends SceneStep {
        private final int temperature;

        public TemperatureStep(int temperature) {
            this.temperature = temperature;
        }

        @Override
        public String getCapabilityName() {
            return "Temperature";
        }

        @Override
        protected String execute(String deviceName, Capability capability) {
            TemperatureCapability thermostat = (TemperatureCapability) capability;
            thermostat.setTemperature(temperature);
            return deviceName + ": temperature set to " + temperature + "°C";
        }
    }

    static class Scene {
        private final String name;
        private final List<SceneStep> steps = new ArrayList<>();

        public Scene(String name) {
            this.name = name;
        }

        public void addStep(SceneStep step) {
            steps.add(step);
        }

        public String getName() {
            return name;
        }

        public List<SceneStep> getSteps() {
            return new ArrayList<>(steps);
        }
    }

    static class LabControlPanel {
        private final List<Device> devices = new ArrayList<>();

        public void registerDevice(Device device) {
            devices.add(device);
        }

        public int runScene(Scene scene) {
            System.out.println("Scene '" + scene.getName() + "' started.");
            int applied = 0;
            for (SceneStep step : scene.getSteps()) {
                for (Device device : devices) {
                    if (step.appliesTo(device) && apply(device, step)) {
                        applied++;
                    }
                }
            }
            System.out.println("Scene '" + scene.getName() + "' completed: " + applied + " actions applied.");
            return applied;
        }

        public boolean command(Device device, SceneStep step) {
            if (!step.appliesTo(device)) {
                System.out.println("Skipped: " + device.getName() + " does not support " + step.getCapabilityName() + ".");
                return false;
            }
            return apply(device, step);
        }

        public void addCapability(Device device, Capability capability) {
            try {
                device.addCapability(capability);
                System.out.println(device.getName() + ": " + capability.getName() + " capability added.");
            } catch (IllegalArgumentException e) {
                System.out.println("Rejected: " + e.getMessage());
            }
        }

        private boolean apply(Device device, SceneStep step) {
            try {
                System.out.println(step.applyTo(device) + ".");
                return true;
            } catch (IllegalArgumentException e) {
                System.out.println("Rejected: " + device.getName() + " " + e.getMessage() + ".");
                return false;
            }
        }
    }

    public static void main(String[] args) {
        LabControlPanel panel = new LabControlPanel();

        Device labAc = new Device("Lab AC");
        labAc.addCapability(new PowerCapability());
        labAc.addCapability(new TemperatureCapability());

        Device ceilingLights = new Device("Ceiling Lights");
        ceilingLights.addCapability(new PowerCapability());
        ceilingLights.addCapability(new BrightnessCapability());

        Device projector = new Device("Projector");
        projector.addCapability(new PowerCapability());

        panel.registerDevice(labAc);
        panel.registerDevice(ceilingLights);
        panel.registerDevice(projector);

        Scene lectureMode = new Scene("Lecture Mode");
        lectureMode.addStep(new PowerStep(true));
        lectureMode.addStep(new BrightnessStep(40));
        lectureMode.addStep(new TemperatureStep(24));
        panel.runScene(lectureMode);

        panel.command(labAc, new TemperatureStep(12));

        panel.addCapability(projector, new BrightnessCapability());
        panel.command(projector, new BrightnessStep(70));
    }
}

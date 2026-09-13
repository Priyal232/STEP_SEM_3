import java.util.*;

public class VitalsMonitoringEncapsulationGuard {

    static class PatientVitals {
        private double[] readings;
        private int count;

        PatientVitals(double[] initialReadings) {
            this.readings = new double[initialReadings.length];
            this.count = 0;
            for (int i = 0; i < initialReadings.length; i++) {
                recordReading(initialReadings[i]);
            }
        }

        void recordReading(double reading) {
            if (reading <= 0 || reading > 45) return;
            readings[count] = reading;
            count++;
        }

        double getAverage() {
            double sum = 0;
            for (int i = 0; i < count; i++) sum += readings[i];
            return count == 0 ? 0 : sum / count;
        }

        double[] getAllReadings() {
            double[] copy = new double[count];
            for (int i = 0; i < count; i++) copy[i] = readings[i];
            return copy;
        }
    }

    public static void main(String[] args) {
        PatientVitals v = new PatientVitals(new double[]{36.5, -2, 37.1});
        System.out.println(Arrays.toString(v.getAllReadings()));
        double[] copy = v.getAllReadings();
        copy[0] = 999;
        System.out.println(v.getAllReadings()[0]);
    }
}

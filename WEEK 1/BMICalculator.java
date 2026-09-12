public class BMICalculator {

    static String getBmiStatus(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    static void printWellnessReport(double[] heights, double[] weights) {
        System.out.println("Person | Height (m) | Weight (kg) | BMI | Status");
        for (int i = 0; i < heights.length; i++) {
            double bmi = weights[i] / (heights[i] * heights[i]);
            String status = getBmiStatus(bmi);
            System.out.println("Person " + (i + 1) + " | " + heights[i] + " | " + weights[i] + " | "
                    + String.format("%.2f", bmi) + " | " + status);
        }
    }

    public static void main(String[] args) {
        printWellnessReport(new double[]{1.75, 1.60}, new double[]{70, 90});
    }
}

public class WarehouseInventoryBalancer {

    static void analyzeInventory(int[] sectionA, int[] sectionB) {
        int totalA = 0;
        int totalB = 0;
        for (int i = 0; i < sectionA.length; i++) totalA += sectionA[i];
        for (int i = 0; i < sectionB.length; i++) totalB += sectionB[i];
        String status = (totalA == totalB) ? "Balanced" : "Not Balanced";

        int maxVal = sectionA[0];
        String maxSection = "Section A";
        int maxIndex = 1;
        for (int i = 0; i < sectionA.length; i++) {
            if (sectionA[i] > maxVal) {
                maxVal = sectionA[i];
                maxSection = "Section A";
                maxIndex = i + 1;
            }
        }
        for (int i = 0; i < sectionB.length; i++) {
            if (sectionB[i] > maxVal) {
                maxVal = sectionB[i];
                maxSection = "Section B";
                maxIndex = i + 1;
            }
        }
        System.out.println("Section A Total: " + totalA + " | Section B Total: " + totalB + " | Status: " + status
                + " | Highest Quantity: " + maxVal + " (" + maxSection + ", Item " + maxIndex + ")");
    }

    public static void main(String[] args) {
        analyzeInventory(new int[]{20, 15, 30}, new int[]{25, 10, 30});
    }
}

public class ParkingSlotAllocation {

    static class ParkingSlot {
        String slotNo;
        int capacity;
        int occupiedCount;

        ParkingSlot(String slotNo, int capacity, int occupiedCount) {
            this.slotNo = slotNo;
            this.capacity = capacity;
            this.occupiedCount = occupiedCount;
        }

        void allot(String vehicleNo) {
            occupiedCount++;
        }

        static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
            for (int i = 0; i < slots.length; i++) {
                if (slots[i].occupiedCount < slots[i].capacity) {
                    return slots[i];
                }
            }
            return null;
        }

        static void safeAllot(ParkingSlot[] slots, String vehicleNo) {
            ParkingSlot slot = findAvailableSlot(slots);
            if (slot == null) {
                System.out.println("No slots available for " + vehicleNo);
            } else {
                slot.allot(vehicleNo);
                System.out.println(vehicleNo + " allotted to slot " + slot.slotNo);
            }
        }
    }

    public static void main(String[] args) {
        ParkingSlot[] slots = {new ParkingSlot("A1", 4, 3), new ParkingSlot("A2", 5, 5)};
        ParkingSlot.safeAllot(slots, "TN09AB1234");
        slots[0].occupiedCount = 4;
        ParkingSlot.safeAllot(slots, "TN09AB1234");
    }
}

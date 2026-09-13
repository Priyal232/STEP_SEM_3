public class HostelRoomAllocation {

    static class HostelRoom {
        String roomNo;
        int beds;
        int occupied;

        HostelRoom(String roomNo, int beds, int occupied) {
            this.roomNo = roomNo;
            this.beds = beds;
            this.occupied = occupied;
        }

        void allot(String name) {
            occupied++;
        }

        static HostelRoom findAvailableRoom(HostelRoom[] rooms) {
            for (int i = 0; i < rooms.length; i++) {
                if (rooms[i].occupied < rooms[i].beds) {
                    return rooms[i];
                }
            }
            return null;
        }

        static void safeAllot(HostelRoom[] rooms, String studentName) {
            HostelRoom room = findAvailableRoom(rooms);
            if (room == null) {
                System.out.println("No rooms available for " + studentName);
            } else {
                room.allot(studentName);
                System.out.println(studentName + " allotted to room " + room.roomNo);
            }
        }
    }

    public static void main(String[] args) {
        HostelRoom[] rooms = {new HostelRoom("C-214", 3, 2), new HostelRoom("C-507", 2, 2)};
        HostelRoom.safeAllot(rooms, "Divya");
        HostelRoom.safeAllot(rooms, "Divya");
    }
}

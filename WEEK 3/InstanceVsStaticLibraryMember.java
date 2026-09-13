public class InstanceVsStaticLibraryMember {

    static class LibraryMemberBroken {
        static String name;
        static String memberId;
        static int booksIssued;
    }

    static class LibraryMember {
        String name;
        String memberId;
        int booksIssued;
        static String libraryName = "Campus Library";
        static int memberCount = 0;

        LibraryMember(String name, int booksIssued) {
            this.name = name;
            memberCount++;
            this.memberId = "LM-" + (1000 + memberCount);
            this.booksIssued = booksIssued;
        }

        void printMemberCard() {
            System.out.println(name + " | " + memberId);
        }

        static void printTotalMembers() {
            System.out.println("Total members: " + memberCount);
        }
    }

    public static void main(String[] args) {
        new LibraryMemberBroken();
        LibraryMemberBroken.name = "Aditi";
        LibraryMemberBroken.name = "Rohan";
        System.out.println(LibraryMemberBroken.name);
        System.out.println(LibraryMemberBroken.name);

        LibraryMember m1 = new LibraryMember("Aditi", 2);
        LibraryMember m2 = new LibraryMember("Rohan", 1);
        m1.printMemberCard();
        m2.printMemberCard();
        LibraryMember.printTotalMembers();
    }
}

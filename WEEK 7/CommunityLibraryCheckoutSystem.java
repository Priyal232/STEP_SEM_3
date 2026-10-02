public class CommunityLibraryCheckoutSystem {
    static abstract class LibraryItem {
        private static int itemCounter = 1000;
        private final String itemId;
        private final String title;

        public LibraryItem(String title) {
            itemCounter++;
            this.itemId = "LI-" + itemCounter;
            this.title = title;
        }

        public abstract int getLoanPeriodDays();

        public String getItemId() {
            return itemId;
        }

        public String getTitle() {
            return title;
        }
    }

    interface Renewable {
        String renew();
    }

    interface Reservable {
        String reserve();
    }

    static class Textbook extends LibraryItem implements Renewable, Reservable {
        public Textbook(String title) {
            super(title);
        }

        @Override
        public int getLoanPeriodDays() {
            return 14;
        }

        @Override
        public String renew() {
            return getTitle() + " renewed";
        }

        @Override
        public String reserve() {
            return getTitle() + " reserved";
        }
    }

    static class Magazine extends LibraryItem implements Renewable {
        public Magazine(String title) {
            super(title);
        }

        @Override
        public int getLoanPeriodDays() {
            return 7;
        }

        @Override
        public String renew() {
            return getTitle() + " renewed";
        }
    }

    static class DigitalPass implements Renewable {
        private final String resourceName;

        public DigitalPass(String resourceName) {
            this.resourceName = resourceName;
        }

        @Override
        public String renew() {
            return resourceName + " renewed";
        }
    }

    static void processCheckouts(LibraryItem[] items) {
        for (LibraryItem item : items) {
            System.out.println(item.getItemId() + " " + item.getTitle() + ": loan period " + item.getLoanPeriodDays() + " days");
        }
    }

    static String reserveIfSupported(Object o) {
        if (o instanceof Reservable) {
            Reservable reservable = (Reservable) o;
            return reservable.reserve();
        }
        return "Reservation not supported";
    }

    public static void main(String[] args) {
        Textbook t = new Textbook("Java Fundamentals");
        System.out.println(t.getLoanPeriodDays());
        System.out.println(t.renew());
        System.out.println(t.reserve());

        Magazine m = new Magazine("Tech Monthly");
        System.out.println(reserveIfSupported(m));

        DigitalPass d = new DigitalPass("E-Journal Access");
        System.out.println(reserveIfSupported(d));
        System.out.println(d.renew());

        LibraryItem ref = t;
        System.out.println(reserveIfSupported(ref));

        System.out.println("--- Checkouts ---");
        processCheckouts(new LibraryItem[]{ t, m });
    }
}

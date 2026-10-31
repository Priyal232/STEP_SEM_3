public class LibraryPlatform {

    interface Reservable {
        void reserve(String memberId);
    }

    interface Downloadable {
        void download();
    }

    static abstract class Resource {
        private final String id;

        Resource(String id) {
            this.id = id;
        }

        String getId() {
            return id;
        }
    }

    static class Book extends Resource implements Reservable {
        private String reservedBy;

        Book(String id) {
            super(id);
        }

        public void reserve(String memberId) {
            reservedBy = memberId;
        }
    }

    static class EBook extends Resource implements Downloadable {
        private int downloads;

        EBook(String id) {
            super(id);
        }

        public void download() {
            downloads++;
        }
    }

    private Resource[] resources = new Resource[4];
    private int size;

    int find(String id) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = resources[mid].getId().compareTo(id);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return -(lo + 1);
    }

    String add(Resource r) {
        int pos = find(r.getId());
        if (pos >= 0) {
            return "duplicate rejected";
        }
        int insertAt = -(pos + 1);
        if (size == resources.length) {
            Resource[] bigger = new Resource[resources.length * 2];
            System.arraycopy(resources, 0, bigger, 0, size);
            resources = bigger;
        }
        System.arraycopy(resources, insertAt, resources, insertAt + 1, size - insertAt);
        resources[insertAt] = r;
        size++;
        return r.getId() + " added";
    }

    String reserve(String id, String memberId) {
        int idx = find(id);
        if (idx < 0) {
            return id + " not found";
        }
        if (resources[idx] instanceof Reservable res) {
            res.reserve(memberId);
            return id + " reserved for " + memberId;
        }
        return id + " rejected: reserve unsupported";
    }

    String download(String id) {
        int idx = find(id);
        if (idx < 0) {
            return id + " not found";
        }
        if (resources[idx] instanceof Downloadable d) {
            d.download();
            return id + " downloaded";
        }
        return id + " rejected: download unsupported";
    }

    String findMessage(String id) {
        int idx = find(id);
        return idx >= 0 ? id + " found at index " + idx : id + " not found";
    }

    String process(String command) {
        String[] p = command.trim().split("\\s+");
        switch (p[0].toUpperCase()) {
            case "ADD":
                if (p[1].equalsIgnoreCase("Book")) {
                    return add(new Book(p[2]));
                } else if (p[1].equalsIgnoreCase("EBook")) {
                    return add(new EBook(p[2]));
                }
                return "unknown resource type";
            case "RESERVE":
                return reserve(p[1], p[3]);
            case "DOWNLOAD":
                return download(p[1]);
            case "FIND":
                return findMessage(p[1]);
            default:
                return "unknown command";
        }
    }

    public static void main(String[] args) {
        LibraryPlatform library = new LibraryPlatform();
        String[] commands = {
            "ADD Book B1",
            "ADD Book B1",
            "ADD EBook E1",
            "RESERVE B1 by M1",
            "DOWNLOAD B1",
            "DOWNLOAD E1",
            "FIND E1"
        };
        for (String c : commands) {
            System.out.println(library.process(c));
        }
    }
}

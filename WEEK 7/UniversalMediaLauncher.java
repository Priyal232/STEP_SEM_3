public class UniversalMediaLauncher {
    interface Playable {
        String play();
        String play(int fromSecond);
        String pause();
    }

    static abstract class MediaFile {
        private static int fileCounter = 1000;
        private final String fileId;

        public MediaFile() {
            fileCounter++;
            this.fileId = "MF-" + fileCounter;
        }

        public abstract String getFormatInfo();

        public String getFileId() {
            return fileId;
        }
    }

    static class AudioFile extends MediaFile implements Playable {
        private final String title;

        public AudioFile(String title) {
            super();
            this.title = title;
        }

        @Override
        public String getFormatInfo() {
            return "Audio file, ID: " + getFileId();
        }

        @Override
        public String play() {
            return "Playing audio: " + title;
        }

        @Override
        public String play(int fromSecond) {
            return play() + " from " + TimeFormat.of(fromSecond);
        }

        @Override
        public String pause() {
            return "Paused audio: " + title;
        }
    }

    static class Podcast implements Playable {
        private final String showName;
        private final int episodeNumber;

        public Podcast(String showName, int episodeNumber) {
            this.showName = showName;
            this.episodeNumber = episodeNumber;
        }

        @Override
        public String play() {
            return "Streaming episode " + episodeNumber + " of " + showName;
        }

        @Override
        public String play(int fromSecond) {
            return "Streaming episode " + episodeNumber + " of " + showName + " from " + TimeFormat.of(fromSecond);
        }

        @Override
        public String pause() {
            return "Paused podcast: " + showName + " episode " + episodeNumber;
        }
    }

    static class TimeFormat {
        private TimeFormat() {
        }

        static String of(int totalSeconds) {
            return (totalSeconds / 60) + ":" + String.format("%02d", totalSeconds % 60);
        }
    }

    static void launchAll(Playable[] items) {
        for (Playable item : items) {
            System.out.println(item.play());
        }
    }

    public static void main(String[] args) {
        AudioFile a = new AudioFile("Morning Jazz");
        System.out.println(a.play());
        System.out.println(a.play(30));
        System.out.println(a.getFormatInfo());

        Podcast p = new Podcast("Tech Talk", 12);
        System.out.println(p.play());

        Playable ref = a;
        System.out.println(ref.play());

        System.out.println("--- Launch all ---");
        launchAll(new Playable[]{ ref, p });
    }
}

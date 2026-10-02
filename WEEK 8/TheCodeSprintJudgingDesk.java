import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TheCodeSprintJudgingDesk {
    static class Student {
        private final String name;

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class Score {
        private final int idea;
        private final int execution;
        private final int presentation;

        public Score(int idea, int execution, int presentation) {
            validate(idea);
            validate(execution);
            validate(presentation);
            this.idea = idea;
            this.execution = execution;
            this.presentation = presentation;
        }

        private static void validate(int rating) {
            if (rating < 0 || rating > 10) {
                throw new IllegalArgumentException("Score rejected: Each rating must be between 0 and 10.");
            }
        }

        public int getIdea() {
            return idea;
        }

        public int getExecution() {
            return execution;
        }

        public int getPresentation() {
            return presentation;
        }
    }

    interface Track {
        String getName();

        double calculateFinalScore(Score score);
    }

    static class InnovationTrack implements Track {
        @Override
        public String getName() {
            return "Innovation";
        }

        @Override
        public double calculateFinalScore(Score score) {
            return score.getIdea() * 0.5 + score.getExecution() * 0.3 + score.getPresentation() * 0.2;
        }
    }

    static class OpenTrack implements Track {
        @Override
        public String getName() {
            return "Open";
        }

        @Override
        public double calculateFinalScore(Score score) {
            return (score.getIdea() + score.getExecution() + score.getPresentation()) / 3.0;
        }
    }

    static class Judge {
        private final String name;

        public Judge(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void score(Hackathon hackathon, Project project, int idea, int execution, int presentation) {
            hackathon.recordScore(this, project, new Score(idea, execution, presentation));
        }
    }

    static class Project {
        private final String title;
        private final Team team;
        private final Map<Judge, Score> scores = new LinkedHashMap<>();

        Project(String title, Team team) {
            this.title = title;
            this.team = team;
        }

        void putScore(Judge judge, Score score) {
            scores.put(judge, score);
        }

        boolean hasScoreFrom(Judge judge) {
            return scores.containsKey(judge);
        }

        public boolean isScored() {
            return !scores.isEmpty();
        }

        public double getFinalScore() {
            if (scores.isEmpty()) {
                return 0.0;
            }
            double sum = 0;
            for (Score score : scores.values()) {
                sum += team.getTrack().calculateFinalScore(score);
            }
            return sum / scores.size();
        }

        public String getTitle() {
            return title;
        }

        public Team getTeam() {
            return team;
        }
    }

    static class Team {
        private final String name;
        private final List<Student> members = new ArrayList<>();
        private final Track track;
        private Project project;

        Team(String name, Student[] members, Track track) {
            this.name = name;
            for (Student member : members) {
                this.members.add(member);
            }
            this.track = track;
        }

        Project submitProject(String title) {
            if (project != null) {
                throw new IllegalStateException("Submission failed: " + name + " has already submitted a project.");
            }
            project = new Project(title, this);
            return project;
        }

        public String getName() {
            return name;
        }

        public List<Student> getMembers() {
            return new ArrayList<>(members);
        }

        public Track getTrack() {
            return track;
        }

        public Project getProject() {
            return project;
        }
    }

    static class Hackathon {
        public static final String OPEN = "OPEN";
        public static final String JUDGING = "JUDGING";
        public static final String PUBLISHED = "PUBLISHED";
        private static final int MIN_TEAM_SIZE = 2;
        private static final int MAX_TEAM_SIZE = 4;
        private final String name;
        private final List<Team> teams = new ArrayList<>();
        private final Map<Student, Team> studentTeams = new HashMap<>();
        private String state = OPEN;

        public Hackathon(String name) {
            this.name = name;
        }

        public Team registerTeam(String teamName, Student[] members, Track track) {
            if (!OPEN.equals(state)) {
                throw new IllegalStateException("Registration failed: Registration is closed.");
            }
            if (members.length < MIN_TEAM_SIZE || members.length > MAX_TEAM_SIZE) {
                throw new IllegalArgumentException("Registration failed: A team must have " + MIN_TEAM_SIZE + " to " + MAX_TEAM_SIZE + " members.");
            }
            for (int i = 0; i < members.length; i++) {
                for (int j = i + 1; j < members.length; j++) {
                    if (members[i] == members[j]) {
                        throw new IllegalArgumentException("Registration failed: A student cannot be listed twice in a team.");
                    }
                }
            }
            for (Team team : teams) {
                if (team.getName().equalsIgnoreCase(teamName)) {
                    throw new IllegalArgumentException("Registration failed: Team name " + teamName + " is already taken.");
                }
            }
            for (Student student : members) {
                Team existing = studentTeams.get(student);
                if (existing != null) {
                    throw new IllegalStateException("Registration failed: " + student.getName() + " already belongs to team " + existing.getName() + ".");
                }
            }
            Team team = new Team(teamName, members, track);
            teams.add(team);
            for (Student student : members) {
                studentTeams.put(student, team);
            }
            return team;
        }

        public Project submitProject(Team team, String title) {
            if (!OPEN.equals(state)) {
                throw new IllegalStateException("Submission failed: Submissions are closed.");
            }
            if (!teams.contains(team)) {
                throw new IllegalArgumentException("Submission failed: Team is not registered.");
            }
            return team.submitProject(title);
        }

        void recordScore(Judge judge, Project project, Score score) {
            if (PUBLISHED.equals(state)) {
                String action = project.hasScoreFrom(judge) ? "Rescore" : "Scoring";
                throw new IllegalStateException(action + " rejected: Results have already been published.");
            }
            if (!teams.contains(project.getTeam())) {
                throw new IllegalArgumentException("Scoring rejected: Project does not belong to this hackathon.");
            }
            if (OPEN.equals(state)) {
                state = JUDGING;
            }
            project.putScore(judge, score);
        }

        public void publishResults() {
            if (PUBLISHED.equals(state)) {
                throw new IllegalStateException("Publish failed: Results are already published.");
            }
            if (!JUDGING.equals(state)) {
                throw new IllegalStateException("Publish failed: No project has been scored yet.");
            }
            state = PUBLISHED;
        }

        public List<Project> getProjects() {
            List<Project> projects = new ArrayList<>();
            for (Team team : teams) {
                if (team.getProject() != null) {
                    projects.add(team.getProject());
                }
            }
            return projects;
        }

        public String getState() {
            return state;
        }

        public String getName() {
            return name;
        }
    }

    static Team register(Hackathon hackathon, String teamName, Student[] members, Track track) {
        try {
            Team team = hackathon.registerTeam(teamName, members, track);
            System.out.println("Team " + team.getName() + " registered (" + team.getMembers().size() + " members, " + track.getName() + " track).");
            return team;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static void score(Hackathon hackathon, Judge judge, Project project, int idea, int execution, int presentation) {
        try {
            judge.score(hackathon, project, idea, execution, presentation);
            System.out.printf("Score recorded for '%s'. Final score: %.2f.%n", project.getTitle(), project.getFinalScore());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Hackathon hackathon = new Hackathon("Code Sprint 2024");
        Student asha = new Student("Asha");
        Student ravi = new Student("Ravi");
        Student neha = new Student("Neha");
        Student kiran = new Student("Kiran");
        Judge judge = new Judge("Dr. Rao");

        Team byteBusters = register(hackathon, "ByteBusters", new Student[]{asha, ravi, neha}, new InnovationTrack());
        register(hackathon, "SoloCoder", new Student[]{kiran}, new OpenTrack());

        Project smartAttend = hackathon.submitProject(byteBusters, "SmartAttend");
        System.out.println("Project '" + smartAttend.getTitle() + "' submitted by " + byteBusters.getName() + ".");

        score(hackathon, judge, smartAttend, 8, 7, 9);

        hackathon.publishResults();
        System.out.println("Results published.");
        for (Project project : hackathon.getProjects()) {
            System.out.printf("%s - '%s' - %.2f%n", project.getTeam().getName(), project.getTitle(), project.getFinalScore());
        }

        score(hackathon, judge, smartAttend, 10, 7, 9);
    }
}

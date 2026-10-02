import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineExaminationSystemDesignAndImplementation {
    static class Student {
        private final String studentId;
        private final String name;

        public Student(String studentId, String name) {
            this.studentId = studentId;
            this.name = name;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        public Attempt startExamination(Examination examination) {
            return examination.startAttempt(this);
        }
    }

    static abstract class Question {
        private final int number;
        private final String text;

        protected Question(int number, String text) {
            this.number = number;
            this.text = text;
        }

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }

        public abstract boolean isValidAnswer(String answer);

        public abstract boolean isCorrect(String answer);
    }

    static class MultipleChoiceQuestion extends Question {
        private final List<String> options = new ArrayList<>();
        private final String correctOption;

        public MultipleChoiceQuestion(int number, String text, String[] options, String correctOption) {
            super(number, text);
            for (String option : options) {
                this.options.add(option.toUpperCase());
            }
            this.correctOption = correctOption;
        }

        @Override
        public boolean isValidAnswer(String answer) {
            return answer != null && options.contains(answer.toUpperCase());
        }

        @Override
        public boolean isCorrect(String answer) {
            return correctOption.equalsIgnoreCase(answer);
        }
    }

    static class TrueFalseQuestion extends Question {
        private final boolean correctAnswer;

        public TrueFalseQuestion(int number, String text, boolean correctAnswer) {
            super(number, text);
            this.correctAnswer = correctAnswer;
        }

        @Override
        public boolean isValidAnswer(String answer) {
            return "true".equalsIgnoreCase(answer) || "false".equalsIgnoreCase(answer);
        }

        @Override
        public boolean isCorrect(String answer) {
            return Boolean.parseBoolean(answer) == correctAnswer;
        }
    }

    static class Result {
        private final int correctCount;
        private final int totalQuestions;

        public Result(int correctCount, int totalQuestions) {
            this.correctCount = correctCount;
            this.totalQuestions = totalQuestions;
        }

        public int getCorrectCount() {
            return correctCount;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        @Override
        public String toString() {
            return correctCount + "/" + totalQuestions + " correct";
        }
    }

    static class Attempt {
        private static final String IN_PROGRESS = "IN_PROGRESS";
        private static final String SUBMITTED = "SUBMITTED";
        private final Student student;
        private final Examination examination;
        private final Map<Integer, String> answers = new LinkedHashMap<>();
        private String status = IN_PROGRESS;
        private Result result;

        Attempt(Student student, Examination examination) {
            this.student = student;
            this.examination = examination;
        }

        public void answer(int questionNumber, String answer) {
            if (isSubmitted()) {
                throw new IllegalStateException("Cannot change answers: attempt for '" + examination.getTitle() + "' has already been submitted.");
            }
            Question question = examination.getQuestion(questionNumber);
            if (!question.isValidAnswer(answer)) {
                throw new IllegalArgumentException("Invalid answer '" + answer + "' for Question " + questionNumber + ".");
            }
            answers.put(questionNumber, answer.toUpperCase());
        }

        void submit() {
            if (isSubmitted()) {
                throw new IllegalStateException("Attempt for '" + examination.getTitle() + "' has already been submitted.");
            }
            status = SUBMITTED;
            result = evaluate();
        }

        private Result evaluate() {
            int correct = 0;
            List<Question> questions = examination.getQuestions();
            for (Question question : questions) {
                String given = answers.get(question.getNumber());
                if (given != null && question.isCorrect(given)) {
                    correct++;
                }
            }
            return new Result(correct, questions.size());
        }

        public boolean isSubmitted() {
            return SUBMITTED.equals(status);
        }

        public String getStatus() {
            return status;
        }

        public Result getResult() {
            if (result == null) {
                throw new IllegalStateException("Result is available only after submission.");
            }
            return result;
        }

        public Student getStudent() {
            return student;
        }

        public Examination getExamination() {
            return examination;
        }
    }

    static class Examination {
        private final String title;
        private final List<Question> questions = new ArrayList<>();
        private final Map<Student, Attempt> attempts = new HashMap<>();

        public Examination(String title) {
            this.title = title;
        }

        public void addQuestion(Question question) {
            if (!attempts.isEmpty()) {
                throw new IllegalStateException("Questions cannot be added after attempts have started.");
            }
            questions.add(question);
        }

        public Attempt startAttempt(Student student) {
            Attempt existing = attempts.get(student);
            if (existing != null) {
                if (existing.isSubmitted()) {
                    throw new IllegalStateException(student.getName() + " has already submitted an attempt for '" + title + "'.");
                }
                return existing;
            }
            Attempt attempt = new Attempt(student, this);
            attempts.put(student, attempt);
            return attempt;
        }

        public Result submit(Attempt attempt) {
            if (attempt.getExamination() != this) {
                throw new IllegalArgumentException("Attempt does not belong to '" + title + "'.");
            }
            attempt.submit();
            return attempt.getResult();
        }

        public Question getQuestion(int number) {
            for (Question question : questions) {
                if (question.getNumber() == number) {
                    return question;
                }
            }
            throw new IllegalArgumentException("Question " + number + " does not exist in '" + title + "'.");
        }

        public List<Question> getQuestions() {
            return new ArrayList<>(questions);
        }

        public String getTitle() {
            return title;
        }
    }

    public static void main(String[] args) {
        String[] options = {"A", "B", "C", "D"};
        Examination exam = new Examination("Math Quiz");
        exam.addQuestion(new MultipleChoiceQuestion(1, "2 + 2 = ?", options, "A"));
        exam.addQuestion(new MultipleChoiceQuestion(2, "5 x 3 = ?", options, "B"));

        Student student = new Student("S001", "Student");

        Attempt attempt = student.startExamination(exam);
        System.out.println("Examination '" + exam.getTitle() + "' started by " + student.getName() + ".");

        attempt.answer(1, "A");
        System.out.println("Question 1 answered with 'A'.");

        attempt.answer(2, "C");
        System.out.println("Question 2 answered with 'C'.");

        Result result = exam.submit(attempt);
        System.out.println("Examination '" + exam.getTitle() + "' submitted successfully.");
        System.out.println("Result for '" + exam.getTitle() + "' attempt: " + result + ".");

        try {
            attempt.answer(2, "B");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        try {
            student.startExamination(exam);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        Examination science = new Examination("Science Quiz");
        science.addQuestion(new TrueFalseQuestion(1, "Water boils at 100 C at sea level.", true));
        science.addQuestion(new MultipleChoiceQuestion(2, "Chemical symbol of gold?", options, "C"));
        Attempt second = student.startExamination(science);
        second.answer(1, "True");
        second.answer(2, "C");
        System.out.println("Result for '" + science.getTitle() + "' attempt: " + science.submit(second) + ".");
    }
}

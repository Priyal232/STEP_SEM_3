public class BasicDrawingCanvas {
    static abstract class Shape {
        private static int shapeCounter = 1000;
        private final String shapeId;

        public Shape() {
            shapeCounter++;
            this.shapeId = "SH-" + shapeCounter;
        }

        public abstract double calculateArea();

        protected abstract void resize(double factor);

        public void scale(double factor) {
            if (factor <= 0) {
                throw new IllegalArgumentException("Scale factor must be positive");
            }
            resize(factor);
        }

        public void scale(double xFactor, double yFactor) {
            if (xFactor <= 0 || yFactor <= 0) {
                throw new IllegalArgumentException("Scale factors must be positive");
            }
            resize(Math.sqrt(xFactor * yFactor));
        }

        public String getShapeId() {
            return shapeId;
        }
    }

    static class CircleShape extends Shape {
        private double radius;

        public CircleShape(double radius) {
            super();
            this.radius = radius;
        }

        @Override
        public double calculateArea() {
            return Math.PI * radius * radius;
        }

        @Override
        protected void resize(double factor) {
            radius = radius * factor;
        }
    }

    static class SquareShape extends Shape {
        private double side;

        public SquareShape(double side) {
            super();
            this.side = side;
        }

        @Override
        public double calculateArea() {
            return side * side;
        }

        @Override
        protected void resize(double factor) {
            side = side * factor;
        }
    }

    static void printArea(Shape s) {
        System.out.printf("Area of %s: %.2f%n", s.getShapeId(), s.calculateArea());
    }

    public static void main(String[] args) {
        CircleShape c = new CircleShape(5.0);
        System.out.printf("%.2f%n", c.calculateArea());

        SquareShape sq = new SquareShape(4.0);
        System.out.println(sq.calculateArea());

        sq.scale(2.0);
        System.out.println(sq.calculateArea());

        sq.scale(2.0, 0.5);
        System.out.println(sq.calculateArea());

        printArea(c);
        printArea(sq);
    }
}

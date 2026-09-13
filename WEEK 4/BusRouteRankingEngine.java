import java.util.*;

public class BusRouteRankingEngine {

    static class BusRoute {
        private String routeCode;
        private String routeName;
        private int priority;

        public BusRoute(String routeCode, String routeName, int priority) {
            this.routeCode = routeCode;
            this.routeName = routeName;
            this.priority = priority;
        }

        public BusRoute(String routeCode, String routeName) {
            this(routeCode, routeName, 3);
        }

        int compareTo(BusRoute other) {
            if (this.priority != other.priority) {
                return other.priority - this.priority;
            }
            return this.routeCode.compareToIgnoreCase(other.routeCode);
        }

        String getRouteCode() {
            return routeCode;
        }

        static BusRoute[] rankRoutes(BusRoute[] routes) {
            BusRoute[] result = Arrays.copyOf(routes, routes.length);
            for (int i = 0; i < result.length - 1; i++) {
                for (int j = 0; j < result.length - 1 - i; j++) {
                    if (result[j].compareTo(result[j + 1]) > 0) {
                        BusRoute temp = result[j];
                        result[j] = result[j + 1];
                        result[j + 1] = temp;
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        BusRoute[] routes = {
                new BusRoute("RT205L", "Airport Express", 3),
                new BusRoute("rt201j", "City Central", 4),
                new BusRoute("RT299T", "Night Service")
        };
        BusRoute[] ranked = BusRoute.rankRoutes(routes);
        for (int i = 0; i < ranked.length; i++) {
            System.out.println(ranked[i].getRouteCode());
        }
    }
}

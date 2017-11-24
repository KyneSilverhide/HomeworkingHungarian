import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<Employee> employees = Arrays.asList(
                new Employee("Aurélien", WeekDay.MONDAY, WeekDay.FRIDAY),
                new Employee("Grégory", WeekDay.TUESDAY, WeekDay.THURSDAY),
                new Employee("Jean-Pierre", WeekDay.FRIDAY, WeekDay.WEDNESDAY),
                new Employee("Yann", WeekDay.WEDNESDAY, WeekDay.MONDAY),
                new Employee("Lorenzo", WeekDay.MONDAY, WeekDay.FRIDAY)
        );

        final double[][] costMatrix = buildCostMatrix(employees);
        printDebugMatrix(costMatrix);

        HungarianAlgorithm algorithm = new HungarianAlgorithm(costMatrix);
        final int[] homeWorkingDaysIndexes = algorithm.execute();
        printDebugResults(homeWorkingDaysIndexes);

        for (WeekDay weekDay : WeekDay.values()) {
            final List<Employee> homeWorkingEmployees = findHomeWorkingEmployeesThatDay(employees, weekDay, homeWorkingDaysIndexes);
            final List<Employee> workingEmployees = findRemainingEmployees(employees, homeWorkingEmployees);
            printEmployeeRepartitionThatDay(weekDay, homeWorkingEmployees, workingEmployees);
        }
    }

    private static void printEmployeeRepartitionThatDay(final WeekDay weekDay, final List<Employee> homeWorkingEmployees,
                                                        final List<Employee> workingEmployees) {
        System.out.println(weekDay.getName());
        System.out.println("Working : " + workingEmployees.stream()
                .map(Employee::getName).collect(Collectors.joining(", ")));
        System.out.println("Home working : " + homeWorkingEmployees.stream()
                .map(Employee::getName).collect(Collectors.joining(", ")) + "\n");
    }

    private static List<Employee> findRemainingEmployees(final List<Employee> employees, final List<Employee> homeWorkingEmployees) {
        return employees.stream().filter(employee -> !homeWorkingEmployees.contains(employee)).collect(Collectors.toList());
    }

    private static List<Employee> findHomeWorkingEmployeesThatDay(final List<Employee> employees, final WeekDay weekDay,
                                                                  final int[] homeWorkingDaysIndexes) {
        final List<Employee> homeWorkingEmployees = new ArrayList<>();
        // The given array (from the algorithm) will give is a flat list of columns, picked at each row
        for(int offset = 0; offset < Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE; offset++) {
            int offsetDayOrder = weekDay.getOrder() + (offset * WeekDay.workingDays());
            int employeeOrder = findEmployeMatchingRowAt(offsetDayOrder, homeWorkingDaysIndexes);
            homeWorkingEmployees.add(employees.get(employeeOrder));
        }
        return homeWorkingEmployees;
    }

    private static void printDebugResults(int[] homeWorkingDaysIndexes) {
        System.out.println("Results DEBUG");
        System.out.println(Arrays.toString(homeWorkingDaysIndexes));
        System.out.println("\n\n");
    }

    private static void printDebugMatrix(double[][] costMatrix) {
        System.out.println("Matrix DEBUG");
        Arrays.stream(costMatrix)
                .map(cost -> String.join(" ", Arrays.toString(cost)))
                .forEach(System.out::println);
        System.out.println("\n\n");
    }

    private static int findEmployeMatchingRowAt(int dayOrder, int[] homeWorkingDaysIndexes) {
        for (int i = 0; i < homeWorkingDaysIndexes.length; i++) {
            if (homeWorkingDaysIndexes[i] == dayOrder) {
                return i / Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE;
            }
        }
        throw new IllegalArgumentException("Day n°" + dayOrder + " was not found in " + Arrays.toString(homeWorkingDaysIndexes));
    }

    private static double[][] buildCostMatrix(List<Employee> employees) {
        int resources = employees.size() * Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE;
        int places = WeekDay.workingDays() * Config.MAX_NUMBER_OFF_SIMULTANEOUS_HOMEWORKING;
        final double[][] homeWorkingMatrix = initializeEmptyMatrix(resources, places);

        for (int i = 0; i < employees.size(); i++) {
            final Employee employee = employees.get(i);

            int safeHWDays = Math.min(Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE, employee.getPreferredHomeWorkingDays().size());
            for(int day = 0; day < safeHWDays; day++) {
                int dayOrder = employee.getPreferredHomeWorkingDays().get(day).getOrder();
                homeWorkingMatrix[i * Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE][dayOrder] = Config.HOME_WORKING_COST;

                for(int offset = 0; offset < Config.MAX_NUMBER_OFF_SIMULTANEOUS_HOMEWORKING; offset++) {
                    int daysOffset =  dayOrder + (offset * WeekDay.workingDays());
                    homeWorkingMatrix[i * Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE][daysOffset] = Config.HOME_WORKING_COST;
                }
            }
        }
        return homeWorkingMatrix;
    }

    private static double[][] initializeEmptyMatrix(int resources, int places) {
        final double[][] homeWorkingMatrix = new double[resources][places];
        for (int i = 0; i < homeWorkingMatrix.length; i++) {
            for (int j = 0; j < homeWorkingMatrix[i].length; j++) {
                homeWorkingMatrix[i][j] = Config.WORKING_DAYS_COST;
            }
        }
        return homeWorkingMatrix;
    }

}

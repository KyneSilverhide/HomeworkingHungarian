import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Employee {

    private final String name;
    private final List<WeekDay> preferredHomeWorkingDays = new ArrayList<>();

    Employee(String name, WeekDay... preferredHomeWorkingDays) {
        this.name = name;
        if(preferredHomeWorkingDays.length > Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE) {
            throw new IllegalArgumentException("Employee " + name + " can't have more preferred days than " + Config.NUMBER_OF_HOMEWORKING_DAYS_PER_EMPLOYEE);
        }
        Collections.addAll(this.preferredHomeWorkingDays, preferredHomeWorkingDays);
    }

    String getName() {
        return name;
    }

    List<WeekDay> getPreferredHomeWorkingDays() {
        return preferredHomeWorkingDays;
    }
}
public enum WeekDay {

    MONDAY("Lundi", 0),
    TUESDAY("Mardi", 1),
    WEDNESDAY("Mercredi", 2),
    THURSDAY("Jeudi", 3),
    FRIDAY("Vendredi", 4);

    private final String name;
    private final int order;

    WeekDay(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public static int workingDays() {
        return values().length;
    }
}

package peaksoft.house.tasktrackerb9.enums;

public enum ReminderType {
    NONE(0),
    FIVE_MINUTE(5),
    TEN_MINUTE(10),
    FIFTEEN_MINUTE(15),
    THIRD_MINUTE(30);



    final int minutes;

    ReminderType(int minutes) {
        this.minutes = minutes;
    }

    public long getMinutes() {
        return minutes;
    }
}
package peaksoft.house.tasktrackerb9.enums;

import java.util.Map;

public enum ReminderType {
    REMINDER_TYPE(Map.of(5L,"minute")),
    REMINDER_TYPE1(Map.of(15L,"minute")),
    REMINDER_TYPE2(Map.of(30L,"minute")),
    REMINDER_TYPE3(Map.of(60L,"minute"));


    private final Map<Long,String>map;

    ReminderType(Map<Long, String> map) {
        this.map = map;
    }

    public Map<Long, String> getMap() {
        return map;
    }
}

package cs371m.weatherwake.preferences;

/**
 * Created by KC on 4/18/2016.
 */
public class AlarmEditorPreference {

    public enum Key {
        ALARM_NAME,
        ALARM_ACTIVE,
        ALARM_TIME,
        ALARM_REPEAT,
        ALARM_MUSIC,
        ALARM_VIBRATE
    }

    public enum Type {
        STRING,
        INTEGER,
        BOOLEAN,
        LIST,
        LISTS,
        TIME
    }

    private Key key;
    private Object value;
    private Type type;
    private String title;
    private String summary;
    private String[] options;

    public AlarmEditorPreference(Key key, Object value, Type type) {
//        this(key, null, null, null, value, type);
        this(key, value, type, null, null, null);
    }

//    public AlarmEditorPreference(Key key,String title, String summary, String[] options, Object value, Type type) {
    public AlarmEditorPreference(Key key, Object value, Type type, String title, String summary, String[] options) {
        setKey(key);
        setValue(value);
        setType(type);
        setTitle(title);
        setSummary(summary);
        setOptions(options);
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }
}

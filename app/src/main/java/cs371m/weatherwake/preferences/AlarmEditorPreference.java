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
        ALARM_SNOOZE,
        ALARM_PAIRING,
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
    private Object object;
    private Type type;
    private String title;
    private String summary;
    private String[] options;

    public AlarmEditorPreference(Key key, Object object, Type type) {
//        this(key, null, null, null, object, type);
        this(key, object, type, null, null, null);
    }

//    public AlarmEditorPreference(Key key,String title, String summary, String[] options, Object object, Type type) {
    public AlarmEditorPreference(Key key, Object object, Type type, String title, String summary, String[] options) {
        setKey(key);
        setObject(object);
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

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
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

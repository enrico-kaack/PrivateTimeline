package ek.de.privatetimeline.persistence;


import com.orm.SugarRecord;

/**
 * Created by enrico on 28.01.17.
 */

public class KeyValue extends SugarRecord {

    private String key;

    private String value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

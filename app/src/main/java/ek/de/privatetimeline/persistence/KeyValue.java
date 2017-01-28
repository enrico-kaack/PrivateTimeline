package ek.de.privatetimeline.persistence;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by enrico on 28.01.17.
 */
@Entity
public class KeyValue {
    @Id
    private long id;

    private String key;

    private String value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Generated(hash = 1737500805)
    public KeyValue(long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    @Generated(hash = 92014137)
    public KeyValue() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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

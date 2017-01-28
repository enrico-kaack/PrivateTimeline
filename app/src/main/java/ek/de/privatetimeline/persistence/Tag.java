package ek.de.privatetimeline.persistence;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by enrico on 28.01.17.
 */
@Entity
public class Tag {
    @Id
    private long id;

    private String tag;

    @Generated(hash = 909322665)
    public Tag(long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    @Generated(hash = 1605720318)
    public Tag() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}

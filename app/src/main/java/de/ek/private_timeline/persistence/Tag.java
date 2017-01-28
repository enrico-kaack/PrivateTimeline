package de.ek.private_timeline.persistence;


import com.orm.SugarRecord;

/**
 * Created by enrico on 28.01.17.
 */

public class Tag  extends SugarRecord{

    private String tag;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}

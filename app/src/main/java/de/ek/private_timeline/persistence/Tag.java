package de.ek.private_timeline.persistence;



import com.plumillonforge.android.chipview.Chip;

import io.realm.RealmObject;


/**
 * Created by enrico on 28.01.17.
 */

public class Tag  extends RealmObject implements Chip{

    private String tag;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    public Tag() {
    }

    @Override
    public String toString() {
        return getTag();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getTag().equalsIgnoreCase(((Tag)obj).getTag());
    }

    @Override
    public String getText() {
        return getTag();
    }
}

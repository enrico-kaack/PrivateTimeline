package ek.de.privatetimeline.persistence;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.query.LazyList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by enrico on 28.01.17.
 */

@Entity
public class TimelineObject {
    @Id
    private long id;

    private Typ typ;

    private String content;

    private Date time;

    private LazyList<Tag> tags;

    private LazyList<KeyValue> attributes;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public TimelineObject(Typ typ, String content) {
        this.typ = typ;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Typ getTyp() {
        return typ;
    }

    public void setTyp(Typ typ) {
        this.typ = typ;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LazyList<Tag> getTags() {
        return tags;
    }

    public void setTags(LazyList<Tag> tags) {
        this.tags = tags;
    }

    public LazyList<KeyValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(LazyList<KeyValue> attributes) {
        this.attributes = attributes;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public void addAttribute(String key, String value){
        this.attributes.add(new KeyValue(key,value));
    }

    public void addAttribute(KeyValue attr){
        this.attributes.add(attr);
    }

}

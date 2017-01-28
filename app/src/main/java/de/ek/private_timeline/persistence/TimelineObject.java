package de.ek.private_timeline.persistence;



import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by enrico on 28.01.17.
 */


public class TimelineObject extends SugarRecord{

    private int typ;

    private String content;

    private Date time;

    private List<Tag> tags;

    private List<KeyValue> attributes;

    public TimelineObject(){
        this.time = new Date();
        this.tags = new ArrayList<>(3);
        this.attributes = new ArrayList<>(5);
    }

    public TimelineObject(int typ, String content) {
        this();
        this.typ = typ;
        this.content = content;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<KeyValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<KeyValue> attributes) {
        this.attributes = attributes;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }
    public void addAttribute(KeyValue attribute){
        this.attributes.add(attribute);
    }
    public void addAttribute(String key, String value){
        this.attributes.add(new KeyValue(key, value));
    }
}

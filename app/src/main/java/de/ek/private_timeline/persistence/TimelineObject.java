package de.ek.private_timeline.persistence;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


/**
 * Created by enrico on 28.01.17.
 */


public class TimelineObject extends RealmObject{

    private int typ;

    private String content;

    private Date time;

    private RealmList<Tag> tags;

    private RealmList<KeyValue> attributes;

    public TimelineObject(){
        this.time = new Date();

    }

    public TimelineObject(int typ, String content) {
        this.typ = typ;
        this.content = content;
        this.time = new Date();

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

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public List<KeyValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(RealmList<KeyValue> attributes) {
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

    public String getAttributeValue(String key){
        for (int i = 0; i<attributes.size();i++){
            if (attributes.get(i).getKey().equalsIgnoreCase(key)){
                return attributes.get(i).getValue();
            }
        }
        return null;
    }
}

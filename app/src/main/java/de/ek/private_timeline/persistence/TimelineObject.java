package de.ek.private_timeline.persistence;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


/**
 * Created by enrico on 28.01.17.
 */


public class TimelineObject extends RealmObject{
    @Ignore
    public static final String IMAGE_COUNT = "image_count";
    @Ignore
    public static final String IMAGE_PATH = "image_path";


    @PrimaryKey
    private String id;

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

    public String getId() {
        return id;
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
        if (!this.tags.contains(tag))
            this.tags.add(tag);
    }
    public void addAttribute(KeyValue attribute, Realm realm){
        realm.copyToRealm(attribute);
        this.attributes.add(attribute);

    }
    public void addAttribute(String key, String value){
        this.attributes.add(new KeyValue(key, value));
    }

    public void setAttribute(KeyValue attribute, Realm realm){
        boolean found = false;
        for (int i=0;i<attributes.size();i++) {
            if (attributes.get(i).getKey().equals(attribute.getKey())){
                attributes.get(i).setValue(attribute.getValue());
                found = true;
            }
        }
        if (!found){
            addAttribute(attribute, realm);
        }
    }

    public String getAttributeValue(String key){
        for (int i = 0; i<attributes.size();i++){
            if (attributes.get(i).getKey().equalsIgnoreCase(key)){
                return attributes.get(i).getValue();
            }
        }
        return null;
    }

    public void clearAttributes(){
        this.attributes.deleteAllFromRealm();
    }

    public ArrayList<String> getImageList(){
        ArrayList<String> images = new ArrayList<>();
        if (getAttributeValue(IMAGE_COUNT) != null){
            for (int i=0;i<Integer.parseInt(getAttributeValue(IMAGE_COUNT));i++){
                images.add(getAttributeValue(IMAGE_PATH + i));
            }
            return images;
        }else{
            return null;
        }
    }

    public void setImageList(ArrayList<String>  images, Realm realm){
        //removeAttributeByKey(IMAGE_COUNT);
        //removeAttributeByKey(IMAGE_PATH);

        //set image count
        setAttribute(new KeyValue(IMAGE_COUNT, String.valueOf(images.size())), realm);

        //set all image paths
        for (int i=0;i<images.size();i++){
            setAttribute(new KeyValue(IMAGE_PATH+i, images.get(i)), realm);
        }
    }

    private void removeAttributeByKey(String keyToDelete) {
        for (int i=0; i<attributes.size();i++){
            if (attributes.get(i).getKey().contains(keyToDelete)){
                attributes.deleteFromRealm(i);
            }
        }
    }
}

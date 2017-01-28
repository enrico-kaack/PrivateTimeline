package de.ek.private_timeline.persistence;



import com.orm.SugarRecord;

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


}

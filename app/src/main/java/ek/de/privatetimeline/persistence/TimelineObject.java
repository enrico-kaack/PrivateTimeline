package ek.de.privatetimeline.persistence;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.query.LazyList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by enrico on 28.01.17.
 */

@Entity
public class TimelineObject {
    @Id
    private long id;

    private int typ;

    private String content;

    private Date time;

    @ToMany(referencedJoinProperty = "id")
    private List<Tag> tags;
    @ToMany(referencedJoinProperty = "id")
    private List<KeyValue> attributes;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1207215664)
    private transient TimelineObjectDao myDao;


    public TimelineObject(int typ, String content) {
        this.typ = typ;
        this.content = content;
    }



    @Generated(hash = 909971080)
    public TimelineObject(long id, int typ, String content, Date time) {
        this.id = id;
        this.typ = typ;
        this.content = content;
        this.time = time;
    }



    @Generated(hash = 1595127915)
    public TimelineObject() {
    }



    public void addAttribute(String key, String value){
        this.attributes.add(new KeyValue(key,value));
    }

    public void addAttribute(KeyValue attr){
        this.attributes.add(attr);
    }



    public long getId() {
        return this.id;
    }



    public void setId(long id) {
        this.id = id;
    }



    public int getTyp() {
        return this.typ;
    }



    public void setTyp(int typ) {
        this.typ = typ;
    }



    public String getContent() {
        return this.content;
    }



    public void setContent(String content) {
        this.content = content;
    }



    public Date getTime() {
        return this.time;
    }



    public void setTime(Date time) {
        this.time = time;
    }



    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 741843276)
    public List<Tag> getTags() {
        if (tags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TagDao targetDao = daoSession.getTagDao();
            List<Tag> tagsNew = targetDao._queryTimelineObject_Tags(id);
            synchronized (this) {
                if (tags == null) {
                    tags = tagsNew;
                }
            }
        }
        return tags;
    }



    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 404234)
    public synchronized void resetTags() {
        tags = null;
    }



    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1179129715)
    public List<KeyValue> getAttributes() {
        if (attributes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            KeyValueDao targetDao = daoSession.getKeyValueDao();
            List<KeyValue> attributesNew = targetDao
                    ._queryTimelineObject_Attributes(id);
            synchronized (this) {
                if (attributes == null) {
                    attributes = attributesNew;
                }
            }
        }
        return attributes;
    }



    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1697487056)
    public synchronized void resetAttributes() {
        attributes = null;
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }



    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 739136645)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTimelineObjectDao() : null;
    }

}

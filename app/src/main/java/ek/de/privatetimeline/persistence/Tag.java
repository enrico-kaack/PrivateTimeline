package ek.de.privatetimeline.persistence;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by enrico on 28.01.17.
 */
@Entity
public class Tag {
    @Id
    private long id;

    private String tag;


}

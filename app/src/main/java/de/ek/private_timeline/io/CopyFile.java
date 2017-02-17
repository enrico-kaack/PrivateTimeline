package de.ek.private_timeline.io;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Enrico on 17.02.2017.
 */

public class CopyFile {
    File source;
    File dest;

    public CopyFile(File source, File dest) {
        this.source = source;
        this.dest = dest;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public File getDest() {
        return dest;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }
}

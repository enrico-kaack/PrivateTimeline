package de.ek.private_timeline.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

import de.ek.private_timeline.Constant;

/**
 * Created by Enrico on 29.01.2017.
 */

public class FileHelper {
    public static String getNewRandomFileName(String src, String destRoot){
        return destRoot + getGuid() + src.substring(src.lastIndexOf("."));
    }


    public static boolean copy(File src, File dest){
        try {
            dest.createNewFile();
            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dest);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }
    private static String getGuid(){
        return UUID.randomUUID().toString();
    }
}

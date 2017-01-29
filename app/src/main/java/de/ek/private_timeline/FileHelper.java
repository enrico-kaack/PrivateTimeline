package de.ek.private_timeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * Created by Enrico on 29.01.2017.
 */

public class FileHelper {
    public static String copyFileToInternalStorage(String source_path, String dest_path_without_name){
        File src =  new File(source_path);
        File dest = new File(dest_path_without_name + getGuid() + src.getName().substring(src.getName().lastIndexOf(".")));

        if (copy(src, dest)){
            return dest.getPath();
        }else{
            return null;
        }
    }


    private static boolean copy(File src, File dest){
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

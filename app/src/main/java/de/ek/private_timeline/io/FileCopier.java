package de.ek.private_timeline.io;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Enrico on 17.02.2017.
 */

public class FileCopier extends AsyncTask<CopyFile, Integer, Boolean> {
    private ArrayList<CopyFile> failList = new ArrayList<>();
    private ArrayList<CopyFile> successList = new ArrayList<>();
    private FileCopierUpdate listener;

    public FileCopier(FileCopierUpdate listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onTransferInProgress();
    }

    @Override
    protected Boolean doInBackground(CopyFile... copyFiles) {
        boolean success = true;
        for (CopyFile f:copyFiles) {
            if (!FileHelper.copy(f.getSource(), f.getDest())){
                success = false;
                failList.add(f);
            }else{
                successList.add(f);
            }
        }

        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success){
            listener.onSuccess(successList);
        }else{
            listener.onFail(failList, successList);
        }
    }
}


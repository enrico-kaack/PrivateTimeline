package de.ek.private_timeline.io;

import java.util.ArrayList;

public interface FileCopierUpdate{
        public void onTransferInProgress();
        public void onSuccess(ArrayList<CopyFile> succesFiles);
        public void onFail(ArrayList<CopyFile> failedFiles, ArrayList<CopyFile> succesFiles);
}

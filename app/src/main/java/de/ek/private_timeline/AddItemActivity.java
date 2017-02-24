package de.ek.private_timeline;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.ek.private_timeline.io.CopyFile;
import de.ek.private_timeline.io.FileCopier;
import de.ek.private_timeline.io.FileCopierUpdate;
import de.ek.private_timeline.io.FileHelper;
import de.ek.private_timeline.list.ItemInteractionListener;
import de.ek.private_timeline.list.RecyclerViewAdapterPreview;
import de.ek.private_timeline.list.RecyclerViewAdapterTimeLine;
import de.ek.private_timeline.persistence.KeyValue;
import de.ek.private_timeline.persistence.Paths;
import de.ek.private_timeline.persistence.Tag;
import de.ek.private_timeline.persistence.TimelineObject;
import de.ek.private_timeline.persistence.Typ;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddItemActivity extends AppCompatActivity implements FileCopierUpdate, ItemInteractionListener{
    Realm realm;
    TimelineObject timelineObject;
    final int PICK_IMAGE_REQUEST = 1;

    //Layout Components
    private EditText in_content;
    private MultiAutoCompleteTextView in_tags;

    //Tags
    String[] tagArray;
    RealmResults<Tag> tags;

    //Images
    private ArrayList<String> images = new ArrayList<>(3);
    private List<CopyFile> succesfulCopiedFiles = new ArrayList<>(3);
    private int runningThreadCounter = 0;

    //PreviewList
    private RecyclerViewAdapterPreview adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        realm = Realm.getDefaultInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        ImageButton btn_add_image = (ImageButton)findViewById(R.id.btn_add_img);
        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        in_content = (EditText)findViewById(R.id.in_content);
        in_tags = (MultiAutoCompleteTextView)findViewById(R.id.in_tags);

        //RecycleView
        RecyclerView rv_timeLine = (RecyclerView)findViewById(R.id.preview_recyc_view);
        adapter = new RecyclerViewAdapterPreview(images, getApplication(), this);
        rv_timeLine.setAdapter(adapter);
        rv_timeLine.setHasFixedSize(true);
        rv_timeLine.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        setTokenAdapter();

        loadSavedItemToEdit(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (timelineObject != null){
                    realm.beginTransaction();
                    timelineObject.deleteFromRealm();
                    realm.commitTransaction();
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadSavedItemToEdit(Bundle savedInstanceState) {
        String id = getIntent().getStringExtra("id");
        if (id != null){
            timelineObject = realm.where(TimelineObject.class).equalTo("id", id).findFirst();

            if (timelineObject != null){
                in_content.setText(timelineObject.getContent());

                //tags
                List<Tag> tags = timelineObject.getTags();
                String tag_string = "";
                for (Tag tag:tags){
                    tag_string += tag.getText();
                }
                in_tags.setText(tag_string);


                //images
                if (timelineObject.getTyp() == Typ.IMAGES){
                    List<String> imgList = timelineObject.getImageList();

                    for (int i=0; i< imgList.size();i++){
                        images.add(imgList.get(i));

                    }
                    loadImages();


                }
            }
        }

    }

    private void setTokenAdapter() {
        tags = realm.where(Tag.class).findAll();
        tagArray = new String[tags.size()];
        for (int i = 0; i < tags.size(); i++){
            tagArray[i] = tags.get(i).getTag();
        }
        final ArrayAdapter tagSuggestionAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tagArray);
        in_tags.setAdapter(tagSuggestionAdapter);
        in_tags.setThreshold(2);
        in_tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                saveImages(images);

        }
    }

    private void saveImages(ArrayList<Image> new_images){
        List<CopyFile> copyFiles = new ArrayList<>(3);
        for (Image img: new_images){
            String path = FileHelper.getNewRandomFileName(img.getPath(), getFilesDir().getPath() + Paths.IMAGE_FILE_PATH);
            copyFiles.add(new CopyFile(new File(img.getPath()), new File(path)));

        }

        FileCopier copier = new FileCopier(this);
        copier.execute(copyFiles.toArray(new CopyFile[copyFiles.size()]));

    }

    private void addImage() {
        ImagePicker.create(this)
                .start(PICK_IMAGE_REQUEST);
    }

    private void saveData() {
        if (runningThreadCounter > 0){
            Toast.makeText(getApplicationContext(), "Copying files in progress, please wait...", Toast.LENGTH_SHORT).show();
            return;
        }

        //modify the timeline object
        realm.beginTransaction();
        if (timelineObject == null){
            timelineObject = realm.createObject(TimelineObject.class, UUID.randomUUID().toString());
        }
        timelineObject.setContent(((EditText)findViewById(R.id.in_content)).getText().toString());
        realm.commitTransaction();

        //modify additional images
        if (images != null && images.size() > 0){
            realm.beginTransaction();
            timelineObject.setTyp(Typ.IMAGES);
            timelineObject.setImageList(images, realm);
            realm.commitTransaction();

        }


        //handle tags
        if (in_tags.getText().toString().length() > 0){
            String[] split = in_tags.getText().toString().split(", ");
            for (String tagExtracted : split) {
                realm.beginTransaction();
                Tag tagMatching = realm.where(Tag.class).equalTo("tag", tagExtracted).findFirst();
                if (tagMatching != null){
                    timelineObject.addTag(tagMatching);
                }else{
                    Tag tag = realm.createObject(Tag.class);
                    tag.setTag(tagExtracted);
                    timelineObject.addTag(tag);
                    Log.d("tag saved", tag.getTag());
                }
                realm.commitTransaction();}
        }

        finish();
    }


    @Override
    public void onTransferInProgress() {
        runningThreadCounter++;
    }

    @Override
    public void onSuccess(ArrayList<CopyFile> succesFiles) {
        runningThreadCounter--;
        images.addAll(getImageListAsString(succesFiles));
        loadImages();
    }

    @Override
    public void onFail(ArrayList<CopyFile> failedFiles, ArrayList<CopyFile> succesFiles) {
        runningThreadCounter--;
        images.addAll(getImageListAsString(succesFiles));
        loadImages();
        Toast.makeText(getApplicationContext(), "Copy of files failed!", Toast.LENGTH_LONG).show();

    }


    private ArrayList<String> getImageListAsString(List<CopyFile> list){
        ArrayList<String> imgList = new ArrayList<>();
        for (CopyFile file : list){
            imgList.add(file.getDest().getPath());
        }
        return imgList;
    }


    private ArrayList<String> getImageListAsString(){
        ArrayList<String> imgList = new ArrayList<>();
        for (CopyFile file : succesfulCopiedFiles){
            imgList.add(file.getDest().getPath());
        }
        return imgList;
    }

    private void loadImages(){
        adapter.setNewData(images);
    }

    @Override
    public void onItemClick(int pos) {
        images.remove(pos);
        adapter.setNewData(images);
    }
}

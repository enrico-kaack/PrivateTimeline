package de.ek.private_timeline;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import de.ek.private_timeline.persistence.KeyValue;
import de.ek.private_timeline.persistence.Tag;
import de.ek.private_timeline.persistence.TimelineObject;
import de.ek.private_timeline.persistence.Typ;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddItemActivity extends AppCompatActivity implements FileCopierUpdate{
    final int PICK_IMAGE_REQUEST = 1;

    private EditText in_content;
    private MultiAutoCompleteTextView in_tags;
    private LinearLayout image_list;

    String[] tagArray;
    RealmResults<Tag> tags;

    Realm realm;
    private List<Image> images = new ArrayList<>(3);
    private List<CopyFile> copyFiles = new ArrayList<>(3);
    private List<CopyFile> succesfulCopiedFiles = new ArrayList<>(3);
    private int runningThreadCounter = 0;

    TimelineObject timelineObject;


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

        Button btn_add_image = (Button)findViewById(R.id.btn_add_img);
        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        in_content = (EditText)findViewById(R.id.in_content);
        image_list = (LinearLayout)findViewById(R.id.image_switch);
        in_tags = (MultiAutoCompleteTextView)findViewById(R.id.in_tags);

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

                //single image
                if (timelineObject.getTyp() == Typ.SINGLE_IMAGE){
                    String image_path = timelineObject.getAttributeValue("image_path");
                    images.add(new Image(0,image_path , image_path, false ));

                    ImageView imgView = new ImageView(this);
                    imgView.setAdjustViewBounds(true);
                    imgView.setMaxHeight(300);
                    image_list.addView(imgView);
                    Glide.with(this).load(image_path).fitCenter().into(imgView);
                }

                //multiple images
                if (timelineObject.getTyp() == Typ.MULTIPLE_IMAGES){
                    int image_count = Integer.parseInt(timelineObject.getAttributeValue("image_count"));

                    for (int i=0; i< image_count;i++){
                        String image_path = timelineObject.getAttributeValue("image_path" + i);
                        images.add(new Image(0,image_path , image_path, false ));
                        ImageView imgView = new ImageView(this);
                        imgView.setAdjustViewBounds(true);
                        imgView.setMaxHeight(300);
                        image_list.addView(imgView);
                        Glide.with(this).load(image_path).fitCenter().into(imgView);
                    }


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
        for (Image img: new_images){
            String path = FileHelper.getNewRandomFileName(img.getPath(), getFilesDir().getPath() + "/images/");
            copyFiles.add(new CopyFile(new File(img.getPath()), new File(path)));
            img.setPath(path);
            images.add(img);

            ImageView imgView = new ImageView(this);
            imgView.setAdjustViewBounds(true);
            imgView.setMaxHeight(300);
            image_list.addView(imgView);
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
        }

        realm.beginTransaction();
        if (timelineObject == null){
            timelineObject = realm.createObject(TimelineObject.class, UUID.randomUUID().toString());
        }else{
            //clear additional data
            timelineObject.clearAttributes();
        }
        timelineObject.setContent(((EditText)findViewById(R.id.in_content)).getText().toString());
        realm.commitTransaction();

        //handle single image
        if (succesfulCopiedFiles != null && succesfulCopiedFiles.size() == 1){
            realm.beginTransaction();
            timelineObject.setTyp(Typ.SINGLE_IMAGE);
            KeyValue image_path_object = realm.createObject(KeyValue.class);
            image_path_object.setKey("image_path");
            image_path_object.setValue(succesfulCopiedFiles.get(0).getDest().getPath());
            timelineObject.getAttributes().add(image_path_object);
            realm.commitTransaction();

        }else if (succesfulCopiedFiles != null && succesfulCopiedFiles.size() > 1){
            realm.beginTransaction();
            timelineObject.setTyp(Typ.MULTIPLE_IMAGES);
            KeyValue image_count = realm.createObject(KeyValue.class);
            image_count.setKey("image_count");
            image_count.setValue(String.valueOf(succesfulCopiedFiles.size()));
            timelineObject.addAttribute(image_count);
            realm.commitTransaction();
            for (int i=0; i< succesfulCopiedFiles.size();i++){
                realm.beginTransaction();
                KeyValue image_path_object = realm.createObject(KeyValue.class);
                image_path_object.setKey("image_path" + String.valueOf(i));
                image_path_object.setValue(succesfulCopiedFiles.get(i).getDest().getPath());
                timelineObject.getAttributes().add(image_path_object);
                realm.commitTransaction();
            }


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
        succesfulCopiedFiles = succesFiles;
        for (int i=0; i<succesFiles.size();i++){
            Glide.with(this).load(succesFiles.get(i).getDest().getPath()).fitCenter().into((ImageView)image_list.getChildAt(i));
        }
    }

    @Override
    public void onFail(ArrayList<CopyFile> failedFiles, ArrayList<CopyFile> succesFiles) {
        runningThreadCounter--;
        succesfulCopiedFiles = succesFiles;
        for (int i=0; i<succesFiles.size();i++){
            Glide.with(this).load(succesFiles.get(i).getDest().getPath()).fitCenter().into((ImageView)image_list.getChildAt(i));
        }
        Toast.makeText(getApplicationContext(), "Copy of files failed!", Toast.LENGTH_LONG).show();

    }
}

package de.ek.private_timeline;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

import de.ek.private_timeline.persistence.Tag;
import de.ek.private_timeline.persistence.TimelineObject;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddItemActivity extends AppCompatActivity {
    final int PICK_IMAGE_REQUEST = 1;

    RealmResults<Tag> tags;
    MultiAutoCompleteTextView in_tags;
    String[] tagArray;
    Realm realm;

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



        in_tags = (MultiAutoCompleteTextView)findViewById(R.id.in_tags);
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
            for (int i=0;i<images.size();i++){
                Log.d("images: ", images.get(i).getPath());
            }

        }
    }

    private void addImage() {
        ImagePicker.create(this)
                .start(PICK_IMAGE_REQUEST);
    }

    private void saveData() {
        realm.beginTransaction();
        TimelineObject timelineObject = realm.createObject(TimelineObject.class);
        timelineObject.setContent(((EditText)findViewById(R.id.in_content)).getText().toString());
        realm.commitTransaction();

        String[] split = in_tags.getText().toString().split(", ");
        for (String tagExtracted : split) {
            realm.beginTransaction();
            Tag t = new Tag(tagExtracted);
            timelineObject.addTag(t);
            if (realm.where(Tag.class).equalTo("tag", tagExtracted).count() == 0){
                realm.copyToRealm(t);
                Log.d("tag saved", t.getTag());
            }
            realm.commitTransaction();
        }

        finish();
    }
}

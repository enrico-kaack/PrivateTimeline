package de.ek.private_timeline;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import de.ek.private_timeline.persistence.TimelineObject;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimelineObject timelineObject = new TimelineObject(0, ((EditText)findViewById(R.id.in_content)).getText().toString());
                timelineObject.save();
            }
        });
    }
}

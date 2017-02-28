package de.ek.private_timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import java.io.File;

import de.ek.private_timeline.list.ItemInteractionListener;
import de.ek.private_timeline.list.RecyclerViewAdapterTimeLine;
import de.ek.private_timeline.persistence.TimelineObject;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements ItemInteractionListener {
    RealmResults<TimelineObject> timelineObjectList;
    RecyclerViewAdapterTimeLine adapter;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Realm
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        //Init file system
        File images = new File(getFilesDir().getPath() + "/images/");
        images.mkdir();

        timelineObjectList = realm.where(TimelineObject.class).findAll().sort("time", Sort.DESCENDING);

        RecyclerView rv_timeLine = (RecyclerView)findViewById(R.id.recycl_view);
        adapter = new RecyclerViewAdapterTimeLine(timelineObjectList, getApplication(), this);
        rv_timeLine.setAdapter(adapter);
        rv_timeLine.setHasFixedSize(true);
        rv_timeLine.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_impressum) {
            Intent i = new Intent(MainActivity.this, ImpressumActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int pos) {
        Intent i = new Intent(MainActivity.this, AddItemActivity.class);
        i.putExtra("id", adapter.getObjectForPosition(pos).getId());
        startActivity(i);
    }
}

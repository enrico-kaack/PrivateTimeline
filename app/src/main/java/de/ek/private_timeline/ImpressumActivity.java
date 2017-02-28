package de.ek.private_timeline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ImpressumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);

        TextView tv_impressum = (TextView)findViewById(R.id.tv_impressum);
        tv_impressum.setText(Html.fromHtml(getString(R.string.impressum_long_text)));

        TextView tv_license = (TextView)findViewById(R.id.tv_license);
        tv_license.setText(Html.fromHtml(getString(R.string.license)));

    }
}

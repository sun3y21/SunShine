package com.example.sunnny.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i=getIntent();
        String data=i.getStringExtra(Intent.EXTRA_TEXT);
        TextView t=(TextView)findViewById(R.id.weatherText);
        t.setText(data);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_detail,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

}

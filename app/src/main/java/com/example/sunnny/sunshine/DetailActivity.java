package com.example.sunnny.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //adding tool bar
        Toolbar t=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(t);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState==null)
        {
            Fragment df=new DetailActivityFragment();
            Bundle arguments=new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_URI,getIntent().getData());
            df.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.weather_detail_container,df).commit();
        }
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }
}

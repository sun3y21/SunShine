package com.example.sunnny.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item_forcast,R.id.listitem_forcast_text,new ArrayList<String>());
        ListView listView=(ListView)findViewById(R.id.listview_forcast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String data=adapter.getItem(i);
                Intent intent=new Intent(getApplicationContext(),DetailActivity.class).putExtra(Intent.EXTRA_TEXT,data);
                startActivity(intent);


            }
        });

        

    }

   public void updateWeather()
    {
        FetchWeatherTask fatchWeatherTask=new FetchWeatherTask(getApplicationContext(),adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String location = prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        fatchWeatherTask.execute(location);
    }


    public void onStart()
    {
        super.onStart();
        updateWeather();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=this.getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_refresh:
                updateWeather();
                break;
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }


}

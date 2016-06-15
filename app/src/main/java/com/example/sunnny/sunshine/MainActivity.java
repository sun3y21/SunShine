package com.example.sunnny.sunshine;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sunnny.sunshine.data.WeatherContract;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FORECAST_LOADER = 0;
    ForecastAdapter adapter;
    //String mLocation;

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new ForecastAdapter(getApplicationContext(),null, 0);

        ListView listView=(ListView)findViewById(R.id.listview_forcast);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(FORECAST_LOADER,savedInstanceState,this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor=(Cursor)adapterView.getItemAtPosition(i);

                if(cursor!=null)
                {
                    String locationSettings=Utility.getPreferredLocation(getApplicationContext());
                    Intent intent=new Intent(getApplicationContext(),DetailActivity.class).setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationSettings,cursor.getLong(COL_WEATHER_DATE)));
                    startActivity(intent);
                }
            }
        });

    }


   public void updateWeather()
    {
        FetchWeatherTask fatchWeatherTask=new FetchWeatherTask(getApplicationContext());
        String location =Utility.getPreferredLocation(getApplicationContext());
        fatchWeatherTask.execute(location);

    }


//    public void onStart()
//    {
//        super.onStart();
//        updateWeather();
//
//    }

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



    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String locationSettings=Utility.getPreferredLocation(getApplicationContext());

        String sortOrder= WeatherContract.WeatherEntry.COLUMN_DATE+" ASC";
        Uri weatherForLocationUri=WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSettings,System.currentTimeMillis());
        return new android.content.CursorLoader(getApplicationContext(),weatherForLocationUri,FORECAST_COLUMNS,null,null,sortOrder);

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }




}

package com.example.sunnny.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sunnny.sunshine.SunshineService.SunShineService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback{

    String mLocation;
    boolean mTwoPane;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOCATION_STATUS_OK,LOCATION_STATUS_SERVER_DOWN,LOCATION_STATUS_SERVER_INVALID,LOCATION_STATUS_UNKNOWN})
    public @interface LocationStatus{}

    public final static int LOCATION_STATUS_OK=0;
    public final static int LOCATION_STATUS_SERVER_DOWN=1;
    public final static int LOCATION_STATUS_SERVER_INVALID=2;
    public final static int LOCATION_STATUS_UNKNOWN=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adding tool bar
        Toolbar t=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(t);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mLocation=Utility.getPreferredLocation(getApplicationContext());

        if(findViewById(R.id.weather_detail_container)!=null)
        {
            mTwoPane = true;
            if (savedInstanceState == null) {

                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                if(ft!=null)
                {
                    ft.replace(R.id.weather_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                            .commit();
                }
            }
            ForecastFragment f=(ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            ForecastAdapter forecastAdapter=f.getmForecastAdapter();
            forecastAdapter.setmUseTodayLayout(false);
        }
        else
        {
            mTwoPane=false;
        }



    }

    private void openPreferredLocationInMap() {
        String location = Utility.getPreferredLocation(this);

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("Error : ", "Couldn't call " + location + ", no receiving apps installed!");


        }
    }


        @Override
        protected void onResume() {
            super.onResume();
            ForecastFragment ff=null;
            String location = Utility.getPreferredLocation( this );
            // update the location in our second pane using the fragment manager
            if (location != null && !location.equals(mLocation)) {
                ff = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            }
            if ( null != ff )
            {
                ff.onLocationChanged();
            }
            DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df )
            {
                df.onLocationChanged(location);
            }
            mLocation = location;
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        Intent intent = new Intent(getApplicationContext(), SunShineService.class);
        intent.putExtra(SunShineService.LOCATION_QUERY_EXTRA,
                Utility.getPreferredLocation(getApplicationContext()));
         startService(intent);
    }

    @Override
    public void onItemSelected(Uri dateUri) {
        if(mTwoPane)
        {
            Bundle b=new Bundle();
            b.putParcelable(DetailActivityFragment.DETAIL_URI,dateUri);
            DetailActivityFragment df=new DetailActivityFragment();
            df.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container,df,DETAILFRAGMENT_TAG).commit();
        }
        else
        {
            Intent i=new Intent(getApplicationContext(),DetailActivity.class).setData(dateUri);
            startActivity(i);
        }
    }


}

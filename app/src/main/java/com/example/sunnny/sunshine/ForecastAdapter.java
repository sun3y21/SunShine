package com.example.sunnny.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sunnny on 14/06/16.
 */
public class ForecastAdapter extends CursorAdapter{

    final int VIEW_TYPE_TODAY=0;
    final int VIEW_TYPE_FUTURE_DAY=1;
    boolean mUseTodayLayout=true;


    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }



    Context mContext;
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext=context;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext,high, isMetric) + "/" + Utility.formatTemperature(mContext,low, isMetric);
        return highLowStr;
    }

    public void setmUseTodayLayout(boolean value)
    {
        mUseTodayLayout=value;
    }

    @Override
    public int getItemViewType(int pos)
    {

        return pos==0&&mUseTodayLayout?VIEW_TYPE_TODAY:VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {


        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType=getItemViewType(cursor.getPosition());
        View view=null;
        if(viewType==VIEW_TYPE_TODAY)
        {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast_today, parent, false);
        }
        else
        {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_forcast, parent, false);
        }

        ViewHolder viewHolder=new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        ViewHolder viewHolder=(ViewHolder)view.getTag();

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        // Use placeholder image for now

        if(getItemViewType(cursor.getPosition())==VIEW_TYPE_TODAY)
        {
            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        }
        else
        {
            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
        }




        // TODO Read date from cursor

        // Read date from cursor
        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        // Find TextView and set formatted date on it
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        // TODO Read weather forecast from cursor
        // Read weather forecast from cursor
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
               // Find TextView and set weather forecast on it
        viewHolder.descriptionView.setText(description);


        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText("Max : "+Utility.formatTemperature(mContext,high, isMetric));

        // TODO Read low temperature from cursor
        double low=cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText("Min : "+Utility.formatTemperature(mContext,low,isMetric));

    }
}

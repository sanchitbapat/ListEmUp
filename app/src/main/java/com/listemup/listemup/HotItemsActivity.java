package com.listemup.listemup;

import com.listemup.listemup.util.SystemUiHider;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.codec.digest.DigestUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class HotItemsActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_items);
        final int x= (int) Math.ceil(Math.random()*100);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        final ParseObject testObject = new ParseObject("TestObject");
        Button list=(Button)findViewById(R.id.dummy_button);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Test");
                query.whereEqualTo("DName", "SGH-T399");
                query.setLimit(1);
                query.orderByDescending("updatedAt");
                query.getFirstInBackground(new GetCallback<ParseObject>() {

                    @Override
                    public void done(ParseObject busobj, ParseException e) {
                        // TODO Auto-generated method stub
                        if (e == null) {
                            Log.d("Inside getLatLng()","called get first record");
                            Lat = busobj.getString("Latitude");
                            Lng = busobj.getString("Longitude");
                            latA =  Double.valueOf(Lat);
                            lngA = Double.valueOf(Lng);
                            locationA.setLatitude(latA);
                            locationA.setLongitude(lngA);
                            currLocation = new LatLng(latA, lngA);

                            bus = map.addMarker(new MarkerOptions().position(currLocation)
                                    .title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus) ));

                            float zoom = map.getCameraPosition().zoom;
                            if (zoom < 14){
                                zoom = 14;
                                // Move the camera instantly to hamburg with a zoom of 15.
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, zoom));
                                // Zoom in, animating the camera.
                                map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 10000, null);
                            }

                            txtlabel = (TextView) findViewById(R.id.textView1);

                            Geocoder gcd = new Geocoder(cntxt, Locale.getDefault());
                            try{
                                List<Address> addresses = gcd.getFromLocation(latA, lngA, 1);
                                if (addresses.size() > 0)
                                {
                                    txtlabel.setText("The Bus is at " + addresses.get(0).getAddressLine(0));
                                }
                            }catch(Exception ep){

                            }
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });*/

                final TextView textView= (TextView) findViewById(R.id.editText);

                final String text= textView.getText().toString();
                ParseObject items = new ParseObject("Items");
                items.put("Name",text);
                items.put("Location",x);
                items.saveInBackground();


/*make a list*/
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
                query.whereEqualTo("Location", x);
                query.findInBackground(new FindCallback<ParseObject>() {
                    String s="";
                    public void done(List<ParseObject> itemList, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < itemList.size(); i++) {
                                ParseObject p= itemList.get(i);
                                s+=p.getString("Name")+"\n";
                                // 1 - can call methods of element
                                // 2 - can use i to make index-based calls to methods of list

                                // ...
                            }

                            TextView view= (TextView) findViewById(R.id.fullscreen_content);
                            view.setText(s);
                        } else {
                            //Log.d("score", "Error: " + e.getMessage());
                        }


                    }
                });

/*maintain count*/

                System.out.println("\n\ntext\n\n" + text + "\n\n\n");
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("ItemsCount");
                query1.whereEqualTo("Name", text);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> itemList, ParseException e) {
                        if (e == null) {
                            if(itemList.size()==0) {
                                ParseObject itemsCount = new ParseObject("ItemsCount");
                                itemsCount.put("Name",text);
                                itemsCount.put("Count",1);
                                itemsCount.saveInBackground();
                            }
                            else{
                                itemList.get(0).increment("Count");
                                itemList.get(0).saveInBackground();
                            }
                            //.increment("Count");
                        } else {

                            //Log.d("score", "Error: " + e.getMessage());
                        }


                    }
                });
                //startActivity(new Intent(HotItemsActivity.this, HotItemsActivity.class));
            }

        });

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

package com.dawid.dawiddelimata.bmicalculator_ver14;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity  implements BillingProcessor.IBillingHandler  {

    private Handler handler = new Handler();

    BillingProcessor bp;
    //LikeView likeView;

//    private ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);;
//
//    private int progressStatus = 0;
//
//    private Handler handler = new Handler();

    AdView mAdview;
    File imagePath;


    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public void share(View v) {
        Bitmap bitmap = takeScreenshot();
        saveBitmap(bitmap);
        Toast.makeText(this, "CLICKED", Toast.LENGTH_SHORT).show();
        shareIt();
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "My highest score with screen shot";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Catch score");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

//    public void share(View view){
//
//        View screenView = view.getRootView();
//        screenView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
//        screenView.setDrawingCacheEnabled(false);
//        return bitmap;

//        Bitmap bitmap = takeScreenshot();
//        saveBitmap(bitmap);
//        shareIt();
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        String shareBody = "Your body here";
//        String shareSub = "Your Subject here";
//        intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
//        intent.putExtra(Intent.EXTRA_TEXT, shareSub);
//        startActivity(Intent.createChooser(intent, "Share using"));


//    public Bitmap takeScreenshot() {
//        View rootView = findViewById(android.R.id.content).getRootView();
//        rootView.setDrawingCacheEnabled(true);
//        return rootView.getDrawingCache();
//    }
//
//    public void saveBitmap(Bitmap bitmap) {
//        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(imagePath);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            Log.e("GREC", e.getMessage(), e);
//        } catch (IOException e) {
//            Log.e("GREC", e.getMessage(), e);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Enter height, weight and click blue button", Toast.LENGTH_LONG).show();
        MobileAds.initialize(this, "ca-app-pub-1989255292557669/7509769377");                           // Initialize of Ads
        buttonListenerMethod();

        AppRater.app_launched(this);

        //createShortCut();
        //shareMethod();

        bp = new BillingProcessor(this, "com.dawid.dawiddelimata.bmicalculator_ver14", this);                                                    // Add billing processor. Instead of licence null used for test

        // Add AdMob advertisement banner
        mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("1DB7C92893CB70AD23614BEAC2C07861").build();
        mAdview.loadAd(adRequest);

        // Displaying created bar in xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("    BMI Calculator");                                           // Set name of new toolbar
        getSupportActionBar().setLogo(R.drawable.scale);                                                // Set toolbar icon








    }



    // Create three dot toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set options of three dot menu list
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch(id){
            case R.id.Remove_adds:
                bp.purchase(MainActivity.this, "com.dawid.dawiddelimata.bmicalculator_ver14");
                Toast.makeText(this, "Remove Adds", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonListenerMethod(){

        EditText editText = (EditText) findViewById(R.id.bmiCat);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addCourseFromTextBox();
                            return true;
                        default:
                            break;
                    }
                }

                return false;
            }

            private void addCourseFromTextBox() {

            }
        });


        ImageButton button = (ImageButton) findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                // Hide virtual keyboard if button pressed
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                final EditText heightInput = (EditText) findViewById(R.id.heightInput);
                if (heightInput.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please insert Height and Weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                String heightStr = heightInput.getText().toString();
                double height = Double.parseDouble(heightStr);

                final EditText weightInput = (EditText) findViewById(R.id.weightInput);
                if (weightInput.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please insert Height and Weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                String weightStr = weightInput.getText().toString();
                double weight = Double.parseDouble(weightStr);

                height /= 100;
                final double bmi = (weight) / (height * height);

                final EditText bmiSum = (EditText) findViewById(R.id.bmiSum);
                bmiSum.setText(Double.toString(bmi).format("%.2f", bmi));                              // Round result two places after dot "."
                //String.format("%.2f", bmiSum);

                String bmi_cat;
                if(bmi < 15)
                    bmi_cat = "Very severely underweight";
                else if(bmi < 16)
                    bmi_cat = "Severely underweight";
                else if(bmi < 18.5)
                    bmi_cat = "Underweight";
                else if(bmi < 25)
                    bmi_cat = "Normal weight";
                else if(bmi < 30)
                    bmi_cat = "Overweight";
                else if(bmi < 35)
                    bmi_cat= "Moderately obese";
                else if(bmi < 40)
                    bmi_cat = "Severely obese";
                else
                    bmi_cat = "Very severely\"morbidly\" obese";

                final TextView bmiCat = (TextView) findViewById(R.id.bmiCat);
                bmiCat.setText(bmi_cat);

                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       final int bmi2 = (int) bmi + 24;

                        if (bmi2 < 100){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(bmi2);
                                }
                            });
                        }
                    }
                }).start();

            }


        });


    }



//    public ProgressBar progressBar;
//    progressBar = new ProgressBar(context);
//    progressBar.show();

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "You've purchased", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }


}

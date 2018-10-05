package com.subratgupta.epaperdemo;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.logging.Level;

public class MainActivity extends AppCompatActivity {

    private ImageView mPageView;
    private StorageReference mStorage;
    private StorageReference mStorageRef;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    Boolean move = false;

    RelativeLayout.LayoutParams parms;
    LinearLayout.LayoutParams par;
    float dx=0,dy=0,x=0,y=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPageView = findViewById(R.id.page_view);
        mStorage = FirebaseStorage.getInstance().getReference();
        loadPaper("2");

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

    }

    private void loadPaper(String page){
        mStorageRef = mStorage.child("paper").child("hdimage"+page+".jpg");
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
//                mPageView.loadUrl(imageURL);
//                mPageView.getSettings().setBuiltInZoomControls(true);
//                mPageView.getSettings().setUseWideViewPort(true);
                Glide.with(getApplicationContext()).load(imageURL).into(mPageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (move){
            //Start
            try{
                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN :
                    {
                        RelativeLayout relativeLayout = findViewById(R.id.relative_layout);
                        parms = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                        par = (LinearLayout.LayoutParams) getWindow().findViewById(Window.ID_ANDROID_CONTENT).getLayoutParams();
                        dx = motionEvent.getRawX() - parms.leftMargin;
                        dy = motionEvent.getRawY() - parms.topMargin;
                    }
                    break;
                    case MotionEvent.ACTION_MOVE :
                    {
                        x = motionEvent.getRawX();
                        y = motionEvent.getRawY();
                        parms.leftMargin = (int) (x-dx);
                        parms.topMargin = (int) (y - dy);
                        mPageView.setLayoutParams(parms);
                    }
                    break;
                    case MotionEvent.ACTION_UP :
                    {

                    }
                    break;
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            //End
        }
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 10.0f));
            mPageView.setScaleX(mScaleFactor);
            mPageView.setScaleY(mScaleFactor);
            mPageView.setFocusable(true);

            if (mScaleFactor == 1){
                move = false;
                enableSwipe();
            } else {
                mPageView.setOnTouchListener(null);
                move = true;
            }

//            Toast.makeText(getApplicationContext(),""+mScaleFactor,Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private void enableSwipe(){
        mPageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

}

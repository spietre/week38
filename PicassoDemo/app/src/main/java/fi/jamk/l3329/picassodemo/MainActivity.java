package fi.jamk.l3329.picassodemo;



import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private int imageIndex;
    // swipe down and up values
    private float x1, x2;
    private List<String> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        images = new ArrayList<String>();

        images.add("http://i.imgur.com/DvpvklR.png");
        images.add("https://www.w3schools.com/css/img_fjords.jpg");
        showImage();
    }

        // method gets called when user performs any touch event on screen
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 < x2) { // left to right -> previous
                    imageIndex--;
                    if (imageIndex < 0) imageIndex = images.size()-1;
                } else { // right to left -> next
                    imageIndex++;
                    if (imageIndex > (images.size()-1)) imageIndex = 0;
                }
                showImage();
                break;
        }
        return false;
    }

    public void showImage(){

        Picasso.with(this)
                .load(images.get(imageIndex))
                .into(imageView);
    }
}

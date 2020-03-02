package be.ehb.mycamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    //avoid magic numbers
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //UI
    private ImageView ivProfile;
    //reference to picture
    File currentImage;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
        }
    };

    //LifeCycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfile = findViewById(R.id.iv_profile);
        ivProfile.setOnClickListener(clickListener);

        updateImageView();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            updateImageView();
        }
    }

    private void updateImageView() {
        Picasso.get()
                .load(currentImage)
                .placeholder(R.mipmap.ic_launcher_round)
                .resize(200, 200)
                .centerCrop()
                .into(ivProfile);
    }

    //photo stuff
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // V2, Create the File where the photo should go
            currentImage = createImageFile();
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                    "be.ehb.mycamera.fileprovider",
                    currentImage);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() {
        // Create an image file name
        String imageFileName = "/"+System.currentTimeMillis()+"pic_profile.jpg";
        File storageDir = getFilesDir();
        return new File(storageDir + imageFileName);
    }
}

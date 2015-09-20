package gitcash.instacal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.OutputStream;

public class PhotoPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();
        String filePath = "";
        if (extras != null){
            filePath = extras.getString("filePath");
        }
        Bitmap orientedBitmap = BitmapFactory.decodeFile(filePath);
       // Bitmap orientedBitmap = rotateBitmap(filePath, BitmapFactory.decodeFile(filePath));
        Log.d("PhotoPreview", "TRANSITIONED TO PHOTO PREVIEW!");
        Matrix matrix = new Matrix();
        imageView.setImageMatrix(matrix);
        imageView.setImageBitmap(orientedBitmap);
        imageView.setRotation(90);

        Button useThisButton = (Button) findViewById(R.id.useThisButton);
        useThisButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tesseractOutput =  "testing blah"; //Tesseract.ocrstuff(filePath);
                        Intent intent = new Intent(PhotoPreviewActivity.this, CalEventActivity.class);
                        intent.putExtra("TesseractOutput", tesseractOutput);
                        startActivity(intent);
                    }
                }
        );

        Button takeAnotherButton = (Button) findViewById(R.id.takeAnotherButton);
        takeAnotherButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PhotoPreviewActivity.this, CameraActivity.class);
                        Log.d("PhotoPreview", "About to go back to camera");
                        startActivity(intent);

                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_preview, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
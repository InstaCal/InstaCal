package gitcash.instacal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

        Bitmap orientedBitmap = rotateBitmap(filePath, BitmapFactory.decodeFile(filePath));
        Log.d("PhotoPreview", "TRANSITIONED TO PHOTO PREVIEW!");

        imageView.setImageBitmap();

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

    private Bitmap rotateBitmap(String filePath, Bitmap bitmap) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(90);
                    break;
                case 4:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(0);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(0);
                    break;
                case 7:
                    matrix.setRotate(-180);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-180);
                    break;
                default:
                    return bitmap;
            }
            Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return oriented;
        } catch (Throwable e){
            return bitmap;
        }
    }

}
package gitcash.instacal;

import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.BitmapFactory;
import com.googlecode.tesseract.android.TessBaseAPI;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TessActivity extends AppCompatActivity {
    private static final String TAG = "TessActivity";

    public static final String lang = "eng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tess);

        String DATA_PATH = getFilesDir().toString();
        /*
            Create a tessdata directory on device's data drive
         */
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "/tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.d(TAG, "ERROR: Creation of directory " + path + " on file system failed");
                    return;
                } else {
                    Log.d(TAG, "Created directory " + path + " on file system");
                }
            } else {
                Log.d(TAG,"It's there tho");
            }

        }

        /*
           Copy language assets over to data directory from assets folder
         */

        if (!(new File(DATA_PATH + "/tessdata/" + lang + ".traineddata")).exists()) {

                AssetManager assetManager = getAssets();
                try {
                    InputStream in = assetManager.open(lang + ".traineddata");

                    OutputStream out = new FileOutputStream(DATA_PATH
                            + "/tessdata/" + lang + ".traineddata");

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + lang + " traineddata");

                } catch(IOException e){
                    Log.e(TAG, "No file bruh");
                }
        } else {
            Log.d(TAG,"The lang file is there doe bruh");
        }

        //TODO: R.drawable.eventposter should be the actual jpg file output by camera
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eventposter);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, "eng");

        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();

         Log.d(TAG, "Returned text is " + recognizedText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tess, menu);
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

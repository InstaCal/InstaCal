package gitcash.instacal;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gitcash.instacal.R;

/**
 * Created by kwameefah on 9/20/15.
 */
public class OCRService {

    private static final String TAG = "TessActivity";
    public static final String lang = "eng";
    public String DATA_PATH;
    public String imagePath;
    public AssetManager assetManager;
    public Context theContext;

    public OCRService(String dirpath, String path, Context context) {
        DATA_PATH = dirpath;
        imagePath = path;
        assetManager = context.getAssets();
        theContext = context;
    }

    public void createDir() {
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
    }

    public void copyLanguageAssets() {
        if (!(new File(DATA_PATH + "/tessdata/" + lang + ".traineddata")).exists()) {
            try {

                InputStream in = assetManager.open("eng.traineddata");

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

    }

    public String getTextContent() {
        Log.d("whathaeljf", "The second image path is " + imagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);


        /*
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
*/
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, lang);

        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        return recognizedText;

    }

}

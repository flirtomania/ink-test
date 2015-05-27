package spire.myapplication2;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.simplify.ink.InkView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            fragment = new PlaceholderFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    PlaceholderFragment fragment = new PlaceholderFragment();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            fragment.traceSize();
            fragment.saveJpeg();
            fragment.savePng();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        InkView ink;

        void traceSize() {
            ByteArrayOutputStream streamPng = new ByteArrayOutputStream();
            ink.getBitmap(Color.WHITE).compress(Bitmap.CompressFormat.PNG, 100, streamPng);
            Log.e("", "uncompressed:" + ink.getBitmap().getAllocationByteCount());
            Log.e("", "png:" + streamPng.toByteArray().length);

            ByteArrayOutputStream streamWebp = new ByteArrayOutputStream();
            ink.getBitmap(Color.WHITE).compress(Bitmap.CompressFormat.WEBP, 100, streamWebp);
            Log.e("", "webp:" + streamWebp.toByteArray().length);

            ByteArrayOutputStream streamJpeg = new ByteArrayOutputStream();
            ink.getBitmap(Color.WHITE).compress(Bitmap.CompressFormat.JPEG, 100, streamJpeg);
            Log.e("", "jpeg:" + streamJpeg.toByteArray().length);
        }

        void saveJpeg() {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //ink.setBackgroundColor(0xffffffff);
                ink.getBitmap(Color.WHITE).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                Log.e("", "jpeg:" + stream.toByteArray().length);

                FileOutputStream f = new FileOutputStream("/storage/emulated/0/.inks/ink.jpeg");
                f.write(stream.toByteArray());
                f.flush();
                f.close();
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }

        void savePng() {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //ink.setBackgroundColor(0xffffffff);
                ink.getBitmap(Color.WHITE).compress(Bitmap.CompressFormat.PNG, 100, stream);
                Log.e("", "png:" + stream.toByteArray().length);

                FileOutputStream f = new FileOutputStream("/storage/emulated/0/.inks/ink.png");
                f.write(stream.toByteArray());
                f.flush();
                f.close();
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            try {
                File dir = new File("/storage/emulated/0/.inks");
                dir.mkdirs();
                File file = new File("/storage/emulated/0/.inks/test.txt");
                file.createNewFile();
            } catch (IOException e) {
                Log.e("", "", e);
            }

            ink = (InkView) getActivity().findViewById(R.id.ink);
            ink.setColor(getResources().getColor(android.R.color.black));
            ink.setMinStrokeWidth(1.5f);
            ink.setMaxStrokeWidth(6f);

            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ink.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            ink.getBitmap().getAllocationByteCount();
            Log.e("", stream.toByteArray().length + "");*/
        }
    }
}

package fi.jamk.l3329.musicplayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    //path to mp3-files
    private String mediaPath;
    //list of strings to hold mp3-files
    private List<String> songs = new ArrayList<String>();
    //media player for playing music
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //use acyncTask to load filenames
    private LoadSongsTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        mediaPath = Environment.getExternalStorageDirectory().getPath()+ "/Music/";

        //item listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mediaPlayer.reset();
                    //in recursive version
                    mediaPlayer.setDataSource(songs.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e){
                    Toast.makeText(getBaseContext(),"Cannot start audio!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //mp3 files recursively from sdcard ( takes time to
        //make a list if a lot of songs in sdcard)
        task = new LoadSongsTask();
        task.execute();
    }


    private class LoadSongsTask extends AsyncTask<Void, String, Void>{
        private List<String> loadedSongs = new ArrayList<String>();

        protected void onPreExecute(){
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
        }

        protected Void doInBackground(Void... url){
            updateSongListRecursive(new File(mediaPath));
            return null;
        }

        public void updateSongListRecursive(File path){
            if (path.isDirectory()){
                for (int i = 0; i< path.listFiles().length; i++){
                    File file = path.listFiles()[i];
                    updateSongListRecursive(file);
                }
            } else {
                String name = path.getAbsolutePath();
                publishProgress(name);
                if (name.endsWith(".mp3")){
                    loadedSongs.add(name);
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<String> songList = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1,loadedSongs);
            listView.setAdapter(songList);
            songs = loadedSongs;

            Toast.makeText(getApplicationContext(), "Songs=" + songs.size(),
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }
}

package in.teachcoder.app.boommusicplayer;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button previousButton;
    private Button playButton;
    private Button nextButton;

    public static MediaPlayer iPod = new  MediaPlayer();

    public static int currentSong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previousButton = (Button) findViewById(R.id.prevBtn);
        playButton = (Button) findViewById(R.id.playBtn);
        nextButton = (Button) findViewById(R.id.nextBtn);


         final ArrayList<File> rootFolder = searchDirectory(Environment.getExternalStorageDirectory());

//        File[] allRootFiles = rootFolder.listFiles();

        String[] nameArray = new String[rootFolder.size()];

        for(int i=0; i<rootFolder.size();i++){
            String nameOfFile = rootFolder.get(i).getName();
            nameArray[i] = nameOfFile;

        }

        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameArray );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                iPod.stop();
                iPod.release();

                Uri uri = Uri.fromFile(rootFolder.get(i));

                iPod = MediaPlayer.create(MainActivity.this, uri);

                iPod.start();

                Toast.makeText(MainActivity.this, "Song Clicked " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();


                currentSong = i;
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                iPod.stop();
                iPod.release();
                currentSong = currentSong - 1;

                if(currentSong == -1){
                    currentSong=rootFolder.size()-1;
                }


                Uri uri = Uri.fromFile(rootFolder.get(currentSong));
                iPod = MediaPlayer.create(MainActivity.this, uri);
                iPod.start();

                Toast.makeText(MainActivity.this, "Song "+ uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();


            }


        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(iPod.isPlaying()){
                    Toast.makeText(MainActivity.this,"Paused ", Toast.LENGTH_SHORT).show();
                    iPod.pause();
                    playButton.setText(">");

                }else{
                    iPod.start();
                    playButton.setText("||");
                   Toast.makeText(MainActivity.this,"Playing", Toast.LENGTH_SHORT).show();
                }


            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iPod.stop();
                iPod.release();
                currentSong = currentSong + 1;

                if(currentSong >= rootFolder.size()){
                    currentSong=0;
                }


                Uri uri = Uri.fromFile(rootFolder.get(currentSong));
                iPod = MediaPlayer.create(MainActivity.this, uri);
                iPod.start();

                Toast.makeText(MainActivity.this, "Song "+ uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();

            }



        });






    }

    private ArrayList<File> searchDirectory(File root){

        ArrayList<File> songs = new ArrayList<File>();

        File[] rootFiles = root.listFiles();

        for (File file:rootFiles) {

            if (file.isDirectory()){
                //do something
                songs.addAll(searchDirectory(file)) ;

            }else{
                if(file.getName().endsWith(".mp3")){
                    songs.add(file);
                }
            }

        }

        return songs;

    }






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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

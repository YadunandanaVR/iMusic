package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class musicList extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        listView = findViewById(R.id.listView);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //Toast.makeText(musicList.this, "granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySongs = fetctSongs(Environment.getExternalStorageDirectory());
                        String []items = new String[mySongs.size()];

                        for(int i=0;i<mySongs.size();i++){
                            items[i] = mySongs.get(i).getName().replace(".mp3", "");
                            //Log.d("all songs name: ", items[i]);
                        }

                        //Default Simple list view
                        //ArrayAdapter<String> adopter = new ArrayAdapter<String>(musicList.this, android.R.layout.simple_list_item_1, items);

                        //Custom ArrayAdapter
                        myMusicListAdapter adopter = new myMusicListAdapter(musicList.this, R.layout.custom_list_layout, items);
                        listView.setAdapter(adopter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(musicList.this, playMusic.class);
                                String currentSong = listView.getItemAtPosition(position).toString();

                                intent.putExtra("songList", mySongs);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> fetctSongs(File file){

        ArrayList<File> arrayList = new ArrayList<>();
        File []songs = file.listFiles();

        if(songs != null){
            for(File myFile : songs){
                if(myFile.isDirectory() && !myFile.isHidden()){
                    arrayList.addAll(fetctSongs(myFile));
                }

                else{
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }

        return arrayList;
    }
}
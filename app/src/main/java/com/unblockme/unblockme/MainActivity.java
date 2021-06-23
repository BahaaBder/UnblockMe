package com.unblockme.unblockme;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.unblockme.unblockme.shared.MusicManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
boolean unMutedMode=true;
private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MusicManager mms=new MusicManager(this);
        mms.startMusic();
        Button infoButton = findViewById(R.id.info_bt);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,
                        InfoActivity.class);
                startActivity(intent);
            }
        });
        Button usingGameButton = findViewById(R.id.usingGame_bt);
        usingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,
                        UsingActivity.class);

                startActivity(intent);
            }
        });
        ImageView soundButton=(ImageView)findViewById(R.id.unmute_bt);

        soundButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(unMutedMode) {
                    soundButton.setImageResource(R.drawable.mute);
                    unMutedMode=false;
                    mms.pauseMusic();
                }else
                {
                    soundButton.setImageResource(R.drawable.unmute);
                    unMutedMode=true;
                    mms.refreshTheMusic();
                }

            }
        });
        Button mButton = findViewById(R.id.buttonPlay);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,
                        LevelActivity.class);
                if(unMutedMode){intent.putExtra("MusicStatus",true);}
                else{intent.putExtra("MusicStatus",false);}
                startActivity(intent);
            }
        });


        Button btnabout = findViewById(R.id.buttonAbout);
        btnabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getText(R.string.developerDialogTitle));
                builder.setMessage(getText(R.string.developerDialogMsg));
                builder.setPositiveButton(getText(R.string.developerDialogMsgbt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
            }
        });


        Button btnlogout = findViewById(R.id.buttonExit);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getText(R.string.exitDialogTitle));
                builder.setMessage(getText(R.string.exitDialogMsg));

                builder.setPositiveButton(getText(R.string.exitDialogYes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();

                    }
                });

                builder.setNegativeButton(getText(R.string.exitDialogNo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
            }
        });
    }
}

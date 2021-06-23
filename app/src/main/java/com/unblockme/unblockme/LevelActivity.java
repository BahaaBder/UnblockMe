package com.unblockme.unblockme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.unblockme.unblockme.shared.GameManager;

public class LevelActivity extends AppCompatActivity {
ListView lv;
    Intent intent;
    @Override
    protected void onDestroy() {
        super.onDestroy();


       // Toast.makeText(getBaseContext(), "onDestroy LV", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       SecondActivity.initialEntry=true;
      //  Toast.makeText(getBaseContext(), "onRestart LV", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

      //  Toast.makeText(getBaseContext(), "onResume LV", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
      //  Toast.makeText(getBaseContext(), "onPause LV", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        lv=(ListView)findViewById(R.id.levellist);

      //  Toast.makeText(getBaseContext(), "onCreate LV", Toast.LENGTH_SHORT).show();
        intent=new Intent(LevelActivity.this,
                SecondActivity.class);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:

                        intent.putExtra("MusicStatus",getIntent().getBooleanExtra
                                ("MusicStatus",true));
                        intent.putExtra("Level",1);
                        intent.putExtra("InitialEntry",true);
                        startActivity(intent);
                        //Toast.makeText(getBaseContext(), "00000", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:

                        intent.putExtra("MusicStatus",getIntent().getBooleanExtra
                                ("MusicStatus",true));
                        intent.putExtra("Level",2);
                        intent.putExtra("InitialEntry",true);
                        startActivity(intent);
                       // Toast.makeText(getBaseContext(), "111111", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent.putExtra("MusicStatus",getIntent().getBooleanExtra
                                ("MusicStatus",true));
                        intent.putExtra("Level",3);
                        intent.putExtra("InitialEntry",true);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra("MusicStatus",getIntent().getBooleanExtra
                                ("MusicStatus",true));
                        intent.putExtra("Level",4);
                        intent.putExtra("InitialEntry",true);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.putExtra("MusicStatus",getIntent().getBooleanExtra
                                ("MusicStatus",true));
                        intent.putExtra("Level",5);
                        intent.putExtra("InitialEntry",true);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
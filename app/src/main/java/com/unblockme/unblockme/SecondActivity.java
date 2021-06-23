package com.unblockme.unblockme;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unblockme.unblockme.shared.GameManager;
import com.unblockme.unblockme.shared.MusicManager;
import com.unblockme.unblockme.view.GridView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class SecondActivity extends AppCompatActivity implements Observer {
    static final int TIME_OUT = 3000;

    private static Button button_pause;
    private static Button button_return;
    private static Button button_restart;
    private final String recName = "records.txt";
    private GridView gv;
    private int numberOfMovements = 0;
    private int currentLevel;
    public static   boolean MusicStatus,initialEntry=true;
    private HashMap<Integer, Integer> records = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(getBaseContext(), "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  Toast.makeText(getBaseContext(), "onResume", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
       // Toast.makeText(getBaseContext(), "onPause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

      //  Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_SHORT).show();
        MusicStatus=getIntent().getBooleanExtra("MusicStatus",true);
        if(initialEntry) {
            GameManager.getInstance().level =
                    getIntent().getIntExtra("Level", 0);
            initialEntry=false;
        }
        this.readRecord();
        // Force the display in portrait mode
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);


        button_pause = findViewById(R.id.button_pause);

        button_pause.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        button_return = findViewById(R.id.button_back);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameManager.getInstance().undoLastMove();
                gv.update();
            }
        });

        button_restart = findViewById(R.id.button_restart);

        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentLevelLabel(GameManager.getInstance().getLevel());
                GameManager.getInstance().setLevel(GameManager.getInstance().getLevel());
                reload();
            }
        });

        ImageView img = findViewById(R.id.Left_Arrow);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentLevelLabel(GameManager.getInstance().getLevel() - 1);
                GameManager.getInstance().setLevel(GameManager.getInstance().getLevel() - 1);
                reload();
            }
        });

        img = findViewById(R.id.Right_Arrow);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentLevelLabel(GameManager.getInstance().getLevel() + 1);
                GameManager.getInstance().setLevel(GameManager.getInstance().getLevel() + 1);
                reload();
            }
        });
        button_restart.setEnabled(false);
        button_return.setEnabled(false);

        initGrid();
        updateNumberOfMovements();
        updateCurrentLevelLabel(currentLevel);
        updateRecordValue(this.records.get(currentLevel));
    }

    private void initGrid() {
        currentLevel = GameManager.getInstance().getLevel();
        GameManager.getInstance().setLevel(currentLevel);
        gv = findViewById(R.id.gridView5);
        gv.setGrid(GameManager.getInstance().grid());
        GameManager.getInstance().observeStackChange(this);
    }

    private void updateNumberOfMovements() {
        if (numberOfMovements == 0) {
            button_restart.setEnabled(false);
            button_return.setEnabled(false);
        } else {
            button_restart.setEnabled(true);
            button_return.setEnabled(true);
        }

        TextView viewMoves = findViewById(R.id.moves);
        viewMoves.setText(Integer.toString(numberOfMovements));
    }

    private void updateRecordValue(int rec) {
        TextView re = findViewById(R.id.record);
        StringBuilder sb = new StringBuilder();
        if (rec != -1) {
            sb.append("Record: ").append(rec).append('/').append(
                    GameManager.getInstance().getMinimalMoveNumber()
            );
        } else {
            sb.append("Record: ").append("--").append('/').append(
                    GameManager.getInstance().getMinimalMoveNumber()
            );
        }
        re.setText(sb.toString());
    }

    private void updateCurrentLevelLabel(int level) {
        currentLevel = level;

        ImageView img = findViewById(R.id.Left_Arrow);
        img.setVisibility((this.currentLevel < 2) ? View.INVISIBLE : View.VISIBLE);

        img = findViewById(R.id.Right_Arrow);
        img.setVisibility((this.currentLevel > 4) ? View.INVISIBLE : View.VISIBLE);

        TextView viewLevel = findViewById(R.id.level);
        viewLevel.setText(Integer.toString(currentLevel));

    }

    @Override
    public void update(Observable o, Object arg) {
        this.numberOfMovements = GameManager.getInstance().undoStackSize();
        Log.i("UNDO", "Undo size: " + this.numberOfMovements);
        updateNumberOfMovements();
        if (GameManager.getInstance().grid().isSolved()) {
            int cur = records.get(currentLevel);

            if ((cur > GameManager.getInstance().undoStackSize()) || (cur == -1)) {
                records.put(currentLevel, GameManager.getInstance().undoStackSize());
                updateRecordValue(records.get(currentLevel));
            }

            writeRecord();
            this.showFinishDialog();
        }
    }

    private void writeRecord() {
        // level:<record>
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(recName, MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            for (Integer i : this.records.keySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(i).append(':').append(this.records.get(i));
                bw.write(sb.toString());
                bw.newLine();
            }

            bw.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readRecord() {
        FileInputStream is;
        try {
            is = openFileInput(recName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 1; i <= 5; i++) {
                String[] split = br.readLine().split(":");
                this.records.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }

            br.close();
            is.close();
        } catch (Exception e) {
            this.records.put(1, -1);
            this.records.put(2, -1);
            this.records.put(3, -1);
            this.records.put(4, -1);
            this.records.put(5, -1);
        }
    }

    private void reload() {
        finish();
        startActivity(getIntent());
    }
   // Move to Next Level ..
    private void showFinishDialog() {
        if(MusicStatus)
        {
        MusicManager.startEffect(true,1);}
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.congratulations);
        builder.setMessage("\n\n"+ getText(R.string.nextLevelDialog)+"\n\n");
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                updateCurrentLevelLabel(GameManager.getInstance().getLevel() + 1);
                GameManager.getInstance().setLevel(GameManager.getInstance().getLevel() + 1);
                reload();
            }
        }, TIME_OUT);

    }
}

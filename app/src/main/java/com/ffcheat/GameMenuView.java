package com.ffcheat;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class GameMenuView extends LinearLayout {
    public static boolean ESP_ENABLED = true;
    public static boolean AIM_ENABLED = true;
    public static float FOV_DEGREES = 90f;

    public GameMenuView(Context c) {
        super(c);
        LayoutInflater.from(c).inflate(R.layout.overlay, this, true);
        CheckBox esp = findViewById(R.id.cb_esp);
        CheckBox aim = findViewById(R.id.cb_aim);
        SeekBar fov = findViewById(R.id.sb_fov);
        esp.setChecked(ESP_ENABLED);
        aim.setChecked(AIM_ENABLED);
        fov.setProgress((int) FOV_DEGREES);
        esp.setOnCheckedChangeListener((v, b) -> ESP_ENABLED = b);
        aim.setOnCheckedChangeListener((v, b) -> AIM_ENABLED = b);
        fov.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean u) { FOV_DEGREES = p; }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
    }
}

package com.ffcheat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_REQ = 1001;
    private static final int SHIZUKU_REQ = 2001;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.overlay);

        Button btn = findViewById(R.id.btn_start);
        btn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), OVERLAY_REQ);
                Toast.makeText(this, "Conceda permissao de sobreposicao", Toast.LENGTH_LONG).show();
            } else {
                ShizukuBridge.request(this, SHIZUKU_REQ);
                launchService();
            }
        });
    }

    @Override
    protected void onActivityResult(int req, int res, Intent d) {
        super.onActivityResult(req, res, d);
        if (req == OVERLAY_REQ && Settings.canDrawOverlays(this)) {
            ShizukuBridge.request(this, SHIZUKU_REQ);
            launchService();
        }
    }

    private void launchService() {
        Intent i = new Intent(this, OverlayService.class);
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(i); else startService(i);
        finish();
    }
}

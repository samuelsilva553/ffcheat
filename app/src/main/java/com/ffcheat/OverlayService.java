package com.ffcheat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class OverlayService extends Service {
    private static final String PKG_FF = "com.dts.freefireth";
    private static final String PKG_FF_BR = "com.dts.freefiremax";
    private static final String CHAN = "ffcheat";

    private WindowManager wm;
    private EspRenderer esp;
    private GameMenuView menu;
    private Thread worker;
    private volatile boolean running = true;

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundCompat();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        esp = new EspRenderer(this);
        menu = new GameMenuView(this);

        Button btnStart = menu.findViewById(R.id.btn_start);
        if (btnStart != null) {
            btnStart.setOnClickListener(v -> {
                ShizukuBridge.launchApp(PKG_FF);
                ShizukuBridge.launchApp(PKG_FF_BR);
                Toast.makeText(this, "Abrindo Free Fire...", Toast.LENGTH_SHORT).show();
            });
        }

        WindowManager.LayoutParams espParams = buildParams(true);
        WindowManager.LayoutParams menuParams = buildParams(false);
        menuParams.gravity = Gravity.TOP | Gravity.START;
        menuParams.x = 20;
        menuParams.y = 100;

        wm.addView(esp, espParams);
        wm.addView(menu, menuParams);

        worker = new Thread(this::loop);
        worker.start();
    }

    private WindowManager.LayoutParams buildParams(boolean fullscreen) {
        int type = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                  | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                  | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        if (fullscreen) flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        return new WindowManager.LayoutParams(
            fullscreen ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT,
            fullscreen ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT,
            type, flags, PixelFormat.TRANSLUCENT);
    }

    private void loop() {
        int pid = -1;
        MemoryEngine mem = null;
        while (running) {
            try {
                if (pid == -1) {
                    pid = ShizukuBridge.getPid(PKG_FF);
                    if (pid == -1) pid = ShizukuBridge.getPid(PKG_FF_BR);
                    if (pid != -1) {
                        mem = new MemoryEngine(pid);
                    }
                }
                if (pid != -1 && mem != null) {
                    // TODO: usar mem com offsets preenchidos em MemoryEngine
                    // monta float[][] boxes e chama esp.setBoxes
                    esp.setFov(GameMenuView.FOV_DEGREES *
                        (getResources().getDisplayMetrics().widthPixels / 180f));
                }
            } catch (Throwable ignored) {}
            try { Thread.sleep(16); } catch (InterruptedException e) { break; }
        }
    }

    private void startForegroundCompat() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = getSystemService(NotificationManager.class);
            NotificationChannel ch = new NotificationChannel(CHAN, "FF", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(ch);
        }
        Notification.Builder nb = Build.VERSION.SDK_INT >= 26
            ? new Notification.Builder(this, CHAN)
            : new Notification.Builder(this);
        nb.setSmallIcon(android.R.drawable.ic_menu_compass)
          .setContentTitle("FF Menu ativo")
          .setContentText("ESP/aim rodando");
        startForeground(1, nb.build());
    }

    @Override
    public void onDestroy() {
        running = false;
        if (esp != null) wm.removeView(esp);
        if (menu != null) wm.removeView(menu);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent i) { return null; }
}

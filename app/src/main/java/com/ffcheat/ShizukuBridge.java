package com.ffcheat;

import android.app.Activity;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShizukuBridge {
    public static boolean isReady() {
        try {
            return rikka.shizuku.Shizuku.pingBinder()
                && rikka.shizuku.Shizuku.checkSelfPermission() == 0;
        } catch (Throwable t) { return false; }
    }

    public static void request(Activity act, int req) {
        try {
            if (!rikka.shizuku.Shizuku.pingBinder()) return;
            if (rikka.shizuku.Shizuku.checkSelfPermission() != 0) {
                rikka.shizuku.Shizuku.requestPermission(req);
            }
        } catch (Throwable ignored) {}
    }

    public static int getPid(String pkg) {
        String out = shell("pidof " + pkg).trim();
        if (out.isEmpty()) return -1;
        try { return Integer.parseInt(out.split("\\s+")[0]); }
        catch (NumberFormatException e) { return -1; }
    }

    public static void launchApp(String pkg) {
        shell("monkey -p " + pkg + " -c android.intent.category.LAUNCHER 1");
    }

    public static String shell(String cmd) {
        try {
            Process p = rikka.shizuku.Shizuku.newProcess(
                new String[]{"sh", "-c", cmd}, null, null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            p.waitFor();
            return sb.toString();
        } catch (Throwable t) { return ""; }
    }

    public static byte[] readMem(int pid, long addr, int len) {
        String cmd = String.format(
            "dd if=/proc/%d/mem bs=1 skip=%d count=%d 2>/dev/null | base64 -w0",
            pid, addr, len);
        String b64 = shell(cmd).trim();
        if (b64.isEmpty()) return null;
        return android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
    }
}

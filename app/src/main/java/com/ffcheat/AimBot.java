package com.ffcheat;

public class AimBot {
    public static class Target {
        public float headX, headY, headZ;
        public float dist;
    }

    public static float angleDelta(float from, float to) {
        float d = to - from;
        while (d > 180) d -= 360;
        while (d < -180) d += 360;
        return d;
    }

    public static float[] calcAngle(float lx, float ly, float lz, float tx, float ty, float tz) {
        float dx = tx - lx, dy = ty - ly, dz = tz - lz;
        float hyp = (float) Math.sqrt(dx*dx + dy*dy);
        float pitch = (float)(-Math.atan2(dz, hyp) * (180.0 / Math.PI));
        float yaw   = (float)( Math.atan2(dy, dx) * (180.0 / Math.PI));
        return new float[]{pitch, yaw};
    }

    public static Target pick(float lx, float ly, float lz,
                              float curPitch, float curYaw,
                              Target[] candidates, float fovHalf) {
        Target best = null;
        float bestDelta = fovHalf;
        for (Target t : candidates) {
            if (t == null) continue;
            float[] ang = calcAngle(lx, ly, lz, t.headX, t.headY, t.headZ);
            float dp = Math.abs(angleDelta(curPitch, ang[0]));
            float dy = Math.abs(angleDelta(curYaw,   ang[1]));
            float delta = Math.max(dp, dy);
            if (delta < bestDelta) {
                bestDelta = delta;
                best = t;
            }
        }
        return best;
    }

    public static float smooth(float current, float target, float factor) {
        return current + angleDelta(current, target) * factor;
    }
}

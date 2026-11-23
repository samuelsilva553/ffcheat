package com.ffcheat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class EspRenderer extends View {
    private final Paint boxPaint = new Paint();
    private final Paint fovPaint = new Paint();
    private final Paint textPaint = new Paint();
    private float fovRadius = 200f;
    private float[][] boxes = new float[0][];
    private String[] names = new String[0];

    public EspRenderer(Context c) {
        super(c);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(3f);

        fovPaint.setColor(Color.argb(120, 0, 255, 100));
        fovPaint.setStyle(Paint.Style.STROKE);
        fovPaint.setStrokeWidth(2f);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28f);
        setWillNotDraw(false);
    }

    public void setFov(float r) { fovRadius = r; invalidate(); }

    public void setBoxes(float[][] b, String[] n) {
        this.boxes = b; this.names = n;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        float cx = getWidth() / 2f, cy = getHeight() / 2f;
        c.drawCircle(cx, cy, fovRadius, fovPaint);
        for (int i = 0; i < boxes.length; i++) {
            float[] b = boxes[i];
            c.drawRect(b[0], b[1], b[2], b[3], boxPaint);
            if (names.length > i && names[i] != null) {
                c.drawText(names[i], b[0], b[1] - 6, textPaint);
            }
        }
    }
}

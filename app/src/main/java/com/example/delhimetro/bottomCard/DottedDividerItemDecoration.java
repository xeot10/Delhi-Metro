package com.example.delhimetro.bottomCard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DottedDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint;
    private final int dividerHeight;

    public DottedDividerItemDecoration(Context context, int dividerHeightDimenId, int dividerColor) {
        this.dividerHeight = context.getResources().getDimensionPixelSize(dividerHeightDimenId);
        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStrokeWidth(dividerHeight);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0)); // Adjust the dash pattern as needed
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            int bottom = top + dividerHeight;

            Path path = new Path();
            path.moveTo(left, top + dividerHeight / 2);
            path.lineTo(right, top + dividerHeight / 2);

            c.drawPath(path, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position < state.getItemCount() - 1) {
            outRect.set(0, 0, 0, dividerHeight);
        } else {
            outRect.set(0, 0, 0, 0); // No divider after the last item
        }
    }


}


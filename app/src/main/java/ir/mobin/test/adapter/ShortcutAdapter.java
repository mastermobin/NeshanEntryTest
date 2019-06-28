package ir.mobin.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.mobin.test.model.Shortcut;
import ir.mobin.test.R;

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.ViewHolder> {

    private List<Shortcut> data;
    private Context context;
    private int size = 4;

    public ShortcutAdapter(List<Shortcut> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shortcut, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i == getItemCount() - 1) {
            viewHolder.setPadding(false);
            if (getItemCount() == 4) {
                viewHolder.tvType.setText("بیشتر");
                viewHolder.ivType.setImageResource(R.drawable.ic_down_circle);
                viewHolder.ivType.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.clickBehave = 1;
            } else {
                viewHolder.tvType.setText("کمتر");
                viewHolder.ivType.setImageResource(R.drawable.ic_up_circle);
                viewHolder.ivType.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.clickBehave = 2;
            }
        } else {
            viewHolder.clickBehave = 0;
            viewHolder.setPadding(true);
            viewHolder.tvType.setText(data.get(i).getType());
            viewHolder.ivType.setImageResource(data.get(i).getImageRes());
            viewHolder.ivType.setBackground(new BitmapDrawable(context.getResources(), getCircle(i)));
        }
    }

    private Bitmap getCircle(int i) {
        Bitmap output = Bitmap.createBitmap(128,
                128, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = ContextCompat.getColor(context, data.get(i).getBgColor());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(64, 64, 64, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);
        return output;
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : size;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivType;
        TextView tvType;
        ConstraintLayout clShortcut;

        int clickBehave = 0;
        int padding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivType = itemView.findViewById(R.id.ivType);
            tvType = itemView.findViewById(R.id.tvType);
            clShortcut = itemView.findViewById(R.id.clShortcut);

            padding = ivType.getPaddingRight();

            ivType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickBehave == 0) {

                    } else if (clickBehave == 1) {
                        size = 12;
                        notifyDataSetChanged();
                    } else {
                        size = 4;
                        notifyDataSetChanged();
                    }
                }
            });

            ivType.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    clShortcut.onTouchEvent(motionEvent);
                    return false;
                }
            });
        }

        void setPadding(boolean def) {
            if (def)
                ivType.setPadding(padding, padding, padding, padding);
            else
                ivType.setPadding(0, 0, 0, 0);
        }
    }
}

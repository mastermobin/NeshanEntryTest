package ir.mobin.test.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.neshan.core.LngLat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.mobin.test.model.Result;
import ir.mobin.test.R;
import ir.mobin.test.utils.GeoUtils;
import ir.mobin.test.utils.Utils;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Result> data = new ArrayList<>();
    private LngLat focal;
    private ItemClickListener itemClickListener;

    public void setData(List<Result> data, LngLat focal, ItemClickListener itemClickListener) {
        this.data = data;
        this.focal = focal;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_result, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Result res = data.get(i);
        viewHolder.tvTitle.setText(res.getTitle());
        viewHolder.tvType.setText(res.getType());
        viewHolder.tvAddress.setText(res.getAddress());
        String dist = String.valueOf(new DecimalFormat("#.##").format(GeoUtils.distance(focal, new LngLat(res.getLocation().getX(), res.getLocation().getY()), 0, 0)));
        viewHolder.tvDistance.setText(Utils.persianDigits(dist));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAddress;
        TextView tvType;
        TextView tvDistance;
        ImageView ivType;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvType = itemView.findViewById(R.id.tvType);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            ivType = itemView.findViewById(R.id.ivType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(data.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ItemClickListener{
        void onItemClick(Result result);
    }
}

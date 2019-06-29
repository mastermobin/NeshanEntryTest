package ir.mobin.test.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.mobin.test.R;
import ir.mobin.test.model.Place;
import ir.mobin.test.model.RecentSearch;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    private List<RecentSearch> data;
    private SearchAdapter.PlaceListener placeListener;

    public RecentSearchAdapter(SearchAdapter.PlaceListener placeListener) {
        this.placeListener = placeListener;
    }

    public void setData(List<RecentSearch> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.clItem.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#F7F8FA") : Color.WHITE);
        holder.tvTitle.setText(data.get(position).getTitle());
        holder.tvAddress.setText(data.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvAddress;
        ConstraintLayout clItem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            clItem = itemView.findViewById(R.id.clItem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            RecentSearch recentSearch = data.get(getAdapterPosition());
            placeListener.onPlaceSelection(new Place(recentSearch.getTitle(), recentSearch.getAddress(), recentSearch.getLocation()));
        }
    }


}

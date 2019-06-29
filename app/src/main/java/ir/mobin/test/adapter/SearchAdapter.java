package ir.mobin.test.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.neshan.core.LngLat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.mobin.test.model.Place;
import ir.mobin.test.model.Result;
import ir.mobin.test.R;
import ir.mobin.test.utils.GeoUtils;
import ir.mobin.test.utils.Utils;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static final Map<String, Integer> IMAGE_DRAWABLES = new HashMap<>();
    static {
        IMAGE_DRAWABLES.put("restaurant", R.drawable.ic_restaurant);
        IMAGE_DRAWABLES.put("gas_station", R.drawable.ic_gas_station);
        IMAGE_DRAWABLES.put("wc", R.drawable.ic_wc);
        IMAGE_DRAWABLES.put("road_helal_ahmar", R.drawable.ic_helal_ahmar);
        IMAGE_DRAWABLES.put("parking", R.drawable.ic_parking);
        IMAGE_DRAWABLES.put("mosque", R.drawable.ic_mosque);
        IMAGE_DRAWABLES.put("atm", R.drawable.ic_atm);
        IMAGE_DRAWABLES.put("fast_food", R.drawable.ic_fastfood);
        IMAGE_DRAWABLES.put("shopping_mall", R.drawable.ic_store);
        IMAGE_DRAWABLES.put("pharmacy", R.drawable.ic_pharmacy);
        IMAGE_DRAWABLES.put("cafe", R.drawable.ic_cafe);
        IMAGE_DRAWABLES.put("hospital", R.drawable.ic_hospital);
        IMAGE_DRAWABLES.put("car_wash", R.drawable.ic_car_wash);
        IMAGE_DRAWABLES.put("hotel", R.drawable.ic_hotel);
        IMAGE_DRAWABLES.put("misc", R.drawable.ic_misc);
    }

    private List<Result> data = new ArrayList<>();
    private LngLat focal;
    private PlaceListener placeListener;

    public void setData(List<Result> data, LngLat focal, PlaceListener placeListener) {
        this.data = data;
        this.focal = focal;
        this.placeListener = placeListener;
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
        viewHolder.tvType.setText(Utils.translateTypes(res.getType()));
        viewHolder.tvAddress.setText(res.getAddress());
        if(IMAGE_DRAWABLES.containsKey(res.getType())) {
            viewHolder.ivType.setImageResource(IMAGE_DRAWABLES.get(res.getType()));
        }
        String dist = String.valueOf(new DecimalFormat("#.#").format(GeoUtils.distance(focal, new LngLat(res.getLocation().getX(), res.getLocation().getY()), 0, 0)));
        viewHolder.tvDistance.setText(Utils.persianDigits(dist) + "\n" + "کیلومتر");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear(){
        data.clear();
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

            ivType.setColorFilter(Color.RED);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Result res = data.get(getAdapterPosition());
                    placeListener.onPlaceSelection(new Place(res.getTitle(), res.getAddress(), new LngLat(res.getLocation().getX(), res.getLocation().getY())));
                }
            });
        }
    }

    public interface PlaceListener {
        void onPlaceSelection(Place result);
    }
}

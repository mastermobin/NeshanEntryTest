package ir.mobin.test.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.layers.VectorElementLayer;
import org.neshan.ui.ClickData;
import org.neshan.ui.MapEventListener;
import org.neshan.vectorelements.Marker;

import ir.mobin.test.R;
import ir.mobin.test.helper.WebServiceHelper;
import ir.mobin.test.interfaces.MapListener;
import ir.mobin.test.utils.DrawUtils;
import ir.mobin.test.utils.MarkerAnimation;
import ir.mobin.test.utils.Utils;

public class SimulationFragment extends Fragment implements MapListener, View.OnClickListener {

    private Button btnPlaceA, btnPlaceB;
    private TextView tvSpeed;
    private SeekBar sbSpeed;
    private WebServiceHelper webServiceHelper;
    private VectorElementLayer lineLayer, markerLayer;

    private Marker marker;
    private MarkerAnimation markerAnimation;
    private int selectionNumber = -1;
    private LngLat placeA, placeB;
    private int speed = 30;

    private int colorA = R.color.normal, colorB = R.color.normal;

    public static SimulationFragment newInstance(VectorElementLayer lineLayer, VectorElementLayer markerLayer) {

        Bundle args = new Bundle();

        SimulationFragment fragment = new SimulationFragment();
        fragment.setLayers(lineLayer, markerLayer);
        fragment.setArguments(args);
        return fragment;
    }

    public void setLayers(VectorElementLayer lineLayer, VectorElementLayer markerLayer) {
        this.lineLayer = lineLayer;
        this.markerLayer = markerLayer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulation, container, false);

        btnPlaceA = view.findViewById(R.id.btnPlaceA);
        btnPlaceB = view.findViewById(R.id.btnPlaceB);
        sbSpeed = view.findViewById(R.id.sbSpeed);
        tvSpeed = view.findViewById(R.id.tvSpeed);

        webServiceHelper = WebServiceHelper.getInstance();

        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = i;
                tvSpeed.setText("سرعت: " + Utils.persianDigits(String.valueOf(i)) + " کیلومتر");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPlaceA.setOnClickListener(this);
        btnPlaceB.setOnClickListener(this);

        return view;
    }

    @Override
    public void onMapClicked(ClickData clickData) {
        if (selectionNumber == 0) {
            colorA = R.color.accept;
            placeA = clickData.getClickPos();
        } else if (selectionNumber == 1) {
            colorB = R.color.accept;
            placeB = clickData.getClickPos();
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnPlaceA.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorA));
                btnPlaceB.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorB));
            }
        });

        selectionNumber = -1;
        if (placeA != null && placeB != null)
            webServiceHelper.getRoute(new WebServiceHelper.RouteListener() {
                @Override
                public void onRouteFound(LngLatVector path) {
                    if (markerAnimation != null) {
                        markerAnimation.kill();
                        markerAnimation.cancel(true);
                    }
                    DrawUtils.drawLine(path, lineLayer);
                    marker = DrawUtils.drawMarker(new LngLat(path.get(0).getX(), path.get(0).getY()), markerLayer, BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker));

                    markerAnimation = new MarkerAnimation(marker, speed);
                    markerAnimation.execute(path);
                }

                @Override
                public void onRoutingFailed(Throwable throwable) {
                    Log.d("NESHANTEST", throwable.getMessage());
                }
            }, placeA, placeB);
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btnPlaceA)
            selectionNumber = 0;
        else if (view.getId() == R.id.btnPlaceB)
            selectionNumber = 1;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnPlaceA.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorA));
                btnPlaceB.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorB));
                view.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.wait));
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (markerAnimation != null) {
            markerAnimation.kill();
            markerAnimation.cancel(true);
            lineLayer.clear();
            markerLayer.clear();
        }
        super.onDestroyView();
    }
}

package ir.mobin.test.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.neshan.core.LngLat;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.ui.ClickData;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;

import ir.mobin.test.R;
import ir.mobin.test.fragment.SearchFragment;
import ir.mobin.test.fragment.SimulationFragment;
import ir.mobin.test.interfaces.MapListener;
import ir.mobin.test.utils.DrawUtils;

public class MainActivity extends AppCompatActivity implements SearchFragment.SearchListener {

    private MapView map;
    private TextView tvSerach;

    private VectorElementLayer lineLayer, markerLayer;
    private Fragment lastFrag;

    private boolean onFrag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        tvSerach = findViewById(R.id.tvSearch);

        lineLayer = NeshanServices.createVectorElementLayer();
        markerLayer = NeshanServices.createVectorElementLayer();

        LngLat focalPoint = new LngLat(59.488330, 36.349987);
        map.setFocalPointPosition(focalPoint, 0f);
        map.setZoom(14f, 1);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        map.getLayers().add(lineLayer);
        map.getLayers().add(markerLayer);

        map.setMapEventListener(new MapEventListener() {
            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                if (lastFrag != null && lastFrag instanceof MapListener) {
                    ((MapListener) lastFrag).onMapClicked(mapClickInfo);
                }
            }
        });

        tvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastFrag == null || !(lastFrag instanceof SearchFragment))
                    showFragment(SearchFragment.newInstance(map.getFocalPointPosition(), MainActivity.this, new SearchFragment.SearchActions() {
                        @Override
                        public void onSimulate() {
                            showFragment(SimulationFragment.newInstance(lineLayer, markerLayer), R.id.container, 1);
                        }
                    }), R.id.container, 0);
            }
        });
    }

    void showFragment(Fragment fragment, int layout, int anim) {
        if (onFrag)
            onBackPressed();
        lastFrag = fragment;
        onFrag = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        if(anim == 0)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_down);
        else
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up);

        fragmentTransaction.add(layout, fragment).commit();
    }

    @Override
    public void onSelect(String title, LngLat pos) {
        onBackPressed();
        map.setFocalPointPosition(pos, 0);
        map.setZoom(16f, 1);
        lineLayer.clear();
        DrawUtils.drawMarker(pos, markerLayer, BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(lastFrag != null){
//            lastFrag.des
//        }
        onFrag = false;
        lastFrag = null;
    }
}

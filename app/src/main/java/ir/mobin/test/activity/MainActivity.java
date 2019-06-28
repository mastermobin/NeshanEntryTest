package ir.mobin.test.activity;

import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.ui.ClickData;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;
import org.neshan.vectorelements.Marker;

import ir.mobin.test.R;
import ir.mobin.test.fragment.SearchFragment;
import ir.mobin.test.utils.DrawUtils;
import ir.mobin.test.utils.MarkerAnimation;
import ir.mobin.test.helper.WebServiceHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchFragment.SearchListener {

    private MapView map;
    private Button btnPlaceA, btnPlaceB;
    private TextView tvSpeed, tvSerach;
    private SeekBar sbSpeed;

    private WebServiceHelper webServiceHelper;
    private Marker marker;
    private VectorElementLayer lineLayer, markerLayer;
    private MarkerAnimation markerAnimation;

    private int selectionNumber = -1;
    private LngLat placeA, placeB;
    private int speed = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        btnPlaceA = findViewById(R.id.btnPlaceA);
        btnPlaceB = findViewById(R.id.btnPlaceB);
        sbSpeed = findViewById(R.id.sbSpeed);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvSerach = findViewById(R.id.tvSearch);

        webServiceHelper = WebServiceHelper.getInstance();

        lineLayer = NeshanServices.createVectorElementLayer();
        markerLayer = NeshanServices.createVectorElementLayer();

        LngLat focalPoint = new LngLat(59.488330, 36.349987);
        map.setFocalPointPosition(focalPoint, 0f);
        map.setZoom(14f, 1);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        map.getLayers().add(lineLayer);
        map.getLayers().add(markerLayer);

        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = i;
                tvSpeed.setText("Speed: " + speed + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        map.setMapEventListener(new MapEventListener() {
            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                if (selectionNumber == 0) {
                    placeA = mapClickInfo.getClickPos();
                } else if (selectionNumber == 1) {
                    placeB = mapClickInfo.getClickPos();
                }

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
        });

        btnPlaceA.setOnClickListener(this);
        btnPlaceB.setOnClickListener(this);

        tvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment();
            }
        });
    }

    void showFragment() {
        SearchFragment searchFragment = SearchFragment.newInstance(map.getFocalPointPosition(), this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.exit_anim, R.anim.enter_anim, R.anim.exit_anim, R.anim.enter_anim);
        fragmentTransaction.add(R.id.container, searchFragment).commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPlaceA)
            selectionNumber = 0;
        else if (view.getId() == R.id.btnPlaceB)
            selectionNumber = 1;
    }

    @Override
    public void onSelect(String title, LngLat pos) {
        onBackPressed();
        if (markerAnimation != null) {
            markerAnimation.kill();
            markerAnimation.cancel(true);
        }
        map.setFocalPointPosition(pos, 1f);
        map.setZoom(14f, 1);
        marker = DrawUtils.drawMarker(pos, markerLayer, BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker));
    }
}

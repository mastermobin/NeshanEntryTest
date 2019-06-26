package ir.mobin.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.geometry.LineGeom;
import org.neshan.graphics.ARGB;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.LineStyleCreator;
import org.neshan.ui.ClickData;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;
import org.neshan.vectorelements.Line;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView map;
    private Button btnPlaceA, btnPlaceB;
    private CardView topCard;
    private int selectionNumber = -1;
    private LngLat placeA, placeB;
    private VectorElementLayer myLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        btnPlaceA = findViewById(R.id.btnPlaceA);
        btnPlaceB = findViewById(R.id.btnPlaceB);
        topCard = findViewById(R.id.topCard);
        myLayer = NeshanServices.createVectorElementLayer();

        LngLat focalPoint = new LngLat(51.33800,35.69997);
        map.setFocalPointPosition(focalPoint, 0f);
        map.setZoom(14f, 1);
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        map.getLayers().add(myLayer);

        map.setMapEventListener(new MapEventListener(){
            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                if(selectionNumber == 0) {
                    placeA = mapClickInfo.getClickPos();
                }else if(selectionNumber == 1) {
                    placeB = mapClickInfo.getClickPos();
                }

                selectionNumber = -1;
                if(placeA != null && placeB != null)
                    drawLine();
            }
        });

        btnPlaceA.setOnClickListener(this);
        btnPlaceB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnPlaceA) {
            selectionNumber = 0;
        }else if(view.getId() == R.id.btnPlaceB)
            selectionNumber = 1;

    }

    private void drawLine() {
        myLayer.clear();
        LngLatVector lngLatVector = new LngLatVector();
        lngLatVector.add(placeA);
        lngLatVector.add(placeB);
        LineGeom lineGeom = new LineGeom(lngLatVector);

        LineStyleCreator lineStCr = new LineStyleCreator();
        lineStCr.setColor(new ARGB((short) 2, (short) 119, (short) 189, (short) 190));
        lineStCr.setWidth(12f);
        lineStCr.setStretchFactor(0f);

        Line line = new Line(lineGeom, lineStCr.buildStyle());
        myLayer.add(line);
    }
}

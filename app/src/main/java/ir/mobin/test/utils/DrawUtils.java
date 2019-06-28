package ir.mobin.test.utils;

import android.graphics.Bitmap;

import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.geometry.LineGeom;
import org.neshan.graphics.ARGB;
import org.neshan.layers.VectorElementLayer;
import org.neshan.styles.AnimationStyleBuilder;
import org.neshan.styles.AnimationType;
import org.neshan.styles.LineStyleCreator;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Line;
import org.neshan.vectorelements.Marker;

public class DrawUtils {
    public static Marker drawMarker(LngLat loc, VectorElementLayer layer, Bitmap bitmap) {
        layer.clear();
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);

        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(bitmap));
        markStCr.setAnimationStyle(animStBl.buildStyle());
        MarkerStyle markSt = markStCr.buildStyle();

        Marker marker = new Marker(loc, markSt);
        layer.add(marker);
        return marker;
    }

    public static void drawLine(LngLatVector path, VectorElementLayer layer) {
        layer.clear();

        LineStyleCreator lineStCr = new LineStyleCreator();
        lineStCr.setColor(new ARGB((short) 2, (short) 119, (short) 189, (short) 190));
        lineStCr.setWidth(5f);
        lineStCr.setStretchFactor(0f);

        LineGeom lineGeom = new LineGeom(path);
        Line line = new Line(lineGeom, lineStCr.buildStyle());
        layer.add(line);
    }
}

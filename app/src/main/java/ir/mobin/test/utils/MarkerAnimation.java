package ir.mobin.test.utils;

import android.os.AsyncTask;

import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.vectorelements.Marker;

import java.util.Timer;
import java.util.TimerTask;

public class MarkerAnimation extends AsyncTask<LngLatVector, Integer, Void> {

    private Marker marker;
    private Timer timer;
    private TimerTask timerTask;

    private boolean kill = false;
    private double X, Y, deltaX, deltaY;
    private int speed;

    public MarkerAnimation(Marker marker, int speed) {
        this.marker = marker;
        this.speed = speed;
        timer = new Timer();
    }

    public void kill() {
        kill = true;
    }

    @Override
    protected Void doInBackground(LngLatVector... lngLatVectors) {
        kill = false;

        for (int i = 1; i < lngLatVectors[0].size(); i++) {
            X = lngLatVectors[0].get(i - 1).getX();
            Y = lngLatVectors[0].get(i - 1).getY();
            marker.setPos(new LngLat(X, Y));

            deltaX = lngLatVectors[0].get(i).getX() - lngLatVectors[0].get(i - 1).getX();
            deltaY = lngLatVectors[0].get(i).getY() - lngLatVectors[0].get(i - 1).getY();

            double mag = GeoUtils.distance(lngLatVectors[0].get(i), lngLatVectors[0].get(i - 1), 0, 0);
            int time = (int) ((mag / speed) * 60 * 60 * 50);
            deltaX /= time;
            deltaY /= time;

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    X += deltaX;
                    Y += deltaY;
                    MarkerAnimation.this.marker.setPos(new LngLat(X, Y));
                }
            };
            timer.scheduleAtFixedRate(timerTask, 20, 20);

            try {
                Thread.sleep(time * 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timerTask.cancel();
            if (kill) {
                kill = false;
                return null;
            }
        }
        return null;
    }

}
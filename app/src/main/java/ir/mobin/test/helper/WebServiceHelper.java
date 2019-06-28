package ir.mobin.test.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;

import java.util.ArrayList;
import java.util.List;

import ir.mobin.test.model.Result;
import ir.mobin.test.model.SearchQuery;
import ir.mobin.test.utils.PolylineEncoding;
import ir.mobin.test.network.ServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceHelper {

    private static WebServiceHelper webServiceHelper;

    public static synchronized WebServiceHelper getInstance() {
        if (webServiceHelper == null) {
            webServiceHelper = new WebServiceHelper();
        }
        return webServiceHelper;
    }

    private final ServiceInterface serviceInterface;

    private WebServiceHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neshan.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);
    }

    public void getRoute(final RouteListener routeListener, LngLat a, LngLat b) {
        Call<JsonObject> call = serviceInterface.getRoute(a.getY() + "," + a.getX(), b.getY() + "," + b.getX());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String overviewPolyline = response.body().getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonObject("overview_polyline").get("points").getAsString();
                    JsonArray steps = response.body().getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonArray("legs").get(0).getAsJsonObject().getAsJsonArray("steps");

                    List<PolylineEncoding.LatLng> overviewPath = PolylineEncoding.decode(overviewPolyline);
                    List<PolylineEncoding.LatLng> path = new ArrayList<>();

                    for (int i = 0; i < steps.size(); i++) {
                        String stepPolyline = steps.get(i).getAsJsonObject().get("polyline").getAsString();
                        path.addAll(PolylineEncoding.decode(stepPolyline));
                    }

                    LngLatVector lngLatVector = new LngLatVector();
                    for (PolylineEncoding.LatLng point : path) {
                        lngLatVector.add(new LngLat(point.lng, point.lat));
                    }

                    routeListener.onRouteFound(lngLatVector);
                } catch (NullPointerException npe) {
                    routeListener.onRoutingFailed(npe);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                routeListener.onRoutingFailed(t);
            }
        });
    }

    public void search(String term, LngLat focus, final SearchListener searchListener) {
        final Call<SearchQuery> request = serviceInterface.search(term, String.valueOf(focus.getY()), String.valueOf(focus.getX()));
        request.enqueue(new Callback<SearchQuery>() {
            @Override
            public void onResponse(Call<SearchQuery> call, Response<SearchQuery> response) {
                try {
                    searchListener.onSearchResult(response.body().getItems());
                }catch (NullPointerException npe){
                    searchListener.onSearchFailed(npe);
                }
            }

            @Override
            public void onFailure(Call<SearchQuery> call, Throwable t) {
                searchListener.onSearchFailed(t);
            }
        });
    }

    public interface RouteListener {
        void onRouteFound(LngLatVector path);

        void onRoutingFailed(Throwable throwable);
    }

    public interface SearchListener {
        void onSearchResult(List<Result> results);

        void onSearchFailed(Throwable throwable);
    }
}

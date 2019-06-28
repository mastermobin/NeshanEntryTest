package ir.mobin.test.network;

import com.google.gson.JsonObject;

import ir.mobin.test.model.SearchQuery;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ServiceInterface {
    @Headers("Api-Key: service.3AycCdVOytL1PTyMXMC1czaDdHlzNvNQ5m3IzYw7")
    @GET("/v2/direction")
    Call<JsonObject> getRoute(@Query("origin") String origin, @Query("destination") String dest);

    @Headers("Api-Key: service.pHziwKSqvCUEfGSGwTOA2cqFsfHP3VXsDwL9hDAN")
    @GET("/v1/search")
    Call<SearchQuery> search(@Query("term") String term, @Query("lat") String lat, @Query("lng") String lng);

}

package com.example.end_sem_lab1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("latest")
    Call<ExchangeResponse> getExchangeRates(@Query("base") String baseCurrency, @Query("b01900fb7b16dab7fd4dd421778138c3") String apiKey);
}

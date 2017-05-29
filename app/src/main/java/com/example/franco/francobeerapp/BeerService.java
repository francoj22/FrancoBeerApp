package com.example.franco.francobeerapp;

import com.example.franco.francobeerapp.model.Beer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Franco on 29/05/2017.
 */

interface BeerService {


    @Headers("Content-Type: application/json")
    @GET("beer/random")
    Call<Beer> getRandomBeer();
}


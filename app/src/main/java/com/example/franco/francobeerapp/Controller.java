package com.example.franco.francobeerapp;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franco.francobeerapp.model.Beer;
import com.example.franco.francobeerapp.model.Labels;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.franco.francobeerapp.Constants.API_KEY;
import static com.example.franco.francobeerapp.Constants.API_URL;

public class Controller {
    View view;
    TextView titleBeer;
    ImageView imageView;
    TextView descriptionBeer;
    Context context;


    Controller(View view, TextView titleBeer,  ImageView imageView, TextView descriptionBeer, Context context) {
        this.view = view;
        this.titleBeer = titleBeer;
        this.imageView = imageView;
        this.descriptionBeer = descriptionBeer;
        this.context = context;
    }
    public void start() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder()
                    .url(original.url() + "?key=" + API_KEY)
                    .method(original.method(), original.body());
            final Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        BeerService beerService = retrofit.create(BeerService.class);

        Call<Beer> call = beerService.getRandomBeer();

        call.enqueue(new Callback<Beer>() {
            @Override
            public void onResponse(Call<Beer> call, Response<Beer> response) {
                Beer beer = response.body();
                Labels labels;
                try {
                    labels = beer.getData().getLabels();
                    beer.getData().setName(response.body().getData().getName());
                    beer.getData().setDescription(response.body().getData().getDescription());
                    beer.getData().getLabels().setMedium(response.body().getData().getLabels().getMedium());
                    //setting validation from model classes
                    displayImageFromUrl(context, labels.getMedium());
                    titleBeer.setText(beer.getData().getName());
                    descriptionBeer.setText(response.body().getData().getDescription());
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Some of the values like title, description or even label weren't found try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Beer> call, Throwable t) {
                return;
            }
        });

    }

    private void displayImageFromUrl(Context context, String url) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.non_image)
                .error(R.drawable.non_image)
                .into(imageView);
    }
}
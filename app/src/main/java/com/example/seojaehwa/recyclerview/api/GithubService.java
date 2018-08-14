package com.example.seojaehwa.recyclerview.api;

import com.example.seojaehwa.recyclerview.data.RepoSearchResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {

    GithubService.Companion COMPANION = Companion.INSTANCE;

    @GET("search/repositories?sort=stars")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query,
                                         @Query("page") int page,
                                         @Query("per_page") int itemsPerPage);

    final class Companion {

        private static String BASE_URL = "https://api.github.com/";

        static Companion INSTANCE = new Companion();

        public final GithubService getApi() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
                    )
                    .build()
                    .create(GithubService.class);
        }
    }
}

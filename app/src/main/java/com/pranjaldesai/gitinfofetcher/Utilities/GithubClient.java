package com.pranjaldesai.gitinfofetcher.Utilities;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pranjaldesai.gitinfofetcher.Data.Repo;

import java.util.List;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    This client object uses retrofit and returns service observable object.
 */

public class GithubClient {

    private static final String BASE_URL = "https://api.github.com/";
    private static GithubClient instance;
    private GithubService gitHubService;

    private GithubClient() {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gitHubService = retrofit.create(GithubService.class);
    }

    public static GithubClient getInstance() {
        if (instance == null) {
            instance = new GithubClient();
        }
        return instance;
    }

    public Observable<List<Repo>> getRepositoriesUserName(@NonNull String userName) {
        return gitHubService.getRepositoriesUserName(userName);
    }

    public Observable<List<Repo>> getRepositoriesOrganization(@NonNull String userName) {
        return gitHubService.getRepositoriesOrganization(userName);
    }
}

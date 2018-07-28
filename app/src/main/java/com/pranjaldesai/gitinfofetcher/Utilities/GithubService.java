package com.pranjaldesai.gitinfofetcher.Utilities;

import com.pranjaldesai.gitinfofetcher.Data.Repo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GithubService {

    @GET("users/{userName}/repos")
    Observable<List<Repo>> getRepositoriesUserName(@Path("userName") String userName);

    @GET("orgs/{userName}/repos")
    Observable<List<Repo>> getRepositoriesOrganization(@Path("userName") String userName);
}

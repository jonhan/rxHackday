package com.magine.rxhackday.api;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface CoolService {
	@GET("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q={query}")
	Observable<retrofit.client.Response> search(@Path(value = "query", encode = true) String query);
}

package com.magine.rxhackday.api;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface CoolService {
	@GET("/ajax/services/search/web?v=1.0")
	Observable<Response> search(@Query("q") String query);
}

package com.magine.rxhackday.api;

import retrofit.RestAdapter;

public final class RestService {
	private static RestAdapter sBaseApiAdapter;

	private static RestAdapter getBaseApiAdapter() {

		if (sBaseApiAdapter == null) {
			createRestAdapter();
		}
		return sBaseApiAdapter;
	}

	public static void createRestAdapter() {

		sBaseApiAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://ajax.googleapis.com/").build();
	}

	public static CoolService getCoolService() {
		return getBaseApiAdapter().create(CoolService.class);
	}
}

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

		sBaseApiAdapter = new RestAdapter.Builder().build();
	}

	public static CoolService getCoolService() {
		return sBaseApiAdapter.create(CoolService.class);
	}
}

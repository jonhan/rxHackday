package com.magine.rxhackday.api;

import java.util.List;

public class ResponseData {
	public List<Result> results;

	@Override
	public String toString() {
		return "ResponseData{" +
				"results=" + results +
				'}';
	}
}

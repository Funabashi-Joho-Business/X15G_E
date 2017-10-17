package jp.ac.chiba_fjb.oikawa.routetest;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RouteData{

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Legs{
		public String start_address;
		public jp.ac.chiba_fjb.oikawa.routetest.Location start_location;
		public String end_address;
		public jp.ac.chiba_fjb.oikawa.routetest.Location end_location;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Routes{
		public Legs[] legs;
	}

	public Routes[] routes;
}

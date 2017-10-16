package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RouteData{
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Location{
		public double lat;
		public double lng;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Legs{
		public String start_address;
		public Location start_location;
		public String end_address;
		public Location end_location;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Routes{
		public Legs[] legs;
	}

	public Routes[] routes;
}

package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//リターンデータのサンプル
//https://maps.googleapis.com/maps/api/directions/json?language=ja&origin=%E5%8D%83%E8%91%89&destination=%E6%9D%B1%E4%BA%AC&mode=driving&sensor=false

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

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
		public jp.ac.chiba_fjb.oikawa.routetest.Dis distance;
		public jp.ac.chiba_fjb.oikawa.routetest.Dis duration;

		@JsonIgnoreProperties(ignoreUnknown=true)
			public static class Steps{
			public jp.ac.chiba_fjb.oikawa.routetest.Location end_location;
			public jp.ac.chiba_fjb.oikawa.routetest.Location start_location;
			public jp.ac.chiba_fjb.oikawa.routetest.Points polyline;
			public String html_instructions;
			public jp.ac.chiba_fjb.oikawa.routetest.Dis distance;
			public jp.ac.chiba_fjb.oikawa.routetest.Dis duration;

			public String travel_mode;


		}
		public Steps[] steps;
//	public jp.ac.chiba_fjb.oikawa.routetest.Points polyline;
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Geocoded_waypoints{
			public String geocoder_status;
			public String place_id;
			public String types[];

	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Routes{
		public Legs[] legs;

	}

	public Routes[] routes;

    public Geocoded_waypoints[] waypoints;
}

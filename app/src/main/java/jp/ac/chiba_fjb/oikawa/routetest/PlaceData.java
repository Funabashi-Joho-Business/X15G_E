package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by oikawa on 2017/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlaceData {
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Geometry{
		public Location location;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Results{
		public Geometry geometry;
		public String name;
		public String types[];
	}


	public Results[] results;
}

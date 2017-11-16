package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.maps.model.SnappedPoint;

/**
 * Created by x15g025 on 2017/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RouteRoad {
    public static class SnappedPoints{

    }
    public static class Location{
        double latitude;
        double longitude;
    }
    public SnappedPoints[] snappedPoints;
}

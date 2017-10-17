package jp.ac.chiba_fjb.oikawa.routetest;

import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by oikawa on 2017/10/16.
 */


public class RouteReader {
	public interface  RouteListener{
		void onRoute(RouteData routeData);
	}
	public interface  PlaceListener{
		void onPlace(PlaceData placeData);
	}

	public static boolean recvRoute(String origin, String dest, final RouteListener listener){
		String url = null;
		try {
			String origin2 = URLEncoder.encode(origin, "UTF-8");
			String dest2 = URLEncoder.encode(dest, "UTF-8");

			url = String.format(
				"https://maps.googleapis.com/maps/api/directions/json?language=ja&"+
				"origin=%s&destination=%s&mode=driving&sensor=false",origin2,dest2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		final Handler handler = new Handler();
		final String finalUrl = url;
		new Thread(){
			@Override
			public void run() {
				final RouteData routeData = Json.send(finalUrl,null,RouteData.class);
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onRoute(routeData);
					}
				});
			}
		}.start();
		return true;

	}
	public static boolean recvPlace(String apiKey,String type,LatLng loc,int radius, final PlaceListener listener){
		String url = null;
		try {
			String type2 = URLEncoder.encode(type, "UTF-8");
			url = String.format(
				"https://maps.googleapis.com/maps/api/place/search/json?language=ja&"+
					"key=%s&types=%s&location=%f,%f&radius=%d",
					apiKey,type2,loc.latitude,loc.longitude,radius);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		final Handler handler = new Handler();
		final String finalUrl = url;
		new Thread(){
			@Override
			public void run() {
				final PlaceData placeData = Json.send(finalUrl,null,PlaceData.class);
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onPlace(placeData);
					}
				});
			}
		}.start();
		return true;
	}
}

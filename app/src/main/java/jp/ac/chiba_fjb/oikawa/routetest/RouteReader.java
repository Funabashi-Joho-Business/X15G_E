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
	public interface  Place2Listener{
		void onPlace2(PlaceidData placeidData);
	}


	public static boolean recvRoute(String origin, String dest, final RouteListener listener){
		String url = null;
		try {
			String origin2 = URLEncoder.encode(origin, "UTF-8");
			String dest2 = URLEncoder.encode(dest, "UTF-8");

			url = String.format(
				"https://maps.googleapis.com/maps/api/directions/json?language=ja&"+
				"origin=%s&destination=%s&mode=walking&sensor=false",origin2,dest2);
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

	public static boolean recvPlace2(final String place_id, String apiKey, final Place2Listener listener){
		String url = null;
		try {
			String place_id2 = URLEncoder.encode(place_id, "UTF-8");
			url = String.format(
					"https://maps.googleapis.com/maps/api/place/details/json?"+"placeid=%s&key=%s",
					place_id2,apiKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		final Handler handler = new Handler();
		final String finalUrl = url;
		new Thread(){
			@Override
			public void run() {
				final PlaceidData placeidData = Json.send(finalUrl,null,PlaceidData.class);
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onPlace2(placeidData);
					}
				});

			}
		}.start();
		return true;
	}




}

package jp.ac.chiba_fjb.oikawa.routetest;

import android.os.Handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by oikawa on 2017/10/16.
 */


public class RouteReader {
	public interface  RouteListener{
		void onRoute(RouteData routeData);
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
}

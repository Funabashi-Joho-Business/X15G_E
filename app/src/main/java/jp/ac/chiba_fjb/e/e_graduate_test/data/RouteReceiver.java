package jp.ac.chiba_fjb.e.e_graduate_test.data;

/**
 * Created by oikawa on 2017/10/02.
 */

public class RouteReceiver {
	static public Object getRoute(String origin,String destination,String mode){
		String url = "https://maps.googleapis.com/maps/api/directions/json?&origin="+origin+"&destination="+destination+"&mode="+mode;
		Object o = Json.send(url,null,Object.class);
		return o;
	}
}

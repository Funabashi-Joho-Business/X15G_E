package jp.ac.chiba_fjb.oikawa.routetest;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RouteReader.RouteListener, RouteReader.PlaceListener {

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			                                                      .findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);


	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng sydney = new LatLng(35.7016369, 139.9836126);                //位置設定
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10.0f));   //範囲2.0～21.0(全体～詳細)
		//ルート検索
		RouteReader.recvRoute("JR千葉駅","JR東京駅",this);
	}

	@Override
	public void onRoute(RouteData routeData) {
		//ルート受け取り処理
		if(routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0){
			RouteData.Routes r = routeData.routes[0];
			Location start = r.legs[0].start_location;
			Location end = r.legs[0].end_location;
//月曜聞け
//			Location sw  = r.steps[0].start_location[0];
//			Location ew  = r.steps[0].end_location[0];
//ここから自作
			try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			GeoApiContext context = new GeoApiContext().setApiKey(bundle.getString("com.google.android.geo.API_KEY"));
			DirectionsApi.newRequest(context)
					.mode(TravelMode.WALKING)
					.origin("東京駅")
					.destination("千葉駅");

				PolylineOptions polylineOptions = new PolylineOptions();
				polylineOptions = polylineOptions.add(new LatLng(start.lat, start.lng));
				polylineOptions = polylineOptions.width(15);
				polylineOptions = polylineOptions.color(Color.RED);
				mMap.addPolyline(polylineOptions);

			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}

			mMap.addMarker(new MarkerOptions().position(new LatLng(start.lat, start.lng)).title(r.legs[0].start_address));
			mMap.addMarker(new MarkerOptions().position(new LatLng(end.lat, end.lng)).title(r.legs[0].end_address));

//ここまで
//				mMap.addPolyline(new PolylineOptions().geodesic(true)
//					.add(new LatLng(start.lat, start.lng))
//					.add(new LatLng(ew.lat, ew.lng))
//					.add(new LatLng(sw.lat, sw.lng))
//					.add(new LatLng(end.lat, end.lng))
//			);


			RouteReader.recvPlace("AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo",
				"food",new LatLng(start.lat, start.lng),300,this);
			RouteReader.recvPlace("AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo",
					"food",new LatLng(end.lat, end.lng),300,this);

		}
	}

	@Override
	public void onPlace(PlaceData placeData) {
		for(PlaceData.Results result : placeData.results){
			System.out.println(result.geometry.location.lat+","+result.geometry.location.lng);
			System.out.println(result.name);
			System.out.println(result.types[1]);

			Location loc = result.geometry.location;

			mMap.addMarker(new MarkerOptions().position(new LatLng(loc.lat, loc.lng)).title(result.name));
		}
	}
}

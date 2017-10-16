package jp.ac.chiba_fjb.oikawa.routetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RouteReader.RouteListener {

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
		RouteReader.recvRoute("千葉","東京",this);
	}

	@Override
	public void onRoute(RouteData routeData) {
		//ルート受け取り処理
		if(routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0){
			RouteData.Routes r = routeData.routes[0];
			RouteData.Location start = r.legs[0].start_location;
			RouteData.Location end = r.legs[0].end_location;
			mMap.clear();
			mMap.addMarker(new MarkerOptions().position(new LatLng(start.lat, start.lng)).title(r.legs[0].start_address));
			mMap.addMarker(new MarkerOptions().position(new LatLng(end.lat, end.lng)).title(r.legs[0].end_address));

		}
	}
}

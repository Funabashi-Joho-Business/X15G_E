package jp.ac.chiba_fjb.oikawa.routetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RouteReader.RouteListener, RouteReader.PlaceListener,RouteReader.Place2Listener{

	private RouteRoad routeRoad;
	private Location sloc;
	private Location eloc;
	private GoogleMap mMap;
	private TextView Mname;
	private TextView Mtype;//タイプとってない
	private TextView Mphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Mname = (TextView) findViewById(R.id.name);
		Mtype = (TextView) findViewById(R.id.type);
		Mphone = (TextView) findViewById(R.id.formatted_phone_number);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			                                                      .findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	//https://maps.googleapis.com/maps/api/place/photo?key=AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo&photoreference=CmRZAAAAWdt90mxA_YNLEm4aizE23qz1uEs8mPEXIjxRrsK6rz0UYrCslvFTlnHdhIOp0K2Bbk_1g8HM-2hhU_dNCewfKQCbqduOU-RuLIdSiuYLPZmYxrTGu2raGBTRz5EHa95MEhC3mS7rnDHwsoog__cW44--GhS2lOgjDzxzeC_k1rxJ69tCiLa_gg&maxwidth=400&maxwidth=400
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng sydney = new LatLng(35.7016369, 139.9836126);                //位置設定
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14.0f));   //範囲2.0～21.0(全体～詳細)
		//ルート検索
		RouteReader.recvRoute("船橋駅","船橋情報ビジネス専門学校",this);
	}

	@Override
	public void onRoute(RouteData routeData) {
		//ルート受け取り処理
		if(routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0){
			PolylineOptions line = new PolylineOptions();
			RouteData.Routes r = routeData.routes[0];
			sloc = r.legs[0].start_location;
			eloc = r.legs[0].end_location;
			mMap.addMarker(new MarkerOptions().position(new LatLng(sloc.lat, sloc.lng)).title(r.legs[0].start_address));
			mMap.addMarker(new MarkerOptions().position(new LatLng(eloc.lat, eloc.lng)).title(r.legs[0].end_address));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sloc.lat,sloc.lng),14.0f));
			//String url = null;
			//url = String.format("https://roads.googleapis.com/v1/snapToRoads?path=%f,%f|%f,%f&key=AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo",sloc.lat,sloc.lng,eloc.lat,eloc.lng);
			//routeRoad = Json.send(url,null,RouteRoad.class);

			int i;
			for(i=0;i<r.legs[0].steps.length;i++) {
				sloc = r.legs[0].steps[i].start_location;
				eloc = r.legs[0].steps[i].end_location;
				line.add(new LatLng(sloc.lat,sloc.lng));
				line.add(new LatLng(eloc.lat,eloc.lng));
			}
			eloc = r.legs[0].end_location;
			line.add(new LatLng(eloc.lat,eloc.lng));
			mMap.addPolyline(line);

			RouteReader.recvPlace("AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo",
					"food",new LatLng(sloc.lat, sloc.lng),300,this);
			RouteReader.recvPlace("AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo",
					"food",new LatLng(eloc.lat, eloc.lng),300,this);
		}
	}

	@Override
	public void onPlace(PlaceData placeData) {
		for(PlaceData.Results result : placeData.results){

			System.out.println(result.geometry.location.lat+","+result.geometry.location.lng);
			System.out.println(result.name);

			String place_id = result.place_id;

			if(result.types[0]=="grocery_or_supermarket"||result.types[0]=="food"){

			}
			else {
				RouteReader.recvPlace2(place_id, "AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo", this);
				Location loc = result.geometry.location;
			}
//			mMap.addMarker(new MarkerOptions().position(new LatLng(loc.lat, loc.lng)).title(result.name));
		}
	}

	@Override
	public void onPlace2(PlaceidData placeidData) {
		Mtype.setText(placeidData.result.types[0]);
		if(Mtype.getText().toString().equals("restaurant")){
			Mtype.setText("レストラン");
		}
		else if(Mtype.getText().toString().equals("cafe")){
			Mtype.setText("カフェ");
		}
		else if(Mtype.getText().toString().equals("bar")){
			Mtype.setText("バー");
		}
		else if(Mtype.getText().toString().equals("bakery")){
			Mtype.setText("パン屋");
		}
		else if(Mtype.getText().toString().equals("meal_takeaway")){
			Mtype.setText("テイクアウト形式");
		}
		else{
			Mtype.setText("");
		}

		Mname.setText(placeidData.result.name);
		Mphone.setText(placeidData.result.formatted_phone_number);

		System.out.println(placeidData.result.formatted_phone_number);
		int i;

if(placeidData.result.opening_hours!=null&&placeidData.result.opening_hours.weekday_text!=null && placeidData.result.opening_hours.weekday_text.length>0)
	for(i=0;i<7;i++) {
		System.out.println(placeidData.result.opening_hours.weekday_text[i]);
	}
	}

//	@Override
//	public void onPlace3(RouteRoad routeroad) {
//		PolylineOptions line = new PolylineOptions();
//		int i;
//		line.add(new LatLng(slat,slng));
//
//			for(i=0;i<length;i++) {
//				Location mstart = ;
//				Location mend = r.legs[0].steps[i].start_location;
//
//			}
//		line.add(new LatLng(elat,elng));
//			mMap.addPolyline(line);
//
//
//	}

}
package jp.ac.chiba_fjb.oikawa.routetest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RouteReader.RouteListener, RouteReader.PlaceListener,RouteReader.Place2Listener{

	private Location sloc;
	private Location eloc;
	private GoogleMap mMap;
	private TextView mName;
	private TextView mType;//タイプとってない
	private TextView mPhone;
    private TextView mOpen;
	private List<String> idlist= new ArrayList();
	private Calendar cal;
	private int week;
	private int day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		RouteReader.recvRoute("JR船橋駅","船橋情報ビジネス専門学校",this);
	}

	@Override
	public void onRoute(RouteData routeData) {
		//ルート受け取り処理
		if(routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0){
			PolylineOptions line = new PolylineOptions();
			RouteData.Routes r = routeData.routes[0];
			sloc = r.legs[0].start_location;
			eloc = r.legs[0].end_location;
			mMap.addMarker(new MarkerOptions().position(new LatLng(sloc.lat, sloc.lng)).title(r.legs[0].start_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(eloc.lat, eloc.lng)).title(r.legs[0].end_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sloc.lat,sloc.lng),14.0f));

    		List<List<HashMap<String,String>>> list = new parseJsonpOfDirectionAPI().parse(routeData);
			RouteSearch(list);
			cal = Calendar.getInstance();
			week = cal.get(Calendar.DAY_OF_WEEK);
			switch (week){
				case 1:day=6;
                    break;
				case 2:day=0;
                    break;
				case 3:day=1;
                    break;
				case 4:day=2;
                    break;
				case 5:day=3;
                    break;
				case 6:day=4;
                    break;
				case 7:day=5;
                    break;
			}
		}

		RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
				"food",new LatLng(sloc.lat, sloc.lng),25,this);

		for(int i=0;i<routeData.routes[0].legs[0].steps.length;i++) {
			RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
					"food", new LatLng(routeData.routes[0].legs[0].steps[i].start_location.lat,routeData.routes[0].legs[0].steps[i].start_location.lng), 25, this);
		}

		RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
				"food",new LatLng(eloc.lat, eloc.lng),25,this);
	}

	@Override
	public void onPlace(PlaceData placeData) {
		for(PlaceData.Results result : placeData.results){
			int count=0;
			System.out.println(result.geometry.location.lat+","+result.geometry.location.lng);
			System.out.println(result.name);
			String place_id = result.place_id;
//			System.out.print(result.types);
//     		mType.setText(result.types[0]);
			if(result.types[0].toString().equals("grocery_or_supermarket")||result.types[0].toString().equals("food")||result.types[0].toString().equals("convenience_store")||idlist.contains(result.place_id)==true){

			}
			else {
				RouteReader.recvPlace2(place_id, "AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc", this);
				mMap.addMarker(new MarkerOptions().position(new LatLng(result.geometry.location.lat,result.geometry.location.lng)).title(result.name));
				idlist.add(result.place_id);
			}

		}
	}

	@Override
	public void onPlace2(PlaceidData placeidData) {
		LinearLayout output = (LinearLayout) findViewById(R.id.output);
		LinearLayout layout;

		layout = (LinearLayout)getLayoutInflater().inflate(R.layout.layout1, null);   //レイアウトをその場で生成
		output.addView(layout);
		mName = (TextView) layout.findViewById(R.id.name);
		mPhone = (TextView) layout.findViewById(R.id.formatted_phone_number);
        mOpen = (TextView) layout.findViewById(R.id.opening_hours);
		mName.setText(placeidData.result.name);
		mPhone.setText(placeidData.result.formatted_phone_number);
		if(placeidData.result.opening_hours!=null&&placeidData.result.opening_hours.weekday_text.length>0){
			System.out.println(placeidData.result.opening_hours.weekday_text[day]);
			mOpen.setText(placeidData.result.opening_hours.weekday_text[day]);
		}
		else{
			mOpen.setText("不明");
		}

//		Mtype.setText(placeidData.result.types[0]);
//		if(Mtype.getText().toString().equals("restaurant")){
//			Mtype.setText("レストラン");
//		}
//		else if(Mtype.getText().toString().equals("cafe")){
//			Mtype.setText("カフェ");
//		}
//		else if(Mtype.getText().toString().equals("bar")){
//			Mtype.setText("バー");
//		}
//		else if(Mtype.getText().toString().equals("bakery")){
//			Mtype.setText("パン屋");
//		}
//		else if(Mtype.getText().toString().equals("meal_takeaway")){
//			Mtype.setText("テイクアウト形式");
//		}
//		else{
//			Mtype.setText("");
//		}

//		Mname.setText(placeidData.result.name);
//		Mphone.setText(placeidData.result.formatted_phone_number);
	}

public void RouteSearch(List<List<HashMap<String, String>>> result){
    ArrayList<LatLng> points = null;
    PolylineOptions lineOptions = null;
    MarkerOptions markerOptions = new MarkerOptions();
    if(result.size() != 0){
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = result.get(i);
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            //ポリライン
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.CYAN);
        }
        //描画
        mMap.addPolyline(lineOptions);
    }
}
}
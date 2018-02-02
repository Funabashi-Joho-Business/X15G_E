package jp.ac.chiba_fjb.oikawa.routetest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
//--
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.widget.Toast;
//--

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RouteReader.RouteListener, RouteReader.PlaceListener,RouteReader.Place2Listener, View.OnClickListener,LocationListener, RouteReader.Place3Listener {
	LocationManager locationManager;
	private Location sloc;
	private Location eloc;
	private GoogleMap mMap;
	private TextView mName;
	private TextView mType;
	private TextView mPhone;
	private TextView mOpen;
	private TextView mCount;
	private List<String> idlist = new ArrayList();
	private List<String> namelist = new ArrayList();
	private Calendar cal;
	private int week;
	private int day;
	private int count = 1;
	private LinearLayout output;
	private LinearLayout layout;
	private ImageButton Imagebutton;
	private ImageButton gpsButton;
	private int pCount = 0;
	private Toast t;
	private SeekBar seekBar;
	private TextView textView;
	private EditText editText1;
	private EditText editText2;
	private double distance;
	private double pLat;
	private double pLng;
	private int gps=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
					1000);
		} else {
		}

		Imagebutton = (ImageButton) findViewById(R.id.imageButton);
		Imagebutton.setOnClickListener(this);
		gpsButton = (ImageButton) findViewById(R.id.GPSButton);
		gpsButton.setOnClickListener(this);

		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);
		output = (LinearLayout) findViewById(R.id.output);
		layout = (LinearLayout) findViewById(R.id.layout1);
		textView = (TextView) findViewById(R.id.textView);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setProgress(1);
		seekBar.setMax(4);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
				textView.setText("検索範囲" + progress * 25 + "m");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		mapFragment.getMapAsync(this);
	}

	private void locationStart() {


		// LocationManager インスタンス生成
		locationManager =
				(LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		} else {
			// GPSを設定するように促す
			Intent settingsIntent =
					new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(settingsIntent);

		}

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
			return;
		}
		if (t != null) {
			t.cancel();
		}
		t = Toast.makeText(this, "現在地を取得しています...", Toast.LENGTH_SHORT);
		t.show();
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				1000, 50, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 50, this);
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 1000) {
			// 使用が許可された
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				locationStart();

			} else {
				// それでも拒否された時の対応
				if (t != null) {
					t.cancel();
				}
				t = Toast.makeText(this, "現在地を取得するにはGPS機能を許可してください", Toast.LENGTH_SHORT);
				t.show();
			}
		}
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		if(gps ==0) {
		editText1.setText("" + location.getLatitude() + "," + location.getLongitude());
			mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("現在地"));
			gps = 1;
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
			case LocationProvider.AVAILABLE:
				break;
			case LocationProvider.OUT_OF_SERVICE:
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				break;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
	//https://maps.googleapis.com/maps/api/place/photo?key=AIzaSyDGFuXp-_GWmCe8lMaw-e71V3p19uQkJIo&photoreference=CmRZAAAAWdt90mxA_YNLEm4aizE23qz1uEs8mPEXIjxRrsK6rz0UYrCslvFTlnHdhIOp0K2Bbk_1g8HM-2hhU_dNCewfKQCbqduOU-RuLIdSiuYLPZmYxrTGu2raGBTRz5EHa95MEhC3mS7rnDHwsoog__cW44--GhS2lOgjDzxzeC_k1rxJ69tCiLa_gg&maxwidth=400&maxwidth=400

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng sydney = new LatLng(35.7016369, 139.9836126);                //位置設定
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));   //範囲2.0～21.0(全体～詳細)
	}

	@Override
	public void onRoute(RouteData routeData) {
		//ルート受け取り処理
		if (routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0) {
			PolylineOptions line = new PolylineOptions();
			RouteData.Routes r = routeData.routes[0];
			sloc = r.legs[0].start_location;
			eloc = r.legs[0].end_location;
			mMap.addMarker(new MarkerOptions().position(new LatLng(sloc.lat, sloc.lng)).title(r.legs[0].start_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(eloc.lat, eloc.lng)).title(r.legs[0].end_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sloc.lat, sloc.lng), 14.0f));
			List<List<HashMap<String, String>>> list = new parseJsonpOfDirectionAPI().parse(routeData);
			RouteSearch(list);
			cal = Calendar.getInstance();
			week = cal.get(Calendar.DAY_OF_WEEK);
			switch (week) {
				case 1:
					day = 6;
					break;
				case 2:
					day = 0;
					break;
				case 3:
					day = 1;
					break;
				case 4:
					day = 2;
					break;
				case 5:
					day = 3;
					break;
				case 6:
					day = 4;
					break;
				case 7:
					day = 5;
					break;
			}
		}
		try {
			RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
					"food", new LatLng(sloc.lat, sloc.lng), seekBar.getProgress() * 25, this);
		} catch (Exception e) {
			if (t != null) {
				t.cancel();
			}
			t = Toast.makeText(this, "インターネットに接続されていません", Toast.LENGTH_SHORT);
			t.show();
		}
//		for(int i=0;i<routeData.routes[0].legs[0].steps.length;i++) {
//			RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
//					"food", new LatLng(routeData.routes[0].legs[0].steps[i].start_location.lat,routeData.routes[0].legs[0].steps[i].start_location.lng), 50, this);
//		}
		try {
			RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
					"food", new LatLng(eloc.lat, eloc.lng), seekBar.getProgress() * 25, this);
		} catch (Exception e) {
			if (t != null) {
				t.cancel();
			}
			t = Toast.makeText(this, "インターネットに接続されていません", Toast.LENGTH_SHORT);
			t.show();
		}

	}

	@Override
	public void onPlace(PlaceData placeData) {
		for (PlaceData.Results result : placeData.results) {
			String place_id = result.place_id;
			if (result.types[0].toString().equals("grocery_or_supermarket") || result.types[0].toString().equals("supermarket") || result.types[0].toString().equals("food") || result.types[0].toString().equals("convenience_store") || idlist.contains(result.place_id) == true || result.name.toString().equals("天鼓") || result.name.toString().contains("カラオケ")) {
			} else {
				RouteReader.recvPlace2(place_id, "AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc", this);
				namelist.add(result.name);
				idlist.add(result.place_id);
			}
		}
	}

	@Override
	public void onPlace2(PlaceidData placeidData) {
//		layout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout1, null);   //レイアウトをその場で生成
//		output.addView(layout);
//		output.clearChildFocus(layout);
//		mMap.addMarker(new MarkerOptions().position(new LatLng(placeidData.result.geometry.location.lat, placeidData.result.geometry.location.lng)).title(count + ". " + placeidData.result.name));
//		mCount = (TextView) layout.findViewById(R.id.count);
//		mCount.setText(String.valueOf(count));
//		count++;
//		mName = (TextView) layout.findViewById(R.id.name);
//		mPhone = (TextView) layout.findViewById(R.id.formatted_phone_number);
//		mOpen = (TextView) layout.findViewById(R.id.opening_hours);
//		mName.setText(placeidData.result.name);
//		mPhone.setText(placeidData.result.formatted_phone_number);
//		if (placeidData.result.opening_hours != null && placeidData.result.opening_hours.weekday_text.length > 0) {
//			System.out.println(placeidData.result.opening_hours.weekday_text[day]);
//			mOpen.setText(placeidData.result.opening_hours.weekday_text[day]);
//		} else {
//			mOpen.setText("");
//		}
	}

	public void RouteSearch(List<List<HashMap<String, String>>> result) {
		ArrayList<LatLng> points = null;
		PolylineOptions lineOptions = null;
		MarkerOptions markerOptions = new MarkerOptions();
		if (result.size() != 0) {
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = result.get(i);
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);
					//始点付近の店が取れていない可能性がある。
						if (pCount < 300) {
								pCount++;
								pLat = lat;
								pLng = lng;
								RouteReader.recvPlace("AIzaSyCTBLImkAQi3CoNVJ7wXe32cNwHKTFSOqc",
										"food", position, seekBar.getProgress() * 25, this);
							RouteReader.recvPlace3(lat, lng, this);
						}
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

	@Override
	public void onClick(View view) {
		//マーカークリア
		//Marker.setMap(null);
		switch (view.getId()) {
			case R.id.imageButton:

				output.removeAllViews();
				pCount = 0;
				count = 1;
				idlist.clear();
				output.clearChildFocus(layout);
				//経路クリア
				mMap.clear();
				String editstr1 = editText1.getText().toString();
				String editstr2 = editText2.getText().toString();
				//ルート検索
				if (editstr1.matches("") && editstr2.matches("")) {
					if (t != null) {
						t.cancel();
					}
					t = Toast.makeText(this, "出発地･目的地が未入力です", Toast.LENGTH_SHORT);
					t.show();
				} else if (editstr1.matches("")) {
					if (t != null) {
						t.cancel();
					}
					t = Toast.makeText(this, "出発地が未入力です", Toast.LENGTH_SHORT);
					t.show();
				} else if (editstr2.matches("")) {
					if (t != null) {
						t.cancel();
					}
					t = Toast.makeText(this, "目的地が未入力です", Toast.LENGTH_SHORT);
					t.show();
				} else {
					if (t != null) {
						t.cancel();
					}
					t = Toast.makeText(this, "検索中です...", Toast.LENGTH_SHORT);
					t.show();
					try {
						RouteReader.recvRoute(editText1.getText().toString(), editText2.getText().toString(), this);
					} catch (Exception e) {
						if (t != null) {
							t.cancel();
						}
						t = Toast.makeText(this, "インターネットに接続されていません", Toast.LENGTH_SHORT);
						t.show();

					}
				}
				break;
			case R.id.GPSButton:
				gps=0;
				locationStart();
				break;
		}
	}
//	public void onPlace(PlaceData placeData) {
//		for (PlaceData.Results result : placeData.results) {
//System.out.println(result.geometry.location.lat + "," + result.geometry.location.lng);

	@Override
	public void onPlace3(GnaviData gnaviData) {
			System.out.println(namelist);
			for (GnaviData.Rest rest : gnaviData.rest) {
				float[] rDistance = new float[3];
				android.location.Location.distanceBetween(pLat,pLng,rest.latitude,rest.longitude,rDistance);
				System.out.println("距離"+rDistance[0]);
				System.out.println(rest.name);
				if(namelist.contains(" "+rest.name)==true) {
						layout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout1, null);   //レイアウトをその場で生成
						output.addView(layout);
						output.clearChildFocus(layout);
						mMap.addMarker(new MarkerOptions().position(new LatLng(rest.latitude, rest.longitude)).title(count + ". " + rest.name));
						mCount = (TextView) layout.findViewById(R.id.count);
						mCount.setText(String.valueOf(count));
						count++;
						mName = (TextView) layout.findViewById(R.id.name);
						mPhone = (TextView) layout.findViewById(R.id.formatted_phone_number);
						mOpen = (TextView) layout.findViewById(R.id.opening_hours);
						mType = (TextView) layout.findViewById(R.id.category);
						mName.setText(rest.name);
						mPhone.setText(rest.tel);
						mType.setText(rest.category);
						namelist.add(rest.name);
				}

//			if (rest.opentime != null) {
//				mOpen.setText(rest.opentime);
//			} else {
//				mOpen.setText("");
//			}
			}

	}
}
package jp.ac.chiba_fjb.oikawa.routetest;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.chiba_fjb.oikawa.routetest.RouteData;

//import jp.ac.chiba_fjb.c.chet.MainFragment;
public class parseJsonpOfDirectionAPI {

    //   MainFragment ma;

    public List<List<HashMap<String, String>>> parse(RouteData jObject) {
        String temp = "";
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        RouteData.Routes jsonRoutes = null;
        RouteData.Legs[] jsonLegs = null;
        RouteData.Legs.Steps[] jsonSteps = null;

        try {
            jsonRoutes = jObject.routes[0];

            for (int i = 0; i < jsonRoutes.legs.length; i++) {
                jsonLegs = jsonRoutes.legs;

                //スタート地点・住所
                String s_address = jsonLegs[i].start_address;
                // ma.info_A = s_address;

                //到着地点・住所
                String e_address = jsonLegs[i].end_address;
                // ma.info_B = e_address;

                String distance_txt = jsonLegs[i].distance.text;
                temp += distance_txt + "<br><br>";

                String distance_val = jsonLegs[i].distance.value;
                temp += distance_val + "<br><br>";

                List path = new ArrayList<HashMap<String, String>>();

                for (int j = 0; j < jsonLegs.length; j++) {
                    jsonSteps = jsonLegs[j].steps;

                    for (int k = 0; k < jsonSteps.length; k++) {
                        String polyline = "";
                        polyline = jsonSteps[k].polyline.points;
                        String instructions = jsonSteps[k].html_instructions;
                        String duration_value = jsonSteps[k].duration.value;
                        String duration_txt = jsonSteps[k].duration.text;

                        temp += instructions + "/" + duration_value + " m /" + duration_txt + "<br><br>";

                        List<LatLng> list = decodePoly(polyline);

                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    //ルート座標
                    routes.add(path);
                }

                //ルート情報
                //  ma.posinfo = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    //座標データをデコード
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by oikawa on 2017/10/17.
 */ //リターンデータのサンプル
//https://maps.googleapis.com/maps/api/directions/json?language=ja&origin=%E5%8D%83%E8%91%89&destination=%E6%9D%B1%E4%BA%AC&mode=driving&sensor=false
@JsonIgnoreProperties(ignoreUnknown=true)
public class Location{
	public double lat;
	public double lng;
}

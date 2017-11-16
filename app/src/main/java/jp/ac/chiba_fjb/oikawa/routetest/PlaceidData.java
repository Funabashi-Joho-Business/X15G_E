package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by x15g025 on 2017/10/31.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlaceidData {
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Opening_hours{
        public String[] weekday_text = new String[7];
    }
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Result{
        public String formatted_phone_number;
        public Opening_hours opening_hours;
        public String name;
        public String[] types;
    }

    public static class Photos {
        public String html_attributions[];
        public String height;
        public String width;
        public String photo_reference;
    }

    public Photos photos[];

    public Result result;
}

package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by x15g025 on 2018/01/25.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GnaviData {
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Rest{
      public String category;
        public String name;
        public double latitude;
        public double longitude;
        public String tel;
 //       public String opentime;
    }

    public Rest[] rest;
}

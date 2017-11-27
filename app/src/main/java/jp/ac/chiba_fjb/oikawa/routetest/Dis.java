package jp.ac.chiba_fjb.oikawa.routetest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by x15g025 on 2017/11/22.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dis {
    public String text;
    public String value;
}

package jp.ac.chiba_fjb.e.e_graduate_test;

import org.junit.Test;

import jp.ac.chiba_fjb.e.e_graduate_test.data.RouteReceiver;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Object o = RouteReceiver.getRoute("東京タワー","スカイツリー","driving");
        if(o == null)
            System.out.println("データ取得エラー");
        else
            System.out.println(o.toString());
    }
}
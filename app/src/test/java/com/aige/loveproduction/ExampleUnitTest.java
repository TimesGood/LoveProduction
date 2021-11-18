package com.aige.loveproduction;


import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        float f = 12.002541f;
        String s = f+"";
        System.out.println(s.substring(s.lastIndexOf(".") + 1));


    }


}
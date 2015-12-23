package com.coryrowens.egon;

//import org.junit;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

/**
 * Created by cory on 12/8/2015.
 */
public class NameTest {
    // TODO: Make tests

    @LargeTest
    public static void BigTest(){
        NameGenerator nameGen = NameGenerator.getInstance();
        for (int i = 0; i < 10; i++){
            Name name = nameGen.generate();
            Log.e("BigTest", name.getFullName());
            System.err.println(name.getFullName());
        }
    }
}

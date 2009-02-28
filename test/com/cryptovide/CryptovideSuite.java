/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cryptovide;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.Assert;
import org.junit.Test;
import clojure.lang.RT;
/**
 *
 * @author arthur
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({})
public class CryptovideSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("Testing Clojure World!");
        try {
            RT.loadResourceScript(MAINCLJ);
            RT.var("com.cryptovide.cryptovideTest",
                    "cryptovideTest").invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int foo = 1 / 0;
        Assert.assertTrue(false);
    }

    @After
    public void tearDown() throws Exception {
    }
    private static final String MAINCLJ = "test/com/cryptovide/cryptovideTest.clj";

    @Test
    public void cryptovideTest() {
        System.out.println("Testing Clojure World!");
        try {
            RT.loadResourceScript(MAINCLJ);
            RT.var("com.cryptovide.cryptovideTest",
                    "cryptovideTest").invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int foo = 1/0;
        Assert.assertTrue(false);
    }
}
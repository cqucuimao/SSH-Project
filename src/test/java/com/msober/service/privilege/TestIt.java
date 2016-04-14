package com.msober.service.privilege;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestIt {
 
    @BeforeClass
    public static void enter() {
        System.out.println("进来了！");
    }
 
    @Before
    public void init() {
        System.out.println("正在初始化。。");
        System.out.println("hello world");
        System.out.println("初始化完毕！");
    }
 
    @Test
    public void testit() {
    	System.out.println("running 1");
    }
    
    @Test
    public void testit2() {
    	System.out.println("running 2");
    }
 
 
    @After
    public void destroy() {
        System.out.println("销毁对象。。。");
        System.out.println("bye bye!");
        System.out.println("销毁完毕！");
    }
 
    @AfterClass
    public static void leave() {
        System.out.println("离开了！");
    }
}
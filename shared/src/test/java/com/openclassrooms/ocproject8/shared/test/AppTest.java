package com.openclassrooms.ocproject8.shared.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Test 
    public void testAppHasAGreeting() {
		System.out.println("Test shared");
    }
}
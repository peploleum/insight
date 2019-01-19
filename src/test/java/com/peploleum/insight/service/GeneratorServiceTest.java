package com.peploleum.insight.service;

import com.peploleum.insight.InsightApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class GeneratorServiceTest {
    @Autowired
    private GeneratorService generatorService;

    @Before
    public void setup() {
    }

    @Test
    public void generateOneTest() {
        this.generatorService.feed(1);
        this.generatorService.clean();
    }

    @Test
    public void generateMultipleTest() {
        this.generatorService.feed(10);
    }

    @After
    public void cleanTest() {

    }

}

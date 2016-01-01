package org.jboss.as.quickstarts.wshelloworld;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class HelloWorldServiceImplTest {

    private HelloWorldServiceImpl underTest;

    @Before
    public void setup() {
        underTest = new HelloWorldServiceImpl();
    }

    @Test
    public void sayHello() throws Exception {
        assertThat(underTest.sayHello(), is("Hello World!"));
    }

    @Test
    public void sayHelloToName() throws Exception {
        assertThat(underTest.sayHelloToName("Buddy"), is("Hello Buddy!"));
    }

    @Test
    public void sayHelloToNames() throws Exception {
        assertThat(underTest.sayHelloToNames(Arrays.asList("Tick", "Trick", "Track")),
                is("Hello Tick, Trick & Track!"));
    }
}

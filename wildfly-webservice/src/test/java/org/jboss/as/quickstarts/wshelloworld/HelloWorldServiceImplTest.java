package org.jboss.as.quickstarts.wshelloworld;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class HelloWorldServiceImplTest {
	
	private HelloWorldServiceImpl underTest;

	@Before
	public void setup() {
		underTest = new HelloWorldServiceImpl();
	}
	
	@Test
	public void testName() throws Exception {
		assertThat(  underTest.sayHello(), is("Hello World!"));
	}

}

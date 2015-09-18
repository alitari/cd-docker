
package org.jboss.as.quickstarts.wshelloworld;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;

@WebService(serviceName = "HelloWorldService", portName = "HelloWorld", name = "HelloWorld", endpointInterface = "org.jboss.as.quickstarts.wshelloworld.HelloWorldService", targetNamespace = "http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld")
public class HelloWorldServiceImpl implements HelloWorldService {

	private static final Logger LOGGER = Logger.getLogger(HelloWorldServiceImpl.class);
	
	
    @Override
    public String sayHello() {
        return sayHelloToName("World");
    }

    @Override
    public String sayHelloToName(final String name) {
        final List<String> names = new ArrayList<>();
        names.add(name);
        return sayHelloToNames(names);
    }

    @Override
    public String sayHelloToNames(final List<String> names) {
    	LOGGER.info("called sayHelloToNames("+names+")");
        return "Hello " + createNameListString(names);
    }

    private String createNameListString(final List<String> names) {
        if (names == null || names.isEmpty()) {
            return "Anonymous!";
        }

        final StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {

            if (i != 0 && i != names.size() - 1)
                nameBuilder.append(", ");
            else if (i != 0 && i == names.size() - 1)
                nameBuilder.append(" & ");

            nameBuilder.append(names.get(i));
        }

        nameBuilder.append("!");

        return nameBuilder.toString();
    }
}

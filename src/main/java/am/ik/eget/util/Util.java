package am.ik.eget.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Util {
    public static ConfigurableApplicationContext getApplicationContext() {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/spring/applicationContext.xml");
        return ctx;
    }
}

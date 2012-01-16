package am.ik.eget.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import am.ik.eget.exception.EgetException;

public class Util {
    public static ConfigurableApplicationContext getApplicationContext() {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/spring/applicationContext.xml");
        return ctx;
    }

    public static EgetException convertEgetException(Exception e) {
        if (e instanceof EgetException) {
            return (EgetException) e;
        } else {
            return new EgetException(e);
        }
    }
}

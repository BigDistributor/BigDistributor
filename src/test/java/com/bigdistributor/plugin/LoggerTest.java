package com.bigdistributor.plugin;

import com.bigdistributor.plugin.data.App;
import com.bigdistributor.plugin.data.HelloClass;
import org.junit.Test;

public class LoggerTest {

    @Test
    public void logMessageEnsureCorrectOutputFormat() {
        new HelloClass().sayHello();
        new App();
        new HelloClass().sayHello();
    }
}

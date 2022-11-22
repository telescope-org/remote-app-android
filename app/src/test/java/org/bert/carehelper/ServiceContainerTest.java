package org.bert.carehelper;

import static org.junit.Assert.assertEquals;

import org.bert.carehelper.common.ServiceContainer;
import org.junit.Test;

public class ServiceContainerTest {

    @Test
    public void serviceContainer() {
        ServiceContainer.getInstance();
    }
}

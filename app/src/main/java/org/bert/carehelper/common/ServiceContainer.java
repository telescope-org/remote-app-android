package org.bert.carehelper.common;

import org.bert.carehelper.service.Service;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {

    private Map<String, Service> serviceMap = new HashMap<>();
    private static ServiceContainer instance;

    private ServiceContainer() {
    }

    public static ServiceContainer getInstance() {
        if (instance == null) {
            synchronized (ServiceContainer.class) {
                if (instance == null) {
                    instance = new ServiceContainer();
                }
            }
        }
        return instance;
    }


    public ServiceContainer addService(String key, Service service) {
        if (this.serviceMap == null) {
            this.serviceMap = new HashMap<>();
        }
        this.serviceMap.put(key, service);
        return this;
    }

    public Service getService(String key) throws Exception {
        if (this.serviceMap == null) {
            throw new Exception("service map 未初始化！");
            Lo
        }
        if (this.serviceMap.get(key) == null) {
            throw new Exception("service: " + key + " 未注入！");
        }
        return this.serviceMap.get(key);
    }

    public Map<String, Service> getAllMap() {
        return this.serviceMap;
    }
}

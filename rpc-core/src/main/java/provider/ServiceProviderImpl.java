package provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册表，保存服务端本地服务
 */
public class ServiceProviderImpl implements ServiceProvider{

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    private static final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<Object> registeredServiceSet = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if(registeredServiceSet.contains(serviceName))return;
        registeredServiceSet.add(serviceName);
        serviceMap.put(serviceName,service);
        logger.info("向接口：{} 注解服务: {}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        return null;
    }

}

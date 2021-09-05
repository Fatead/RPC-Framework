package provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceProviderImpl implements ServiceProvider{

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);



    @Override
    public <T> void addServiceProvider(T service, String serviceName) {

    }

    @Override
    public Object getServiceProvider(String serviceName) {
        return null;
    }
}

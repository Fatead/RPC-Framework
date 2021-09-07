package registry;

import java.net.InetSocketAddress;

/**
 * 服务注册的接口
 */
public interface ServiceRegistry {

    /**
     * 将一个服务注册到服务表中
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);


}

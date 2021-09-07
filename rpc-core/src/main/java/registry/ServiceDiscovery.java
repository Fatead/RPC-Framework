package registry;

import java.net.InetSocketAddress;

/**
 * 服务发现的接口
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名获取服务的Socket地址
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);

}

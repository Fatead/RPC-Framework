package loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface LoadBalancer {

    /**
     * 根据负载均衡策略从实例列表中选取实例
     * @param instances
     * @return
     */
    Instance select(List<Instance> instances);

}

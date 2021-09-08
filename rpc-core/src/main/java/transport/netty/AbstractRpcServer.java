package transport.netty;

import annotation.Service;
import annotation.ServiceScan;
import enumeration.RpcError;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import registry.ServiceRegistry;
import util.ReflectUtil;

import java.net.InetSocketAddress;
import java.sql.Ref;
import java.util.Set;

public abstract class AbstractRpcServer implements RpcServer{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanService(){
        //获得启动类的全限类名
        String mainName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainName);
            if(!startClass.isAnnotationPresent(ServiceScan.class)){
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)){
            basePackage = mainName.substring(0,mainName.lastIndexOf("."));
        }
        //根据package名获取当前包下的所有类
        Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classes) {
            //当前类有Service注解，需要将其发布
            if(clazz.isAnnotationPresent(Service.class)){
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建实例时有错误发生");
                    continue;
                }
                if("".equals(serviceName)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        //getCanonical是获取所传类以Java语言规范定义的格式输出
                        publishService(obj,anInterface.getCanonicalName());
                    }
                }else {
                    publishService(obj,serviceName);
                }
            }
        }
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName,new InetSocketAddress(host,port));
    }

}

package handler;

import entity.RpcRequest;
import entity.RpcResponse;
import enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RPC过程中的处理器
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest){
        //根据接口名获取服务实例
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest,service);
    }

    public Object invokeTargetMethod(RpcRequest rpcRequest, Object service){
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service,rpcRequest.getParameters());
            logger.info("服务成功调用方法");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }

}

package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求号
     */
    private String requestId;

    /**
     * 待调用接口的名称
     */
    private String interfaceName;

    /**
     * 待调用方法的名称
     */
    private String methodName;

    /**
     * 待调用参数
     */
    private Object[] parameters;

    /**
     * 待调用参数的类型
     */
    private Class<?>[] paramTypes;

    /**
     * 是否为心跳包
     */
    private boolean heartBeat;

}

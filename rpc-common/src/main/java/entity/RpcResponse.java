package entity;

import enumeration.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对应的请求号
     */
    private String requestId;

    /**
     * 响应状态码
     */
    private Integer statusCode;

    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setData(data);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setMessage(code.getMessage());
        response.setStatusCode(code.getCode());
        return response;
    }

}

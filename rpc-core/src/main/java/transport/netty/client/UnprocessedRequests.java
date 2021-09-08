package transport.netty.client;
import entity.RpcResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedRequestFutures = new ConcurrentHashMap<>();

    /**
     * 将任务加入到容器中
     */
    public void put(String requestId,CompletableFuture<RpcResponse> future){
        unprocessedRequestFutures.put(requestId,future);
    }

    /**
     * 从容器中移除任务
     */
    public void remove(String requestId){
        unprocessedRequestFutures.remove(requestId);
    }

    public void complete(RpcResponse rpcResponse){
        CompletableFuture<RpcResponse> future = unprocessedRequestFutures.remove(rpcResponse.getRequestId());
        if(null!=future){
            future.complete(rpcResponse);
        }else {
            throw new IllegalStateException();
        }
    }

}

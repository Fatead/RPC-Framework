package transport.netty.server;

import entity.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    /**
     * Netty应用心跳和重连的过程：
     * 1. 客户端连接服务器端
     * 2. 在客户端的ChannelPipeline中加入一个比较特殊的IdleStateHandler，设置客户端的写空闲时间，例如5秒
     * 3. 当客户端的所有ChannelHandler中4s内没有write时间，会触发userEventTriggered方法
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState idleState = ((IdleStateEvent) evt).state();
            if(idleState == IdleState.READER_IDLE){
                logger.info("长时间未收到心跳包，断开连接...");
                ctx.close();
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
    }

    /**
     * 发生异常时会调用该函数
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理调用时有异常发生");
        cause.printStackTrace();;
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        if(rpcRequest.isHeartBeat()){
            logger.info("收到客户端心跳包...");
            return;
        }
        logger.info("服务器收到请求");
    }

}

package codec;

import entity.RpcRequest;
import entity.RpcResponse;
import enumeration.PackageType;
import enumeration.RpcError;
import exception.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解码器
 * ReplayingDecoder是ByteToMessageDecoder的一个子类
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUM = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //读入四个字节的数据判断是否是魔数
        int magic = byteBuf.readInt();
        if(magic!=MAGIC_NUM){
            logger.error("不识别的协议包: {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()){
            packageClass = RpcRequest.class;
        }else if(packageCode == PackageType.RESPONSE_PACK.getCode()){
            packageClass = RpcResponse.class;
        }else {
            logger.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

    }

}

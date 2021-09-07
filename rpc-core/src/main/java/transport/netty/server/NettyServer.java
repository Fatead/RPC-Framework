package transport.netty.server;

import serializer.CommonSerializer;
import transport.netty.RpcServer;

public class NettyServer implements RpcServer {

    //private final CommonSerializer serializer;

    public NettyServer(String host, int port){
        this(host,port,DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer){

    }

    @Override
    public void start() {

    }

    @Override
    public <T> void publishService(T service, String serviceName) {

    }
}

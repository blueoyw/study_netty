package Example.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.util.GlobalTrafficHandler;
import common.util.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Hello world!
 *
 */
public class App 
{
	protected static Logger log = LoggerFactory.getLogger(App.class);
	static final int RECONNECT_DELAY = Integer.parseInt( System.getProperty("reconnectDelay", "5"));
	static final int READ_TIMEOUT = Integer.parseInt( System.getProperty("readTimeout", "10"));
	static final String host = "localhost";
    static int port = 8080;
    static int packetSize = 0;
    static int gbps = 0;
	
	static void connect( Bootstrap b ) {
		b.connect().addListener(new ChannelFutureListener() {
			// @Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.cause() != null) {
					System.out.println("Failed to connect:");
				}
			}
		});
	}	
	
	private static Bootstrap configureBootstrap( Bootstrap b ) {
		return configureBootstrap(b, new NioEventLoopGroup());
	}
	
	static Bootstrap configureBootstrap( Bootstrap b, EventLoopGroup g ) {
		b.group(g);
		b.channel(NioSocketChannel.class);
		b.remoteAddress(host, port);
		b.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline().addLast(new TimeDecoder());
				ch.pipeline().addLast(new IdleStateHandler(READ_TIMEOUT, 0, 0));
				ch.pipeline().addLast( "trafficHandler" ,new GlobalTrafficHandler( ch.eventLoop() ) );
				ch.pipeline().addLast(new TimeClientHandler());
			}

		});
		return b;
	}
	
    public static void main(String[] args) throws Exception {
    	log.info("start->"+args.toString() );
    	if ( args.length > 0 ) {
        	port = Integer.parseInt(args[0]);        
        }    	
    	
    	configureBootstrap( new Bootstrap()).connect();
    	
    	/*
        try {        
        	EventLoopGroup workerGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.option(ChannelOption.SO_REUSEADDR, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
            	
                @Override
                public void initChannel(SocketChannel ch) throws Exception {                	                   
                	//ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(2));
                	ch.pipeline().addLast(new TimeDecoder());
                	ch.pipeline().addLast( new IdleStateHandler(READ_TIMEOUT, 0, 0) );
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });
            
 
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
 
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();        
        } finally {
            workerGroup.shutdownGracefully();
        }
        */
    }
}

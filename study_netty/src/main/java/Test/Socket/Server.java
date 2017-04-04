package Test.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.util.TimeEncoder;
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Hello world!
 *
 */
public class Server 
{
	protected static Logger log = LoggerFactory.getLogger(Server.class);	
	
	private int port;
	public Server(int port) {
		this.port = port;
	}
	public void run() throws Exception {
		//Listening 하는 Event Thread 생성
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		log.info( "boss=>" + bossGroup.toString() );
		//연결된 커넥션의 트래픽 처리 Event thread 생성
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		log.info( "workerGroup=>" + bossGroup.toString() );
		try {
			//서버 설정 헬퍼 클래스
			ServerBootstrap b = new ServerBootstrap();
			
			//커넥션 초기화에 NioServerSocketChannel을 이용
			//ChannelInitializer는 새 채널을 설정하는 헬퍼 클래스
			//TCP NO DELAY와 KEEPALIVE 설정
			b.group( bossGroup, workerGroup)
				.channel( NioServerSocketChannel.class )
				.childHandler( 
					new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new TimeEncoder());
							ch.pipeline().addLast(new DiscardServerHandler());
						}
					}
				).option(ChannelOption.SO_BACKLOG, 128)
				.option(ChannelOption.SO_REUSEADDR, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.bind(port).sync();	
			
			f.channel().closeFuture().sync();
			
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
    public static void main( String[] args ) throws Exception
    {
    	//PropertyConfigurator.configure(“log4j2.xml”);
    	
    	log.info("start");
    	
        int port;
        if ( args.length > 0 ) {
        	port = Integer.parseInt(args[0]);
        } else {
        	port = 8080;
        }
        new Server(port).run();
    }
}

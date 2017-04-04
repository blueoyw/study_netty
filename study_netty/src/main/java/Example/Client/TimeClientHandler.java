package Example.Client;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.util.UnixTime;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	protected static Logger log = LoggerFactory.getLogger(TimeClientHandler.class);
	final static String messge = "send test message";
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("Connetced from: " + ctx.channel().remoteAddress());
		
		//ChannelPipeline pipline = ctx.pipeline();
		//pipline.addLast(handlers);
		
		/*
		Timer timer = new HashedWheelTimer();
		timer.newTimeout(new TimerTask() {

			public void run(Timeout timeout) throws Exception {
				// TODO Auto-generated method stub
		
				ctx.writeAndFlush("Send Task Packet");				
				ctx.close();
			}

		}, 5, TimeUnit.SECONDS);
		*/		
		//ctx.writeAndFlush("Send Task Packet");				
		//ctx.close();
    }	
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	//read ByteBuf
    	/*
    	ByteBuf m = (ByteBuf) msg;
    	try {
    		long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
    		System.out.println( new Date(currentTimeMillis));    		
    		ctx.close();
    	} finally {
    		m.release();
    	}
    	*/
    	//read UnixTime class
    	UnixTime m = (UnixTime) msg;
    	log.info( m.toString() );
    	//ctx.close();
    }

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.info( cause.toString() );
		if (cause instanceof ReadTimeoutException) {
           log.info("Timeout!!!");
        }
		else {
			ctx.close();	
		}		
	}
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("Disconnected from: " + ctx.channel().remoteAddress());
    }
	
	@Override
	public void channelUnregistered( final ChannelHandlerContext ctx ) {
		log.info("Sleeping for: " + App.RECONNECT_DELAY + 's');
		
		final EventLoop loop = ctx.channel().eventLoop();
		loop.schedule(
				new Runnable() {
					public void run() {
						log.info("Reconnecting");
						App.connect( App.configureBootstrap(new Bootstrap(), loop));
					}
					
				}
				, App.RECONNECT_DELAY
				, TimeUnit.SECONDS				
				);		
	}
}

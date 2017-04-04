package Test.Socket;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.util.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {	
	protected Logger log = LoggerFactory.getLogger(DiscardServerHandler.class);
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {		
		log.info("====================start");
        ByteBuf in = (ByteBuf)msg;
        try{
        	log.info( in.toString(io.netty.util.CharsetUtil.US_ASCII) );
        } finally {
        	ReferenceCountUtil.release(msg);
        }        
		
		/*
		ctx.write(msg);
		ctx.flush();
		*/
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
	
	
	//when connection is established
	@Override
    public void channelActive( final ChannelHandlerContext ctx) {
		/*
		//alloc ByteBuf
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt( (int) (System.currentTimeMillis() / 1000L + 2208988800L) );
        
        //ChannelFuture represents an I/O operation which has not yet occured
        final ChannelFuture f = ctx.writeAndFlush( time );
        */
		
		
		Timer timer = new HashedWheelTimer();
		timer.newTimeout(new TimerTask() {
			public void run(Timeout timeout) throws Exception {
				UnixTime ut = new UnixTime();
				final ChannelFuture f = ctx.writeAndFlush( ut );
				log.info( ut.toString() );
				
				log.info("==============");
				Timer timer = timeout.timer();				
				timer.newTimeout( timeout.task(), 5, TimeUnit.SECONDS );
				
				/*
				f.addListener(new ChannelFutureListener() {
					public void operationComplete(ChannelFuture future) {
						assert f == future;
						
						//ctx.close();
					}
				});
				*/														
			}

		}, 5, TimeUnit.SECONDS );
		
		
		//f.addListener( ChannelFutureListener.CLOSE );
        //To get notified when a write request is finished. Create ChannelFutureListner and add addListner
		
		
						
    }    
	
}

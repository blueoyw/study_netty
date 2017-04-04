package common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {
	protected static Logger log = LoggerFactory.getLogger(TimeEncoder.class);
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) { 		
		UnixTime m = (UnixTime)msg;
		log.info( m.toString() );
		ByteBuf encoded = ctx.alloc().buffer(UnixTime.UnixTimeLength);
		encoded.writeInt( (int)m.value() );		
        ctx.write(encoded, promise);
    }
}

package common.util;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if ( in.readableBytes() < 4 ) {
			return;
		}
		
		//out.add( in.readBytes(4) );
		//read header
		//read body length				
		out.add( new UnixTime( in.readUnsignedInt() ) );		
		//ByteBuf buf = in.readBytes( UnixTime.UnixTimeLength );
		//out.add( new UnixTime( in.readBytes( UnixTime.UnixTimeLength ) )  );
	}

}

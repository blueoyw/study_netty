package common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.concurrent.EventExecutor;

//public class GlobalTrafficHandler extends GlobalTrafficShapingHandler implements ChannelHandler {
public class GlobalTrafficHandler extends GlobalTrafficShapingHandler {
	protected static Logger log = LoggerFactory.getLogger(GlobalTrafficHandler.class);
	
	public GlobalTrafficHandler(EventExecutor executor) {
		super(executor, 1000);		
	}
	
	@Override
	protected void doAccounting(TrafficCounter counter) {		  
        super.doAccounting(counter);
        
        log.error( "interval->" + counter.checkInterval() );
        log.error( "time->" + counter.lastCumulativeTime() );
        log.error( "cumulativeReadBytes->" + counter.cumulativeReadBytes() );
        log.error( "currentReadBytes->" + counter.currentReadBytes() );
        log.error ( "cum_write=>"+counter.cumulativeWrittenBytes() + "cur_write=>"+counter.currentWrittenBytes() );        		
        log.error( counter.toString() );      
    }		

}

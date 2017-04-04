package common.util;

import java.util.Date;

public class UnixTime {
	public static final int UnixTimeLength =  4;
	
	private final long value; 	
	public UnixTime() {
		this(System.currentTimeMillis() / 1000L + 2208988800L);
	}

	public UnixTime( long value ) {
		this.value = value;
	}
	
    public long value() {
        return value;
    }
    
    @Override
    public String toString() {
    	return new Date( (value() - 2208988800L) * 1000L ).toString();
    }
}

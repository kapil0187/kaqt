package kaqt.foundation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class MicrosecondUtcDateTime {
	private String microSeconds;
	private String milliSecondDateTime;
	
	public MicrosecondUtcDateTime(String datetime) {
		this.milliSecondDateTime = datetime.substring(0, "yyyy-MM-ddTHH:mm:ss.SSS".length());
		this.microSeconds = datetime.substring("yyyy-MM-ddTHH:mm:ss.SSSS".length(), datetime.length());
	}

	public long getMilliSeconds() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
		return sdf.parse(this.milliSecondDateTime).getTime();
	}

	@Override
	public String toString() {
		return this.milliSecondDateTime + this.microSeconds + "Z";
	}
	
}

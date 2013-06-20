package au.id.teda.broadband.usage.util;

public class HourlyVolumeUsage {

	public String day;
	public long hour;
	public long peak;
	public long offpeak;
	public long uploads;
	public long freezone;

	public HourlyVolumeUsage() {
		super();
	}

	public HourlyVolumeUsage(String day, long hour, long peak, long offpeak, long uploads, long freezone) {
		super();
		this.day = day;
		this.hour = hour;
		this.peak = peak;
		this.offpeak = offpeak;
		this.uploads = uploads;
		this.freezone = freezone;
	}

}

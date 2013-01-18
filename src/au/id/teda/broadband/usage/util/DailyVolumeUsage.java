package au.id.teda.broadband.usage.util;

public class DailyVolumeUsage {
	public long day;
	public String month;
	public long peak;
	public long offpeak;
	public long uploads;
	public long freezone;

	public DailyVolumeUsage() {
		super();
	}

	public DailyVolumeUsage(long day, String month, long peak, long offpeak, long uploads, long freezone) {
		super();
		this.day = day;
		this.month = month;
		this.peak = peak;
		this.offpeak = offpeak;
		this.uploads = uploads;
		this.freezone = freezone;
	}

}

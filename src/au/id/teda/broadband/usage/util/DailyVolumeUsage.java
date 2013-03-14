package au.id.teda.broadband.usage.util;

public class DailyVolumeUsage {
	public String month;
	public long day;
	public long peak;
	public long offpeak;
	public long uploads;
	public long freezone;

	public DailyVolumeUsage() {
		super();
	}

	public DailyVolumeUsage(String month, long day, long peak, long offpeak, long uploads, long freezone) {
		super();
		this.month = month;
		this.day = day;
		this.peak = peak;
		this.offpeak = offpeak;
		this.uploads = uploads;
		this.freezone = freezone;
	}

}

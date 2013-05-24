package au.id.teda.broadband.dev.util;

public class DailyVolumeUsage {
	public String month;
	public long day;
    public long anytime;
	public long peak;
	public long offpeak;
	public long uploads;
	public long freezone;

	public DailyVolumeUsage() {
		super();
	}

	public DailyVolumeUsage(String month, long day, long anytime, long peak, long offpeak, long uploads, long freezone) {
		super();
		this.month = month;
		this.day = day;
        this.anytime = anytime;
		this.peak = peak;
		this.offpeak = offpeak;
		this.uploads = uploads;
		this.freezone = freezone;
	}

}

package au.id.teda.broadband.usage.database;

import java.util.Calendar;

// This class represents a single database entry for daily data usage
public class VolumeUsage {
	
	private Long id;
	private Calendar period;
 	private Long month;
 	private Long peak;
 	private Long offpeak;
 	private Long uploads;
 	private Long freezone;

 	public long getId() {
 		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Calendar getPeriod(){
		return period;
	}
	
	public void setPeriod(Calendar period){
		this.period = period;
	}
	
	public Long getMonth(){
		return month;
	}
	
	public void setMonth(Long month){
		this.month = month;
	}
	
	public Long getPeak(){
		return peak;
	}
	
	public void setPeak(Long peak){
		this.peak = peak;
	}
	
	public Long getOffpeak(){
		return offpeak;
	}
	
	public void setOffpeak(Long offpeak){
		this.offpeak = offpeak;
	}
	
	public Long getUploads(){
		return uploads;
	}
	
	public void setUploads(Long uploads){
		this.uploads = uploads;
	}
	
	public Long getFreezone(){
		return freezone;
	}
	
	public void setFreezone(Long freezone){
		this.uploads = freezone;
	}
}

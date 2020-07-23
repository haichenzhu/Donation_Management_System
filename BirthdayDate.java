package DM19S1;
import java.text.*;
import java.util.*;

public class BirthdayDate {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
	private String dateStr;
	private Date date;
	
	public BirthdayDate(String d) { 
		dateStr = d;
		dateFormat.setLenient(false);
		String[] temp;
		// reformat the date string
	    if (dateStr.matches("\\d+\\D\\d+\\D\\d+")) { 
	    	temp = dateStr.split("\\D");
	    	if (temp.length == 3) {
	    		for (int i = 0; i < 2; ++i) { 
	    			if (temp[i].length() < 2) temp[i] = "0" + temp[i];
	    				
	    		}
	    		dateStr = temp[0] + "-" + temp[1] + "-" + temp[2];
	    	}
	    }
	    try {
	    	date = dateFormat.parse(dateStr);
	    } catch (ParseException e) { 
	    	date = null;
	    }
	}
	
	public boolean isValid() {
		Date now = new Date();
		if (date != null && date.compareTo(now) < 0) {
			return true; 
		} else {
			return false;
		}
	}
	
	public String toString() {
		return dateFormat.format(date);
	}
		
	public Date getDate() { 
		return date;
	}
}

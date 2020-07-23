// Written By Haichen ZHU (hzhu8034)
// Date: 14/04/2019
package DM19S1;
import java.util.*;

public class Recipient {

	private String recipientName;                 // the name of the organization which receive donation
	private Map<String, Double> receiveFrom;      // the total money received from each postcode area
	
	public Recipient() {
		recipientName = null;
		receiveFrom = new HashMap<String, Double>();
	}
		
	// create a new recipient by its name
	public Recipient(String name) {
		recipientName = name;
		receiveFrom = new HashMap<String, Double>();
	}
	
	// get the name of this organization
	public String getName() {
		return recipientName;
	}
	
	
	// set name
	public void setName(String name) {
		recipientName = name;
	}
	
	
	// return the total money received from each postcode area
	public Map<String, Double> getReceiveFrom() {
		return receiveFrom;
	}
	
	
	// if a new donation received, update its receiveFrom
	public void updateReceiveFrom(String postcode, double amount) {
		if (receiveFrom.containsKey(postcode)) {
			receiveFrom.put(postcode, amount + receiveFrom.get(postcode));
		} else {
			receiveFrom.put(postcode, amount);
		}
	}
	
	
	// if a donator is deleted, then delete the corresponding donation
	public void deleteByDonator(String postcode, double amount) {
		for (Map.Entry<String, Double> entry: receiveFrom.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(postcode)) {
				receiveFrom.put(postcode, entry.getValue() - amount);
			}
		}
	}
	
	
	// get the largest money received from one postcode area
	public double getLargestReceived() {
		double max = 0.0;
		for (Map.Entry<String, Double> entry: receiveFrom.entrySet()) {
			if (entry.getValue() > max) max = entry.getValue();
		}
		return max;
	}
}	


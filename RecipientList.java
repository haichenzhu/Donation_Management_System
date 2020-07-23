package DM19S1;
import java.util.*;

public class RecipientList {

	private ArrayList<Recipient> recipientList;
	
	public RecipientList() {
		recipientList = new ArrayList<Recipient>();
	}
	
	public RecipientList(ArrayList<Recipient> rList) {
		recipientList = rList;
	}
	
	
	// return the recipientlist
	public ArrayList<Recipient> getRecipientList() {
		return recipientList;
	}
	
	
	// check if there is a specified recipient
	public boolean existRecipient(Recipient r) {
		for (Recipient e: recipientList) {
			if (e.equals(r)) return true;
		}
		return false;
	}
	
	
	// if the recipient does not exist in current recipientlist, then add it 
	public void addRecipient(Recipient r) {
		recipientList.add(r);
	}
	
	
	// check if there is a specified recipient with its name
	public boolean existByName(String name) {
		for (Recipient e: recipientList) {
			if (e.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
	
	// update the recipientlist to a new list
	public void updateRecipientList(ArrayList<Recipient> rList) {
		recipientList.clear();
		recipientList = rList;
	}
	
	
	// update related recipient in the recipientlist for its money received
	public void updateRecipient(Map<String, Double> money, String postcode) {
		for (Map.Entry<String, Double> entry: money.entrySet()) {
			String name = entry.getKey();
			if (existByName(name)) {
				for (Recipient e: recipientList) {
					if (e.getName().equalsIgnoreCase(name)) {
						e.updateReceiveFrom(postcode, entry.getValue());
						continue;
					}
				}
			} else {
				Recipient re = new Recipient(name);
				re.updateReceiveFrom(postcode, entry.getValue());
				recipientList.add(re);
			}
		}
	}
	
	
	// if a donator is deleted, then delete the corresponding donation in its recipient
	public void deleteByDonator(Map<String, Double> money, String postcode) {
		for (Map.Entry<String, Double> entry: money.entrySet()) {
			for (Recipient e: recipientList) {
				if (e.getName().equalsIgnoreCase(entry.getKey())) {
					e.deleteByDonator(postcode, entry.getValue());
				}
			}
		}
	}
	
	
	// sort the recipientlist by its name
	public void sortedByName() {
		SortByName sortby = new SortByName();
		Collections.sort(recipientList, sortby);
		
	}
	
}

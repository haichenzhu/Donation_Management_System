package DM19S1;
import java.util.*;

public class DonatorList {

	private ArrayList<Donator> donatorList;
	
	public DonatorList() {
		donatorList = new ArrayList<Donator>();
	}
	
	
	public DonatorList(ArrayList<Donator> dList) {
		donatorList = new ArrayList<Donator>(dList);
	}
	
	
	// return the donatorlist
	public ArrayList<Donator> getDonatorList() {
		return donatorList;
	}
	
	
	// if a new donator's information is valid to add, add it into donatorlist
	public void addDonator(Donator d) {
		if (d.isValidToAdd()) donatorList.add(d);
	}
	
	
	// delete an donator from donatorlist
	public void removeDonator(Donator d) {
		if (existDonator(d.getName(), d.getBirthday())) donatorList.remove(d);
	}
	
	
	// return the related donator
	public Donator getDonator(String name, BirthdayDate bdate) {
		for (Donator e: donatorList) {
			if (e.getName().equals(name) && e.getBirthday().toString().equals(bdate.toString())) {
				return e;
			}
		}
		return null;
	}
	
	
	// judge if there is a donator with given name and birthday exist in donatorlist and return boolean
	public boolean existDonator(String name, BirthdayDate bdate) {
		for (Donator e: donatorList) {
			if (e.getName().equals(name) && e.getBirthday().toString().equals(bdate.toString())) {
				return true;
			}
		}
		return false;
	}
	
	
	// update a donator in this donatorlist
	public void updateDonator(String strUpdate) {
		Map<String, String> updateContent = new HashMap<String, String>();
		String[] str = strUpdate.trim().split(";");
		for (String e: str) {
			String[] temp = e.trim().split(" ");
			if (temp[0].equalsIgnoreCase("name")) {
				updateContent.put("name", e.trim().substring("name".length()).trim());
			} else if (temp[0].equalsIgnoreCase("birthday")) {
				updateContent.put("birthday", e.trim().substring("birthday".length()).trim());
			} else if (temp[0].equalsIgnoreCase("address")) {
				updateContent.put("address", e.trim().substring("address".length()).trim());
			} else if (temp[0].equalsIgnoreCase("postcode")) {
				updateContent.put("postcode", e.trim().substring("postcode".length()).trim());
			} else if (temp[0].equalsIgnoreCase("phone") || temp[0].equalsIgnoreCase("mobile")) {
				updateContent.put("phone", e.trim().substring("phone".length()).trim());
			}
		}
		BirthdayDate bday = new BirthdayDate(updateContent.get("birthday"));
		if (Donator.isValidName(updateContent.get("name")) && bday.isValid()) {
			if (existDonator(updateContent.get("name"), new BirthdayDate(updateContent.get("birthday")))) {
				Donator don = getDonator(updateContent.get("name"), new BirthdayDate(updateContent.get("birthday")));
				if (updateContent.containsKey("address") && Donator.isValidAddress(updateContent.get("address"))) {
					don.setAddress(updateContent.get("address"));
				}
				if (updateContent.containsKey("postcode") && Donator.isValidPostcode(updateContent.get("postcode"))) {
					don.setPostcode(updateContent.get("postcode"));
				}
				if (updateContent.containsKey("phone") && Donator.isValidPhone(updateContent.get("phone"))) {
					don.setPhone(updateContent.get("phone"));
				}
			} else {
				Donator don = new Donator(updateContent);
				if (don.isValidToAdd()) {
					donatorList.add(don);
				}
				
			}
		}		
	}
	
	
	// sort the donatorlist by its total donation
	public void sortByTotalDonation() {
		SortByMoney sortby = new SortByMoney();
		Collections.sort(donatorList, sortby);
	}
	
	
	// sort the donatorlist by its name
	public void sortByName() {
		SortByDonatorName sortby = new SortByDonatorName();
		Collections.sort(donatorList, sortby);
	}
	
	
	// get information and return using string
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Donator e: donatorList) {
			sb.append(e.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	// for test
	public void show() {
		for (Donator e: donatorList) {
			e.show();
			System.out.print("\n");
		}
	}
}

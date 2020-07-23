// Written By Haichen ZHU (hzhu8034)
// Date: 14/04/2019
package DM19S1;
import java.util.*;

public class Donator {

	private String nameOfDonator;                 // name of the donator
	private BirthdayDate birthday;                // birthday of the donator
	private String phoneNumber;                   // phone number of the donator
	private String address;                       // residential address of the donator
	private String postcode;                      // postcode, zipdoce
	private Map<String, Double> moneyGiven;       // donations that this donator has made(to who, how much)
	private double totalDonation;                 // total number of donation
	
	public Donator() {
		nameOfDonator = null;
		birthday = null;
		phoneNumber = null;
		address = null;
		postcode = null;
		moneyGiven = new HashMap<String, Double>();
		totalDonation = 0.0;
	}
		
	
	public Donator(ArrayList<String> strDonator) {
		Map<String, String> donatorMap = new HashMap<String, String>();
		ArrayList<String> recipients = new ArrayList<String>();
		ArrayList<Double> money = new ArrayList<Double>();
		for (int i = 0; i < strDonator.size(); i++) {
			String[] temp = strDonator.get(i).split(" ");
			if (temp[0].equalsIgnoreCase("name")) {
				donatorMap.put("name", strDonator.get(i).substring("name".length()).trim());
			} else if (temp[0].equalsIgnoreCase("birthday")) {
				donatorMap.put("birthday", strDonator.get(i).substring("birthday".length()).trim());
			} else if (temp[0].equalsIgnoreCase("phone") || temp[0].equalsIgnoreCase("mobile")) {
				donatorMap.put("phone", strDonator.get(i).substring("phone".length()).trim());
			} else if (temp[0].equalsIgnoreCase("address")) {
				String strAddress = strDonator.get(i).substring("address".length()).trim();
				while (strAddress.endsWith(",") && i < strDonator.size()) {
					i++;
					strAddress += " " + strDonator.get(i).trim();
				}
				donatorMap.put("address", strAddress);
			} else if (temp[0].equalsIgnoreCase("postcode")) {
				donatorMap.put("postcode", strDonator.get(i).substring("postcode".length()).trim());
			} else if (temp[0].equalsIgnoreCase("recipient")) {
				String strRecipient = strDonator.get(i).substring("recipient".length()).trim();
				String[] strList = strRecipient.split(",");
				for (String e: strList) {
					recipients.add(e.trim());
				}
			} else if (temp[0].equalsIgnoreCase("donation")) {
				String strMoney = strDonator.get(i).substring("donation".length()).trim();
				String[] strList = strMoney.split(",");
				for (String e: strList) {
					money.add(Double.parseDouble(e.trim()));
				}
			}
		}
		if (donatorMap.containsKey("name") && donatorMap.get("name") != "" &&
				donatorMap.containsKey("birthday") && donatorMap.get("birthday") != "") {
			nameOfDonator = donatorMap.get("name");
			birthday = new BirthdayDate(donatorMap.get("birthday"));
			if (donatorMap.containsKey("phone") && donatorMap.get("phone") != "" && isValidPhone(donatorMap.get("phone"))) {
				phoneNumber = donatorMap.get("phone");
			} else {
				phoneNumber = null;
			}
			if (donatorMap.containsKey("address") && donatorMap.get("address") != "" && isValidAddress(donatorMap.get("address"))) {
				address = donatorMap.get("address");
			} else {
				address = null;
			}
			if (donatorMap.containsKey("postcode") && donatorMap.get("postcode") != "" && isValidPostcode(donatorMap.get("postcode"))) {
				postcode = donatorMap.get("postcode");
			} else {
				postcode = null;
			}
			if (recipients.size() == money.size() && money.size() != 0) {
				moneyGiven = new HashMap<String, Double>();
				double totalMoney = 0.0;
				for (int i = 0; i < recipients.size(); i++) {
					moneyGiven.put(recipients.get(i), money.get(i));
					totalMoney += money.get(i);
				}
				totalDonation = totalMoney;
			} else {
				moneyGiven = new HashMap<String, Double>();
				totalDonation = 0.0;
			}
		} else {
			nameOfDonator = null;
			birthday = null;
			phoneNumber = null;
			address = null;
			postcode = null;
			moneyGiven = new HashMap<String, Double>();
			totalDonation = 0.0;
		}
	}
	
	public Donator(Map<String, String> donatorMap) {
		nameOfDonator = donatorMap.get("name");
		birthday = new BirthdayDate(donatorMap.get("birthday"));
		if (donatorMap.containsKey("phone") && donatorMap.get("phone") != "" && isValidPhone(donatorMap.get("phone"))) {
			phoneNumber = donatorMap.get("phone");
		} else {
			phoneNumber = null;
		}
		if (donatorMap.containsKey("address") && donatorMap.get("address") != "" && isValidAddress(donatorMap.get("address"))) {
			address = donatorMap.get("address");
		} else {
			address = null;
		}
		if (donatorMap.containsKey("postcode") && donatorMap.get("postcode") != "" && isValidPostcode(donatorMap.get("postcode"))) {
			postcode = donatorMap.get("postcode");
		} else {
			postcode = null;
		}
		moneyGiven = new HashMap<String, Double>();
		totalDonation = 0.0;
	}
	
	// get the name of this donator
	public String getName() {
		return nameOfDonator;
	}
	
	
	// set name
	public void setName(String name) {
		nameOfDonator = name;
	}
	
	// get the birthday of this donator
	public BirthdayDate getBirthday() {
		return birthday;
	}
	
	
	// set birthday
	public void setBirthday(String strDate) {
		birthday = new BirthdayDate(strDate); 
	}
	
	// get the phone number of this donator
	public String getPhone() {
		return phoneNumber;
	}
	
	// set the phone number
	public void setPhone(String phone) {
		phoneNumber = phone;
	}
	
	// get the living address of this donator
	public String getAddress() {
		return address;
	}
	
	//set the address
	public void setAddress(String add) {
		address = add;
	}
	
	// get the postcode of this danator
	public String getPostcode() {
		return postcode;
	}
	
	// set the psotcode
	public void setPostcode(String zipcode) {
		postcode = zipcode;
	}
	
	
	// return the donation that this donator has made
	public Map<String, Double> getMoneyGiven() {
		return moneyGiven;
	}
	
	
	// update this donator's donation
	public void updateMoneyGiven(String name, double amount) {
		if (moneyGiven.containsKey(name)) {
			moneyGiven.put(name, moneyGiven.get(name) + amount);
		} else {
			moneyGiven.put(name, amount);
		}
	}
	
	
	// set the donation that this donator has made
	public void setMoneyGiven(Map<String, Double> money) {
		moneyGiven = money;
	}
	
	
	// check this donator's information is valid
	public boolean isValidToAdd() {
		return isValidName(nameOfDonator) && isValidBirthday();
	}
	
	
	//// check if it is a valid name
	public static boolean isValidName(String strName) {
		if (strName != null && strName.matches("[a-zA-Z ]+")) return true;
		return false;
	}
	
	
	// // check if it is a valid birthday
	private boolean isValidBirthday() {
		return birthday.isValid();
	}
	
	
	// // check if it is a valid phone number
	public static boolean isValidPhone(String strPhone) {
		if (strPhone == null || strPhone.matches("[0-9]{8}")) return true;
		return false;		
	}
	
	
	// // check if it is a valid addresss
	public static boolean isValidAddress(String strAddress) {
		if (strAddress == null || strAddress.endsWith("NSW") || strAddress.endsWith("QLD") ||
				strAddress.endsWith("WA") || strAddress.endsWith("VIC") || strAddress.endsWith("ACT") ||
				strAddress.endsWith("NT") || strAddress.endsWith("SA") || strAddress.endsWith("TAS")) return true;
		return false;
	}
	
	
	// check if it is a valid postcode
	public static boolean isValidPostcode(String strPostcode) {
		if (strPostcode != null && strPostcode.matches("[0-9]{4}")) return true;
		return false;
	}
	
	
	// return the total donation that this donator has given
	public double getTotalDonation() {
		return totalDonation;
	}
	
	
	// reutrn the information in String
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name " + nameOfDonator + "\n");
		sb.append("birthday " + birthday.toString() + "\n");
		if (address != null) sb.append("address " + address + "\n");
		if (postcode != null) sb.append("postcode " + postcode + "\n");
		if (phoneNumber != null) sb.append("phone " + phoneNumber + "\n");
		if (moneyGiven.size() != 0 ) {
			sb.append("recipient");
			for (Map.Entry<String, Double> entry: moneyGiven.entrySet()) {
				sb.append(" " + entry.getKey() + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
			
			sb.append("donation");
			for (Map.Entry<String, Double> entry: moneyGiven.entrySet()) {
				String strMoney = String.format("%.0f", entry.getValue());
				sb.append(" " + strMoney + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void show() {
		System.out.println("name: " + nameOfDonator);
		System.out.println("birthday: " + birthday.toString());
		System.out.println("phone: " + phoneNumber);
		System.out.println("address: " + address);
		System.out.println("postcode: " + postcode);
		for (Map.Entry<String, Double> entry: moneyGiven.entrySet()) {
			System.out.println("To " + entry.getKey() + " with " + entry.getValue());
		}
	}
}
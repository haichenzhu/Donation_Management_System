package DM19S1;
import java.io.*;
import java.util.*;

public class DonationProcessor {

	private File membersFile;
	private File instructionFile;
	private File resultFile;
	private File reportFile;
	private DonatorList donaList;
	private RecipientList recipList;
	
	
	public DonationProcessor(String[] files) {
		membersFile = new File(files[0] + ".txt");
		instructionFile = new File(files[1] + ".txt");
		resultFile = new File(files[2] + ".txt");
		reportFile = new File(files[3] + ".txt");
		donaList = new DonatorList();
		recipList = new RecipientList();
	}
	
	
	// read the donator information from members file and record valid ones
	public void readMembersFile() {
		try {
			Scanner scn = new Scanner(membersFile);
			ArrayList<String> records = new ArrayList<String>();
			while (scn.hasNextLine()) {
				String strRecord = scn.nextLine();
				if (strRecord.trim().equals("")) {
					Donator don = new Donator(records);
					if (don.isValidToAdd()) {
							donaList.addDonator(don);
							recipList.updateRecipient(don.getMoneyGiven(), don.getPostcode());						
					}
					records.clear();
					continue;
				} else {
					records.add(strRecord);
				}
			}
			Donator don = new Donator(records);
			if (don.isValidToAdd()) {
				donaList.addDonator(don);
				recipList.updateRecipient(don.getMoneyGiven(), don.getPostcode());
			}
			records.clear();
			scn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	// read the instruction file and call related function
	public void readInstructionFile() {
		try {
			Scanner scn = new Scanner(instructionFile);
			while (scn.hasNextLine()) {
				String instruction = scn.nextLine();
				Scanner sc = new Scanner(instruction);
				String keyword, param;
				if (sc.hasNext()) {
					keyword = sc.next();
					if (sc.hasNextLine()) {
						param = sc.nextLine();
						if (keyword.equalsIgnoreCase("update")) {
							instructionUpdate(param);
						} else if (keyword.equalsIgnoreCase("donate")) {
							instructionDonate(param);
						} else if (keyword.equalsIgnoreCase("delete")) {
							instructionDelete(param);
						} else if (keyword.equalsIgnoreCase("query")) {
							instructionQuery(param);
						}
					} else {
						continue;
					}
					sc.close();
				} else {
					continue;
				}				
			}
			scn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	// when the instruction is update, judge the reords then keep valid ones and give corresponding 
	// answer in report file
	public void instructionUpdate(String strUpdate) {
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
			} else if (temp[0].equalsIgnoreCase("phone")) {
				updateContent.put("phone", e.trim().substring("phone".length()).trim());
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("----update " + str[0].trim() + "----\n");
				sb.append("Invalid instruction (not supported field)!\n");
				sb.append("-------------------------\n");
				saveReport(sb.toString());
				return;
			}
		}
		BirthdayDate bday = new BirthdayDate(updateContent.get("birthday"));
		if (Donator.isValidName(updateContent.get("name")) && bday.isValid()) {
			if (donaList.existDonator(updateContent.get("name"), bday)) {
				Donator don = donaList.getDonator(updateContent.get("name"), new BirthdayDate(updateContent.get("birthday")));
				if (updateContent.containsKey("address") && Donator.isValidAddress(updateContent.get("address"))) {
					don.setAddress(updateContent.get("address"));
				}
				if (updateContent.containsKey("postcode") && Donator.isValidPostcode(updateContent.get("postcode"))) {
					don.setPostcode(updateContent.get("postcode"));
				}
				if (updateContent.containsKey("phone") && Donator.isValidPhone(updateContent.get("phone"))) {
					don.setPhone(updateContent.get("phone"));
				}
				StringBuilder sb = new StringBuilder();
				sb.append("----update " + str[0].trim() + "----\n");
				sb.append("Rocord updated!\n");
				sb.append("-------------------------\n");
				saveReport(sb.toString());
				return;
			} else {
				Donator don = new Donator(updateContent);
				if (don.isValidToAdd()) {
					donaList.addDonator(don);
					StringBuilder sb = new StringBuilder();
					sb.append("----update " + str[0].trim() + "----\n");
					sb.append("Rocord added!\n");
					sb.append("-------------------------\n");
					saveReport(sb.toString());
					return;
				}
			}
		} else if (!Donator.isValidName(updateContent.get("name").trim())) {
			StringBuilder sb = new StringBuilder();
			sb.append("----update " + str[0].trim() + "----\n");
			sb.append("Invalid instruction (name)!\n");
			sb.append("-------------------------\n");
			saveReport(sb.toString());
			return;
		} else if (!bday.isValid()) {
			StringBuilder sb = new StringBuilder();
			sb.append("----update " + str[0].trim() + "----\n");
			sb.append("Invalid instruction (birthday)!\n");
			sb.append("-------------------------\n");
			saveReport(sb.toString());
			return;
		}
	}
	
	
	// when it is donate, update the donation to related one, if not exists add a new donator
	public void instructionDonate(String strDonate) {
		String[] str = strDonate.trim().split(";");
		int n = str.length;
		if (n > 2) {
			String name = str[0].trim();
			BirthdayDate birthday = new BirthdayDate(str[1].trim());
			if (Donator.isValidName(name) && birthday.isValid()) {
				for (Donator e: donaList.getDonatorList()) {
					if (e.getName().equals(name) && birthday.toString().equals(e.getBirthday().toString())) {
						for (int i = 2; i < str.length; i++) {
							String[] moneyGiven = str[i].trim().split(",");
							e.updateMoneyGiven(moneyGiven[0].trim(), Double.parseDouble(moneyGiven[1].trim()));
							Map<String, Double> update = new HashMap<String, Double>();
							update.put(moneyGiven[0].trim(), Double.parseDouble(moneyGiven[1].trim()));
							recipList.updateRecipient(update, e.getPostcode());						
						}
						StringBuilder sb = new StringBuilder();
						sb.append("----donate " + str[0] + "----\n");
						sb.append(n - 2 + " new donation record(s) for " + name + " birthday " + birthday.toString() + "\n");
						sb.append("-------------------------\n");
						saveReport(sb.toString());
						return;
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("----donate " + str[0] + "----\n");
		sb.append("Invalid instruction!\n");
		sb.append("-------------------------\n");
		saveReport(sb.toString());
		return;
	}
	
	
	// delete the specified donator in donatorlist, and give related answer in report file
	public void instructionDelete(String strDelete) {
		String[] str = strDelete.trim().split(";");
		if (str.length == 2) {
			String name = str[0].trim();
			BirthdayDate birthday = new BirthdayDate(str[1].trim());
			if (Donator.isValidName(name) && birthday.isValid()) {
				for (Donator e: donaList.getDonatorList()) {
					if (e.getName().equals(name) && e.getBirthday().toString().equals(birthday.toString())) {
						//update recipient
						recipList.deleteByDonator(e.getMoneyGiven(), e.getPostcode());
						donaList.removeDonator(e);
						StringBuilder sb = new StringBuilder();
						sb.append("----delete " + str[0].trim() + "----\n");
						sb.append("Record deleted!\n");
						sb.append("-------------------------\n");
						saveReport(sb.toString());
						return;
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("----delete " + str[0].trim() + "----\n");
		sb.append("Record not found!\n");
		sb.append("-------------------------\n");
		saveReport(sb.toString());
		return;
	}
	
	
	// when it is query, call related funciton
	public void instructionQuery(String strQuery) {
		String[] queryContent = strQuery.trim().split(" ");
		if (queryContent[0].equalsIgnoreCase("name")) {
			queryName(strQuery.trim().substring("name".length()).trim());
			return;
		} else if (queryContent[0].equalsIgnoreCase("top")) {
			queryTop(strQuery.trim().substring("top".length()).trim());
			return;
		} else if (queryContent[0].equalsIgnoreCase("recipients")) {
			queryRecipients();
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("----query " + strQuery + "----\n");
		sb.append("Invalid Instruction!\n");
		sb.append("-------------------------\n");
		saveReport(sb.toString());
		return;
	}
	
	
	// query by name, find the donator by its name and print it to report file
	private void queryName(String strQueryName) {
		String name = strQueryName.trim();
		ArrayList<Donator> temp = new ArrayList<Donator>();
		for (Donator e: donaList.getDonatorList()) {
			if (e.getName().equals(name)) {
				temp.add(e);
			}
		}
		int n = temp.size();
		if (n > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("----query name " + strQueryName + "----\n");
			sb.append(n + " record(s) found:\n");
			for (Donator e: temp) {
				sb.append(e.toString());
			}
			sb.append("-------------------------\n");
			saveReport(sb.toString());
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("----query name " + strQueryName + "----\n");
		sb.append("Record not found!\n");
		sb.append("-------------------------\n");
		saveReport(sb.toString());
		return;
	}
	
	
	// order the donatorlist by the totaldonation, if the same donation, order by its name
	// and get back the top some result tp report file
	private void queryTop(String strQueryTop) {
		try {
			int number = Integer.parseInt(strQueryTop.trim());
			donaList.sortByName();
			donaList.sortByTotalDonation();
			int i = 0;
			StringBuilder sb = new StringBuilder();
			sb.append("----query top" + strQueryTop + "----\n");
			for (Donator e: donaList.getDonatorList()) {
				sb.append(e.getName() + "; " + e.getBirthday().toString() + "; " + String.format("%.0f", e.getTotalDonation()) + "\n");
				i++;
				if (i >= number) break;
			}
			sb.append("-------------------------\n");
			saveReport(sb.toString());
			return;
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("----query top" + strQueryTop + "----\n");
			sb.append("Invalid Instruction!\n");
			sb.append("-------------------------\n");
			saveReport(sb.toString());
			return;
		}
	}
	
	
	// find every recipient's largest donation received, and find the postcode it comes from
	public void queryRecipients() {
		recipList.sortedByName();
		StringBuilder sb = new StringBuilder();
		sb.append("----query recipients----\n");
		for (Recipient e: recipList.getRecipientList()) {
			double max = e.getLargestReceived();
			sb.append(e.getName() + ": " + String.format("%.0f", max) + "; postcode");
			for (Map.Entry<String, Double> entry: e.getReceiveFrom().entrySet()) {
				if (entry.getValue() == max) {
					sb.append(" " + entry.getKey() + ",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
		}
		sb.append("-------------------------\n");
		saveReport(sb.toString());
		return;
	}
	
	
	// create report file, and save related instruction response to it
	public void saveReport(String reportContent) {
		try {
			FileOutputStream fos = null;
			if (!reportFile.exists()) {
				reportFile.createNewFile();
				fos = new FileOutputStream(reportFile);
			} else {
				fos = new FileOutputStream(reportFile, true);
			}
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			
			if (reportContent != "") {
				osw.write(reportContent);
				osw.write("\n");
			}
			
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// save the final result of donator in the donatorlist in result file
	public void saveResult() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(resultFile)); 
			out.println(donaList.toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}

package DM19S1;

public class DM {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DonationProcessor dp = new DonationProcessor(args);
		dp.readMembersFile();
		dp.saveReport("");
		dp.readInstructionFile();
		dp.saveResult();
	}  
}

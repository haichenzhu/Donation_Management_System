package DM19S1;
import java.util.*;

class SortByMoney implements Comparator<Donator>{
	public int compare(Donator o1, Donator o2) {
		if (o1.getTotalDonation() < o2.getTotalDonation()) return 1;
		if (o1.getTotalDonation() == o2.getTotalDonation()) return 0;
		return -1;
	}
}

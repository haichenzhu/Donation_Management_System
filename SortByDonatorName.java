package DM19S1;
import java.util.Comparator;

class SortByDonatorName implements Comparator<Donator>{
	public int compare(Donator o1, Donator o2) {
		if (o1.getName().compareTo(o2.getName()) > 0) return 1;
		if (o1.getName().compareTo(o2.getName()) == 0) return 0;
		return -1;
	}
}
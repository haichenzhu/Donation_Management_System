package DM19S1;
import java.util.Comparator;

class SortByName implements Comparator<Recipient>{
	public int compare(Recipient o1, Recipient o2) {
		if (o1.getName().compareTo(o2.getName()) > 0) return 1;
		if (o1.getName().compareTo(o2.getName()) == 0) return 0;
		return -1;
	}
}

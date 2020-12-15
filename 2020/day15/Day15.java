import java.util.HashMap;
import java.util.Map;

public class Day15 {

	static Map<Integer, int[]> l = new HashMap<>(10000);
	static {
		l.put(10, new int[]{-1, 1});
		l.put(16, new int[]{-1, 2});
		l.put(6, new int[]{-1, 3});
		l.put(0, new int[]{-1, 4});
		l.put(1, new int[]{-1, 5});
		l.put(17, new int[]{-1, 6});
	}
	static int last = 17;

	static void add(int key, int pos) {
		last = key;
		int[] places = l.get(key);
		if (places == null) {
			l.put(key, new int[]{-1, pos});
		} else {
			places[0] = places[1];
			places[1] = pos;
		}
	}

	public static void main(String[] args) {
		int counter = l.size();
		while (counter < 30000000) {
			counter++;
			int[] places = l.get(last);
			if (places[0] == -1) {
				add(0, counter);
			} else {
				add(places[1] - places[0], counter);
			}
			if (counter == 2020) {
				System.out.println(last);
			}
		}
		System.out.println(last);
	}
}

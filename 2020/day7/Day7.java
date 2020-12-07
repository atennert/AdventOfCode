import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 {
	private static final Map<Rule, Set<Rule>> RULES = new HashMap<>();
	private static final Map<Rule, Set<Rule>> REVERSE_RULES = new HashMap<>();

	public static void main(String[] args) {
		try (Scanner in = new Scanner(new FileInputStream("C:\\data.txt"))) {
			while (in.hasNextLine()) {
				addRule(in.nextLine());
			}

			Rule toSearch = new Rule("shiny", "gold", 1);
			System.out.println(containingBags(toSearch).size());
			System.out.println(requiredBags(toSearch));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void addRule(String ruleString) {
		String[] parts = ruleString.split("contain ");

		// key
		String[] keyParts = parts[0].split(" ");
		Rule key = new Rule(keyParts[0], keyParts[1], 1);

		if (parts[1].startsWith("no")) {
			RULES.put(key, Collections.emptySet());
			return;
		}

		Set<Rule> values = Arrays.stream(parts[1].split(", "))
				.map(p -> p.split(" "))
				.map(p -> new Rule(p[1], p[2], Integer.parseInt(p[0])))
				.peek(r -> {
					if (!REVERSE_RULES.containsKey(r)) {
						REVERSE_RULES.put(r, new HashSet<>());
					}
					REVERSE_RULES.get(r).add(key);
				})
				.collect(Collectors.toSet());

		RULES.put(key, values);
	}

	private static Set<Rule> containingBags(Rule rule) {
		Set<Rule> rules = REVERSE_RULES.get(rule);
		if (rules == null) {
			return Collections.emptySet();
		}
		Set<Rule> result = new HashSet<>(rules);
		for (Rule r: rules) {
			result.addAll(containingBags(r));
		}
		return result;
	}

	private static int requiredBags(Rule rule) {
		Set<Rule> rules = RULES.get(rule);
		int result = 0;
		for (Rule r : rules) {
			result += r.count * (requiredBags(r) + 1);
		}
		return result;
	}

	private static class Rule {
		final String variant;
		final String color;
		final int count;

		Rule(String variant, String color, int count) {
			this.variant = variant;
			this.color = color;
			this.count = count;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Rule rule = (Rule) o;
			return variant.equals(rule.variant) &&
					color.equals(rule.color);
		}

		@Override
		public int hashCode() {
			return Objects.hash(variant, color);
		}
	}
}

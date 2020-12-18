import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 {
	public static void main(String[] args) {
		try (Scanner in = new Scanner(new FileInputStream("C:\\data.txt"))) {
			long sum1 = 0;
			long sum2 = 0;
			while (in.hasNextLine()) {
				String line = in.nextLine();
				sum1 += parse1(new ArrayList<>(Arrays.asList(line.split(" "))));
				sum2 += parse2(line);
			}
			System.out.println(sum1);
			System.out.println(sum2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	static long parse1(List<String> line) {
		long lastNum = 0;
		String lastOp = "+";
		while (!line.isEmpty()) {
			String next = line.get(0);
			if (next.startsWith("(")) {
				line.set(0, next.substring(1));
				lastNum = op(lastNum, parse1(line), lastOp);
				continue;
			}
			if (next.endsWith(")")) {
				if (next.length() <= 2) {
					line.remove(0);
				} else {
					line.set(0, next.substring(1, next.length() - 1));
				}
				if (next.startsWith(")")) {
					return lastNum;
				}
				return op(lastNum, Integer.parseInt(next.substring(0,1)), lastOp);
			}

			switch (next) {
				case "+":
				case "*":
					lastOp = next;
					break;
				default:
					lastNum = op(lastNum, Integer.parseInt(next), lastOp);
			}
			line.remove(0);
		}
		return lastNum;
	}

	static long op(long a, long b, String op) {
		switch (op) {
			case "+": return a+b;
			case "*": return a*b;
			default: throw new IllegalArgumentException(op);
		}
	}

	static final Pattern ADD = Pattern.compile("\\d+ \\+ \\d+");
	static final Pattern MUP = Pattern.compile("\\(\\d+( \\* \\d+)+\\)");
	static final Pattern PAR = Pattern.compile("\\(\\d+\\)");
	static final Pattern MUL = Pattern.compile("\\d+ \\* \\d+");
	static long parse2(String line) {
		while (true) {
			Matcher addMatcher = ADD.matcher(line);
			if (addMatcher.find()) {
				String add = addMatcher.group();
				String[] addParts = add.split(" ");
				String result = Long.toString(Long.parseLong(addParts[0]) + Long.parseLong(addParts[2]));
				line = addMatcher.replaceFirst(result);
				System.out.println("ADD " + line);
				continue;
			}

			Matcher mupMatcher = MUP.matcher(line);
			if (mupMatcher.find()) {
				String mul = mupMatcher.group();
				String[] mulParts = mul.substring(1, mul.length() - 1).split(" ");
				long lres = 1;
				for (int i = 0; i < mulParts.length; i+=2)
					lres *= Long.parseLong(mulParts[i]);
				String result = Long.toString(lres);
				line = mupMatcher.replaceFirst(result);
				System.out.println("MUP " + line);
				continue;
			}

			Matcher parMatcher = PAR.matcher(line);
			boolean found = false;
			while (parMatcher.find()) {
				found = true;
				String par = parMatcher.group();
				String result = par.substring(1, par.length() - 1);
				line = parMatcher.replaceFirst(result);
				System.out.println("PAR " + line);
				parMatcher = PAR.matcher(line);
			}
			if (found) continue;

			Matcher mulMatcher = MUL.matcher(line);
			if (mulMatcher.find()) {
				String mul = mulMatcher.group();
				String[] mulParts = mul.split(" ");
				String result = Long.toString(Long.parseLong(mulParts[0]) * Long.parseLong(mulParts[2]));
				line = mulMatcher.replaceFirst(result);
				System.out.println("MUL " + line);
				continue;
			}
			break;
		}
		return Long.parseLong(line);
	}
}

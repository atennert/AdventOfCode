import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day8 {
	public static void main(String[] args) {
		try (Scanner in = new Scanner(new FileInputStream("day8/code.txt"))) {
			List<String> commands = new ArrayList<>(650);
			while (in.hasNextLine()) {
				commands.add(in.nextLine());
			}
			System.out.println(new Computer().run(commands));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	static class Computer {
		Set<Integer> runCommands = new HashSet<>();
		long accumulator = 0;
		int jmpNopPos = -1;

		long run(List<String> commands) {
			int commandIndex = 0;
			String command = commands.get(commandIndex);
			jmpNopPos = findNextJmpNop(commands);

			do {
				if (runCommands.contains(commandIndex)) {
					System.out.println("LOOP -> " + jmpNopPos);
					jmpNopPos = findNextJmpNop(commands);
					commandIndex = 0;
					accumulator = 0;
					command = commands.get(commandIndex);
					runCommands.clear();
				}
				String cmd = command;
				if (commandIndex == jmpNopPos) {
					if (command.startsWith("jmp")) {
						cmd = command.replace("jmp", "nop");
					} else if (command.startsWith("nop")) {
						cmd = command.replace("nop", "jmp");
					}
				}
				runCommands.add(commandIndex);
				commandIndex += evaluate(cmd);
				if (commandIndex >= commands.size()) {
					break;
				}
				command = commands.get(commandIndex);
			} while (true);
			System.out.println(jmpNopPos);
			return accumulator;
		}

		int findNextJmpNop(List<String> commands) {
			for (int i = jmpNopPos+1; i<commands.size(); i++) {
				String c = commands.get(i);
				if (c.startsWith("nop") || c.startsWith("jmp")) {
					return i;
				}
			}
			throw new IllegalStateException("No more jmp/nop found");
		}

		int evaluate(String command) {
			String[] parts = command.split(" ");
			switch (parts[0]) {
				case "acc":
					accumulator += Integer.parseInt(parts[1]);
					return 1;
				case "jmp":
					return Integer.parseInt(parts[1]);
				case "nop":
					return 1;
				default:
					throw new UnsupportedOperationException(command);
			}
		}
	}
}

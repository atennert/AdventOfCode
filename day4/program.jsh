import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public Map<String, Predicate<String>> requiredFields = new HashMap<>();
requiredFields.put("byr",
        y -> {try {int iy = Integer.parseInt(y);return 1920 <= iy && iy <= 2002;} catch(Exception e) {return false;}});
requiredFields.put("iyr",
        y -> {try {int iy = Integer.parseInt(y);return 2010 <= iy && iy <= 2020;} catch(Exception e) {return false;}});
requiredFields.put("eyr",
        y -> {try {int iy = Integer.parseInt(y);return 2020 <= iy && iy <= 2030;} catch(Exception e) {return false;}});
requiredFields.put("hgt",
        s -> {
    if (s.endsWith("cm")) {
        String value = s.substring(0, s.length() - 2);
        try {int iv = Integer.parseInt(value);return 150 <= iv && iv <= 193;} catch(Exception e) {return false;}
    } else if (s.endsWith("in")) {
        String value = s.substring(0, s.length() - 2);
        try {int iv = Integer.parseInt(value);return 59 <= iv && iv <= 76;} catch(Exception e) {return false;}
    }
    return false;
});
requiredFields.put("hcl", hcl -> hcl.matches("#[a-f0-9]{6}"));
requiredFields.put("ecl", ecl -> ecl.matches("(amb|blu|brn|gry|grn|hzl|oth)"));
requiredFields.put("pid", pid -> pid.matches("\\d{9}"));
//requiredFields.add("cid");

public static boolean check1(Map<String, String> currentFields) {
    return currentFields.keySet().containsAll(requiredFields.keySet());
}

public static boolean check2(Map<String, String> currentFields) {
    for (Map.Entry<String, Predicate<String>> check : requiredFields.entrySet()) {
        if (!currentFields.containsKey(check.getKey()) || !check.getValue().test(currentFields.get(check.getKey()))) {
            return false;
        }
    }
    return true;
}

try (Scanner sc = new Scanner(new FileInputStream("C:\\data.txt"))) {
    Map<String, String> currentFields = new HashMap<>();
    int validPassportCount1 = 0;
    int validPassportCount2 = 0;

    while (sc.hasNextLine()) {
        String line = sc.nextLine();
        if (line.isEmpty()) {
            if (check1(currentFields)) validPassportCount1++;
            if (check2(currentFields)) validPassportCount2++;
            currentFields.clear();
            continue;
        }
        Arrays.stream(line.split(" "))
            .map(field -> field.split(":"))
            .forEach(field -> currentFields.put(field[0], field[1]));
    }
    if (check1(currentFields)) validPassportCount1++;
    if (check2(currentFields)) validPassportCount2++;

    System.out.println(validPassportCount1);
    System.out.println(validPassportCount2);
}

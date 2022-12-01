import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day16 {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(new FileInputStream("C:\\data.txt"))) {
            Map<String, Boundary> boundaries = new HashMap<>();
            Set<List<Integer>> otherTickets = new HashSet<>();
            String inputLine;
            String[] splitLine;
            while (true) {
            // read ticket boundaries
                inputLine = in.nextLine();
                if (inputLine.isEmpty()) {
                    break;
                }
                splitLine = inputLine.split(": ");
                boundaries.put(splitLine[0], new Boundary(splitLine[1]));
            }
            // read my ticket
            in.nextLine();
            splitLine = in.nextLine().split(",");
            List<Integer> myTicket = Arrays.stream(splitLine).map(Integer::parseInt).collect(Collectors.toList());

            // read other tickets
            in.nextLine();
            in.nextLine();
            while (in.hasNextLine()) {
                splitLine = in.nextLine().split(",");
                otherTickets.add(Arrays.stream(splitLine).map(Integer::parseInt).collect(Collectors.toList()));
            }

            System.out.println(getErrorRate(otherTickets, boundaries));

            Set<List<Integer>> validTickets = filterFaultyTickets(otherTickets, boundaries);
            Map<String, List<Integer>> relations = findPossibleRelations(validTickets, boundaries);
            Map<String, Integer> cleanReleations = cleanUpReleations(relations);
            long product = cleanReleations.entrySet().stream()
                    .filter(e -> e.getKey().startsWith("departure"))
                    .map(e -> (long) myTicket.get(e.getValue()))
                    .reduce((a, b) -> a * b).get();
            System.out.println(product);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int getErrorRate(Set<List<Integer>> otherTickets, Map<String, Boundary> boundaries) {
        Collection<Boundary> boundaryValues = boundaries.values();
        return otherTickets.stream()
                .flatMap(List::stream)
                .map(v -> errorValue(v, boundaryValues))
                .reduce(0, Integer::sum);
    }

    private static int errorValue(Integer value, Collection<Boundary> boundaryValues) {
        for (Boundary b : boundaryValues) {
            if (b.isInside(value)) {
                return 0;
            }
        }
        return value;
    }

    private static Set<List<Integer>> filterFaultyTickets(Set<List<Integer>> otherTickets, Map<String, Boundary> boundaries) {
        Collection<Boundary> boundaryValues = boundaries.values();
        return otherTickets.stream()
                .filter(t -> {
                    for (Integer v : t) {
                        if (errorValue(v, boundaryValues) != 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toSet());
    }

    private static Map<String, List<Integer>> findPossibleRelations(Set<List<Integer>> otherTickets, Map<String, Boundary> boundaries) {
        Map<String, List<Integer>> result = new HashMap<>();
        int size = boundaries.size();
        for (int i = 0; i < size; i++) {
            for (Map.Entry<String, Boundary> entry : boundaries.entrySet()) {
                boolean works = true;
                for (List<Integer> ticket : otherTickets) {
                    if (!entry.getValue().isInside(ticket.get(i))) {
                        works = false;
                        break;
                    }
                }
                if (works) {
                    if (!result.containsKey(entry.getKey())) {
                        result.put(entry.getKey(), new ArrayList<>());
                    }
                    result.get(entry.getKey()).add(i);
                }
            }
        }
        return result;
    }

    private static Map<String, Integer> cleanUpReleations(Map<String, List<Integer>> relations) {
        Map<String, Integer> result = new HashMap<>();
        while (!relations.isEmpty()) {
            Map.Entry<String, List<Integer>> theEntry = null;
            for (Map.Entry<String, List<Integer>> entry : relations.entrySet()) {
                if (entry.getValue().size() == 1) {
                    theEntry = entry;
                    break;
                }
            }
            result.put(theEntry.getKey(), theEntry.getValue().get(0));
            for (List<Integer> values : relations.values()) {
                if (values.size() > 1) {
                    values.remove(theEntry.getValue().get(0));
                }
            }
            relations.remove(theEntry.getKey());
        }
        return result;
    }

    private static class Boundary {
        final int lowerMin;
        final int lowerMax;
        final int upperMin;
        final int upperMax;

        Boundary(String input) {
            String[] ulParts = input.split(" or ");
            String[] lParts = ulParts[0].split("-");
            String[] uParts = ulParts[1].split("-");
            lowerMin = Integer.parseInt(lParts[0]);
            lowerMax = Integer.parseInt(lParts[1]);
            upperMin = Integer.parseInt(uParts[0]);
            upperMax = Integer.parseInt(uParts[1]);
        }

        boolean isInside(int value) {
            return lowerMin <= value && value <= lowerMax ||
            upperMin <= value && value <= upperMax;
        }
    }
}

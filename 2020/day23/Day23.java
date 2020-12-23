public class Day23 {
    public static void main(String[] args) {
        String input = args[0];
        int size = input.length();

        int[] cups = new int[size];
        int i = 0;
        for (String s : input.split("")) {
            cups[i++] = Integer.parseInt(s);
        }
        int rounds = 100;

        int[] result = run(cups, rounds, size);
        adjustTo1(result, size);
        System.out.println();
        for (int cup : result) {
            System.out.print(cup);
        }
        System.out.println();
    }

    private static int[] run(int[] input, int rounds, int size) {
        int[] cups = input.clone();

        int current, n1, n2, n3, dest, destIdx = 0;
        for (int i = 0; i < rounds; i++) {
            if (i%1000 == 0) {
                System.out.print(i + "\r");
            }

            current = cups[0];
            n1 = cups[1];
            n2 = cups[2];
            n3 = cups[3];

            dest = getDestination(current, n1, n2, n3, size);
            for (int j = 0; j < size; j++) {
                if (cups[j] == dest) {
                    destIdx = j;
                    break;
                }
            }

            if (destIdx != 0) {
                shiftLeft(cups, 3, destIdx + 1);
                cups[destIdx-2] = n1;
                cups[destIdx-1] = n2;
                cups[destIdx] = n3;
            }
            shiftLeft(cups, 1, size);
            cups[size-1] = current;
        }

        return cups;
    }

    private static int getDestination(int current, int n1, int n2, int n3, int size) {
        int dest = current;
        do {
            dest -= 1;
            if (dest == 0) {
                dest = size;
            }
        } while (dest == n1 || dest == n2 || dest == n3);
        return dest;
    }

    private static void shiftLeft(int[] cups, int shiftSize, int length) {
        for (int i = 0; i < (length - shiftSize); i++){
            cups[i] = cups[i+shiftSize];
        }
    }

    private static void adjustTo1(int[] array, int size) {
        int tmp;
        while (array[0] != 1) {
            tmp = array[0];
            shiftLeft(array, 1, size);
            array[size-1] = tmp;
        }
        tmp = array[0];
        shiftLeft(array, 1, size);
        array[size-1] = tmp;
    }
}
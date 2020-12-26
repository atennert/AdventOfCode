import java.io.FileInputStream;
import java.util.Scanner;

public class Day25 {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(new FileInputStream("day25/keys.txt"))) {
            long pk1 = in.nextInt();
            long pk2 = in.nextInt();

            long ls1 = getLoopSize(pk1);

            long encryptionKey = getEcryptionKey(pk2, ls1);

            System.out.println(encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getLoopSize(long pk) {
        long val = 1;
        long ls = 0;
        while (val != pk) {
            val = transform(val, 7);
            ls++;
        }
        return ls;
    }

    private static long getEcryptionKey(long pk, long ls) {
        long val = 1;
        for (int i = 0; i < ls; i++) {
            val = transform(val, pk);
        }
        return val;
    }

    private static long transform(long val, long sn) {
        return (val * sn) % 20201227;
    }
}
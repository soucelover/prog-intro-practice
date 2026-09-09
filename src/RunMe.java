import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Run this code with provided arguments.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@SuppressWarnings("all")
public final class RunMe {
    private RunMe() {
        // Utility class
    }

    public static void main(final String[] args) {
        final byte[] password = parseArgs(args);

        // flag0(password);
        // System.out.println(
        // "The first flag was low-hanging fruit, can you find others?");
        // System.out.println(
        // "Try to read, understand and modify code in flagX(...) functions");

        // flag1(password);
        // flag2(password);
        // flag3(password);
        // flag4(password);
        // flag5(password);
        // flag6(password);
        // flag7(password);
        // flag8(password);
        // flag9(password);
        // flag10(password);
        // flag11(password);
        // flag12(password);
        // flag13(password);
        flag14(password);
        // flag15(password);
        flag16(password);
        flag17(password);
        flag18(password);
        flag19(password);
        flag20(password);
        flag21(password);
        // flag22(password);
        flag23(password);
    }

    private static void flag0(final byte[] password) {
        // The result of print(...) function depends only on explicit arguments
        print(0, 0, password);
    }

    private static void flag1(final byte[] password) {
        print(1, -7207502475243343743L, password);
    }

    private static void flag2(final byte[] password) {

        print(2, 5724057204750275434L, password);
    }

    private static void flag3(final byte[] password) {
        int result = 0;
        for (int i = 0; i < 2026; i++) {
            for (int j = 0; j < 2026; j++) {
                for (int k = 0; k < 2026; k++) {
                    for (int p = 0; p < 20; p++) {
                        result ^= (i * 7) | (j + k * 17) & ~p;
                        result ^= result << 1;
                    }
                }
            }
        }

        print(3, result, password);
    }

    private static void flag4(final byte[] password) {
        final long target = 4375034750245743233L + getInt(password);
        print(4, 4375034753694253116L, password);
        // for (long i = 0; i < Long.MAX_VALUE; i++) {
        // if ((i ^ (i >>> 30)) == target) {
        // print(4, i, password);
        // }
        // }
    }

    /* package-private */ static final long PRIME = 2026_2026_09;

    private static void flag5(final byte[] password) {
        // Rewrite!
        final long n = 1_000_000_000_000_000L + getInt(password);

        System.out.print(n);

        long result = 906702860;

        print(5, result, password);

        // result = 0;
        // for (long i = 0; i < n; i++) {
        // result = (result + i / 3 + i / 6 + i / 9 + i / 2026) % PRIME;
        // }

        // print(5, result, password);
    }

    private static final long flag5_helper(final long n, final int r) {
        return ((n - 1) / r) * ((n - 1) / r + 1) / 2 * r - (r - 1 - ((n - 1) % r)) * ((n - 1) / r);
    }

    private static void flag6(final byte[] password) {
        long result = 67923473294L ^ password[3] ^ password[4];
        print(6, result, password);
    }

    private static void flag7(final byte[] password) {
        // Count the number of occurrences of the most frequent noun at the following
        // page:
        // https://docs.oracle.com/javase/specs/jls/se25/html/jls-3.html

        // The singular form of the most frequent noun
        final String singular = "character";
        // The plural form of the most frequent noun
        final String plural = "characters";
        // The total number of occurrences (both singular and plural)
        final int total = 125 + 124;

        print(7, (singular + ":" + plural + ":" + total).hashCode(), password);
    }

    private static void flag8(final byte[] password) {
        // Count the number of bluish (#005162) pixels of this image:
        // https://www.oracle.com/a/pr/img/rc24-java-26.jpg

        BufferedImage image;

        try {
            URL url = new URL("https://www.oracle.com/a/pr/img/rc24-java-26.jpg");

            image = ImageIO.read(url);

        } catch (Exception e) {
            System.err.println("Couldn't download the image: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (image == null) {
            System.out.println("Failed to decode image: Format not supported by ImageIO.");
            return;
        }

        final int width = image.getWidth();
        final int height = image.getHeight();
        int total = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int rgb = image.getRGB(x, y);

                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                if (red == 0x00 && green == 0x51 && blue == 0x62) {
                    ++total;
                }
            }
        }

        print(8, total, password);
    }

    private static final String PATTERN = "You might be surprised how helpful the documentation can be!";
    private static final int SMALL_REPEAT_COUNT = 12_345_678;

    private static void flag9(final byte[] password) {
        // Compute (PATTERN * SMALL_REPEAT_COUNT).hash_code()
        final int single_hash = PATTERN.hashCode();
        int hash = 0;

        for (int i = 0; i < SMALL_REPEAT_COUNT; ++i) {
            for (int j = 0; j < PATTERN.length(); ++j) {
                hash *= 31;
            }

            hash += single_hash;
        }

        print(9, hash, password);
    }

    private static final long LARGE_REPEAT_SHIFT = 28;
    private static final long LARGE_REPEAT_COUNT = 1L << LARGE_REPEAT_SHIFT;

    private static void flag10(final byte[] password) {
        // LARGE_REPEAT_COUNT is a power of two, so...
        int hash = PATTERN.hashCode();
        final long shift = pow31(PATTERN.length());

        for (long i = 1; i < LARGE_REPEAT_COUNT; i <<= 1) {
            hash = hash * pow31(PATTERN.length() * i) + hash;
        }

        print(10, hash, password);
    }

    private static int pow31(final long exp) {
        int result = 1;
        long n = exp;

        while (n > 0) {
            if ((n & 1) != 0) {
                result *= 31;
            }

            result *= result;
            n >>= 1;
        }

        return result;
    }

    private static void flag11(final byte[] password) {
        print(11, 420394032, password);
    }

    private static void flag12(final byte[] password) {
        final long year = -2026;
        final long term = PRIME + Math.abs(getInt(password)) % PRIME;

        long result = 0;

        for (long i = 1; true; i++) {
            final long value = (year * i + term) * i;

            if (value <= 10) {
                break;
            }

            result += i * password[(int) (i % password.length)];
        }

        print(12, result, password);
    }

    private static final long MAX_DEPTH = 100_000_000L;

    private static void flag13(final byte[] password) {
        long result = 0;

        for (int i = 0; i < MAX_DEPTH; ++i) {
            result = (result ^ PRIME) | (result << 3) + i * 7;
        }

        print(13, result, password);
    }

    private static void flag14(final byte[] password) {
        final Instant lecture = Instant.parse("2026-09-08T15:30:00Z");
        final BigInteger hours = BigInteger
                .valueOf(Duration.between(Instant.EPOCH, lecture).toHours() + password[1] + password[3]);
        final BigInteger minutes = hours.multiply(BigInteger.valueOf(60));

        final long result = Stream.iterate(BigInteger.ZERO, minutes::add)
                .reduce(BigInteger.ZERO, BigInteger::add)
                .longValue();

        print(14, result, password);
    }

    private static void flag15(final byte[] password) {
        // REDACTED
        print(15, 5534753422343243423L + password[2], password);
    }

    private static void flag16(final byte[] password) {
        byte[] a = {
                (byte) (password[0] + password[3]),
                (byte) (password[1] + password[4]),
                (byte) (password[2] + password[5])
        };

        for (long i = 1_000_000_000_000_000_000L + getInt(password); i >= 0; i--) {
            flag16Update(a);
        }

        print(16, flag16Result(a), password);
    }

    /* package-private */ static void flag16Update(byte[] a) {
        a[0] ^= a[1];
        a[1] -= a[1] * a[2];
        a[2] += a[0];
    }

    /* package-private */ static int flag16Result(byte[] a) {
        return (a[0] + " " + a[1] + " " + a[2]).hashCode();
    }

    /**
     * Original idea by Alexei Shishkin.
     */
    private static void flag17(final byte[] password) {
        final int n = Math.abs(getInt(password) % 2026) + 2026;
        print(17, calc17(n), password);
    }

    /**
     * Write me
     * 
     * <pre>
     *          0: iconst_0
     *          1: istore_1
     *          2: iload_1
     *          3: iload_1
     *          4: imul
     *          5: sipush        2026
     *          8: idiv
     *          9: iload_0
     *         10: iadd
     *         11: sipush        10000
     *         14: if_icmpge     23
     *         17: iinc          1, 1
     *         20: goto          2
     *         23: iload_1
     *         24: ireturn
     * </pre>
     */
    private static int calc17(final int n) {
        return n;
    }

    private static void flag18(final byte[] password) {
        final int n = 2026 + getInt(password) % 2026;
        // Find the number of factors of n! modulo PRIME
        final int factors = 0;
        if (factors != 0) {
            print(18, factors, password);
        }
    }

    private static void flag19(final byte[] password) {
        // Let n = 2026e26 + abs(getInt(password)).
        // Consider the sequence of numbers (n + i) ** 2.
        // Instead of each number, we write the number that is obtained from it by
        // discarding the last 26 digits.
        // How many of the first numbers of the resulting sequence will form an
        // arithmetic progression?
        final long result = 0;
        if (result != 0) {
            print(19, result, password);
        }
    }

    /**
     * Original idea by Dmitrii Liapin.
     */
    private static void flag20(final byte[] password) {
        final Collection<Long> longs = new Random(getInt(password)).longs(2026_000)
                .map(n -> n % 1000)
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));

        // Calculate the number of objects (recursively) accessible by "longs"
        // reference.
        final int result = 0;

        if (result != 0) {
            print(20, result, password);
        }
    }

    /**
     * Original idea and implementation Igor Panasyuk.
     */
    private static void flag21(final byte[] password) {
        record Pair(int x, int y) {
        }

        final List<Object> items = new ArrayList<>(Arrays.asList(
                Byte.toUnsignedInt(password[0]),
                (long) getInt(password),
                new Pair(password[1], password[2]),
                "Java SE 21 " + Arrays.toString(password)));

        for (int round = 0; round < 10; round++) {
            for (final Object item : List.copyOf(items)) {
                // TODO: complete the switch expression using Java 21 features:
                // items.add(
                // case Integer i -> square of i as long
                // case Long l and l is even -> l ^ 0x21L
                // case Long l and l is odd -> -l
                // case Pair(int x, int y) -> x << 8 ^ y
                // case String s -> s.hashCode()
                // default -> 0
                // );
            }
        }

        long result = 0;
        for (final Object item : items) {
            result = result * 17 + item.toString().hashCode();
        }

        print(21, result, password);
    }

    /**
     * Is your Java modern enough?
     */
    private static void flag22(final byte[] password) {
        int result = 0;
        abstract class Point {
            final int x;
            final int y;

            public Point(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        class Point2 extends Point {
            Point2(int x, int y) {
                super(10, 20);
            }
        }

        class Point3 extends Point {
            final int z;

            Point3(int x, int y) {
                super(x, y);
                z = x * y;
            }
        }

        var p2 = new Point2(password[0], password[1]);
        var p3 = new Point3(password[2], password[3]);
        for (int i = 0; i < 1000 + password[4] + password[5]; i++) {
            p2 = new Point2(p2.x + p3.z, p2.y + p3.z);
            p3 = new Point3(p3.x + p3.z, p3.z + p3.z);
        }
        result = p2.x ^ p2.y ^ p3.x ^ p3.y ^ p3.z;

        print(22, result, password);
    }

    /**
     * Once again, the documentation turns out to be very useful!
     * <p>
     * Original idea Ivan Gorobets.
     */
    private static void flag23(final byte[] password) {
        final Random random = new Random(getInt(password));

        long result = 0;
        for (long i = 0; i < 2026_000_000_000_000_000L; i++) {
            result = random.nextLong();
        }

        print(23, result, password);
    }

    /**
     * Original idea Ivan Gorobets.
     */
    private static void flag24(final byte[] password) {
        final BigInteger n = new BigInteger(
                "73102acaa9b83688a52b4722c902e59a83bd94e8044b4257bbdf2b71f94186abde99e2833b60dcaf94eb737a8de77001b48f45b01c6dad65ba6e296c6bade4999a069bbc9dcad87a99c93c4b85902b3418765237c7a2c8664f8079dfcfc2da9a4b72ef73dfac30ed28dfe16779e689f510e4b64b27e69abec921beddbf044379",
                16);
        final BigInteger d = new BigInteger(
                "2c89f0a00bb25f11cb9b849aa22aff75a495c81515168ac1f20982a964489476c0f38ee872a073e824375bf12f1ac7639ad562b0641531dc475167f4f1f6968512f8ed86424db6463b013b3652ae7b77d55d494c004e6a35a93e18268a950752752e3a3c9fdd9d7f7092f8b8c11d7f4a1f07270cc7b5f92db05ac5147c840c81",
                16);
        // Find me
        final BigInteger e = new BigInteger("1", 16);

        final BigInteger b = new BigInteger(
                "5d7e2c071eb993fe1abef31605b1ce50d7a02e07a386844337af7fb3e500d4db3e67d60879c520f9e0a4aa80fe406547fbb79db398b32493e54839f480eed0add2e5a771920170d766876529f01f45297c20a364beeddb7d2fe879b224c0129d444ebd7dca2bb98c4a3070fff6860027d9080373c7f059adc5495254f8ad8d78",
                16);
        final BigInteger r = BigInteger.valueOf(2026_000_000L + getInt(password));
        final BigInteger encrypted = b.multiply(r.modPow(d, n)).mod(n);
        final BigInteger decrypted = encrypted.modPow(e, n);
        print(24, decrypted.hashCode(), password);
    }

    // ---------------------------------------------------------------------------------------------------------------
    // You may ignore all code below this line.
    // It is not required to get any of the flags.
    // ---------------------------------------------------------------------------------------------------------------

    private static void print(final int no, long result, final byte[] password) {
        System.out.format("flag %d: https://www.kgeorgiy.info/courses/prog-intro/hw1/%s%n", no, flag(result, password));
    }

    /* package-private */ static String flag(long result, byte[] password) {
        final byte[] flag = password.clone();
        for (int i = 0; i < 6; i++) {
            flag[i] ^= result;
            result >>>= 8;
        }

        return flag(flag);
    }

    /* package-private */ static String flag(final byte[] data) {
        final MessageDigest messageDigest = RunMe.DIGEST.get();
        messageDigest.update(SALT);
        messageDigest.update(data);
        messageDigest.update(SALT);
        final byte[] digest = messageDigest.digest();

        return IntStream.range(0, 6)
                .map(i -> (((digest[i * 2] & 255) << 8) + (digest[i * 2 + 1] & 255)) % KEYWORDS.size())
                .mapToObj(KEYWORDS::get)
                .collect(Collectors.joining("-"));
    }

    /* package-private */ static byte[] parseArgs(final String[] args) {
        if (args.length != 6) {
            throw error("Expected 6 command line arguments, found: %d", args.length);
        }

        final byte[] bytes = new byte[args.length];
        for (int i = 0; i < args.length; i++) {
            final Byte value = VALUES.get(args[i].toLowerCase(Locale.US));
            if (value == null) {
                throw error("Expected keyword, found: %s", args[i]);
            }
            bytes[i] = value;
        }
        return bytes;
    }

    private static AssertionError error(final String format, final Object... args) {
        System.err.format(format, args);
        System.err.println();
        System.exit(1);
        throw new AssertionError();
    }

    /* package-private */ static int getInt(byte[] password) {
        return IntStream.range(0, password.length)
                .map(i -> password[i])
                .reduce((a, b) -> a * KEYWORDS.size() + b)
                .getAsInt();
    }

    private static final ThreadLocal<MessageDigest> DIGEST = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new AssertionError("Cannot create SHA-256 digest", e);
        }
    });

    public static final byte[] SALT = "TwethJuabFew9OvOolv3RoiphthojophEynfach6".getBytes(StandardCharsets.US_ASCII);

    private static final List<String> KEYWORDS = List.of(
            "abstract",
            "assert",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "new",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "instanceof",
            "int",
            "interface",
            "long",
            "native",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extends",
            "final",
            "finally",
            "float",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "try",
            "void",
            "volatile",
            "while",
            "record",
            "Error",
            "AssertionError",
            "OutOfMemoryError",
            "StackOverflowError",
            "ArrayIndexOutOfBoundsException",
            "ArrayStoreException",
            "AutoCloseable",
            "Character",
            "CharSequence",
            "ClassCastException",
            "Comparable",
            "Exception",
            "IllegalArgumentException",
            "IllegalStateException",
            "IndexOutOfBoundsException",
            "Integer",
            "Iterable",
            "Math",
            "Module",
            "NegativeArraySizeException",
            "NullPointerException",
            "Number",
            "NumberFormatException",
            "Object",
            "Override",
            "RuntimeException",
            "StrictMath",
            "String",
            "StringBuilder",
            "StringIndexOutOfBoundsException",
            "SuppressWarnings",
            "System",
            "Thread",
            "Throwable",
            "ArithmeticException",
            "ClassLoader",
            "ClassNotFoundException",
            "Cloneable",
            "Deprecated",
            "FunctionalInterface",
            "InterruptedException",
            "Process",
            "ProcessBuilder",
            "Runnable",
            "SafeVarargs",
            "StackTraceElement",
            "Runtime",
            "ThreadLocal",
            "UnsupportedOperationException");

    private static final Map<String, Byte> VALUES = IntStream.range(0, KEYWORDS.size())
            .boxed()
            .collect(Collectors.toMap(index -> KEYWORDS.get(index).toLowerCase(Locale.US), Integer::byteValue));
}

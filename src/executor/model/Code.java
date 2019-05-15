package executor.model;

public class Code {
    private final static String sample =
                    "sub main\n" +
                    "   set a 1\n" +
                    "   call foo\n" +
                    "   print a\n" +
                    "   print a\n" +
                    "sub foo\n" +
                    "   set a 2\n";

    public static String getSample() {
        return sample;
    }
}

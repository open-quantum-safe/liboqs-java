import org.openquantumsafe.*;

public class RandExample {

    public static void main(String[] args) {
        String os = System.getProperty("os.name");
        if (!os.equals("Windows")) { // OQS note: Windows not yet supported, but leaving this here for when we do
            Rand.randombytes_switch_algorithm("OpenSSL");
            System.out.println("OpenSSL:\t\t" + Common.to_hex(Rand.randombytes(32)));
        }

        Rand.randombytes_switch_algorithm("system");
        System.out.println("System (default):\t" + Common.to_hex(Rand.randombytes(32)));
    }

}

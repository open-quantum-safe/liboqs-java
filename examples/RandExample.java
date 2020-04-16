import org.openquantumsafe.*;

public class RandExample {

    public static void main(String[] args) {
        Rand.randombytes_switch_algorithm("NIST-KAT");
        byte[] entropy_seed = new byte[48];
        entropy_seed[0] = (byte) 100;
        entropy_seed[20] = (byte) 200;
        entropy_seed[47] = (byte) 150;
        Rand.randombytes_nist_kat_init(entropy_seed);
        System.out.println("NIST-KAT:\t\t" + Common.to_hex(Rand.randombytes(32)));

        String os = System.getProperty("os.name");
        if (!os.equals("Windows")) {
            Rand.randombytes_switch_algorithm("OpenSSL");
            System.out.println("OpenSSL:\t\t" + Common.to_hex(Rand.randombytes(32)));
        }

        Rand.randombytes_switch_algorithm("system");
        System.out.println("System (default):\t" + Common.to_hex(Rand.randombytes(32)));
    }

}

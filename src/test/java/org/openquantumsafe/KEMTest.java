package org.openquantumsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class KEMTest {

    private static ArrayList<String> enabled_kems;

    /**
     * Before running the tests, get a list of enabled KEMs
     */
    @BeforeAll
    public static void init(){
        System.out.println("Initialize list of enabled KEMs");
        enabled_kems = KEMs.get_enabled_KEMs();
    }

    /**
     * Test all enabled KEMs
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getEnabledKEMsAsStream")
    public void testAllKEMs(String kem_name) {
        StringBuilder sb = new StringBuilder();
        sb.append(kem_name);
        sb.append(String.format("%1$" + (40 - kem_name.length()) + "s", ""));

        // Create client and server
        KeyEncapsulation client = new KeyEncapsulation(kem_name);
        KeyEncapsulation server = new KeyEncapsulation(kem_name);

        // Generate client key pair
        byte[] client_public_key = client.generate_keypair();

        // Server: encapsulate secret with client's public key
        Pair<byte[], byte[]> server_pair = server.encap_secret(client_public_key);
        byte[] ciphertext = server_pair.getLeft();
        byte[] shared_secret_server = server_pair.getRight();

        // Client: decapsulate
        byte[] shared_secret_client = client.decap_secret(ciphertext);

        // Check if equal
        assertArrayEquals(shared_secret_client, shared_secret_server, kem_name);

        // If successful print KEM name, otherwise an exception will be thrown
        sb.append("\033[0;32m").append("PASSED").append("\033[0m");
        System.out.println(sb.toString());
    }

    /**
     * Test KEMs with derandomized keypair generation.
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getDerandSupportedKEMsAsStream")
    public void testKEMsWithDerand(String kem_name) {
        StringBuilder sb = new StringBuilder();
        sb.append(kem_name);
        sb.append(" (derand)");
        sb.append(String.format("%1$" + (40 - kem_name.length() - 9) + "s", ""));

        // Create client and server
        KeyEncapsulation client = new KeyEncapsulation(kem_name);
        KeyEncapsulation server = new KeyEncapsulation(kem_name);

        // Generate seed
        byte[] seed = Rand.randombytes(client.get_keypair_seed_length());

        // Generate client key pair
        byte[] client_public_key = client.generate_keypair(seed);

        // Server: encapsulate secret with client's public key
        Pair<byte[], byte[]> server_pair = server.encap_secret(client_public_key);
        byte[] ciphertext = server_pair.getLeft();
        byte[] shared_secret_server = server_pair.getRight();

        // Client: decapsulate
        byte[] shared_secret_client = client.decap_secret(ciphertext);

        // Check if equal
        assertArrayEquals(shared_secret_client, shared_secret_server, kem_name);

        // If successful print KEM name, otherwise an exception will be thrown
        sb.append("\033[0;32m").append("PASSED").append("\033[0m");
        System.out.println(sb.toString());
    }

    /**
     * Test the MechanismNotSupported Exception
     */
    @Test
    public void testUnsupportedKEMExpectedException() {
        Assertions.assertThrows(MechanismNotSupportedError.class, () -> new KeyEncapsulation("MechanismNotSupported"));
    }

    /**
     * Method to convert the list of KEMs to a stream for input to testAllKEMs
     */
    private static Stream<String> getEnabledKEMsAsStream() {
        return enabled_kems.parallelStream();
    }

    /**
     * Method to convert the list of derand-supported KEMs to a stream for input to testAllSigs
     */
    private static Stream<String> getDerandSupportedKEMsAsStream() {
        return Arrays.asList(
                "ML-KEM-512", "ML-KEM-768", "ML-KEM-1024"
            ).parallelStream();
    }
}

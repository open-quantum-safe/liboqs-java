package org.openquantumsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.lang.reflect.Method;
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
     * Test that disposing of a KEM twice is harmless
     */
    @Test
    public void testDisposeKEMTwice() {
        KeyEncapsulation client = new KeyEncapsulation("ML-KEM-512");
        client.generate_keypair();

        client.dispose_KEM();
        client.dispose_KEM();
    }

    /**
     * Test that a KEM cannot be used after it has been disposed of
     */
    @Test
    public void testUseAfterDisposeKEMExpectedException() {
        KeyEncapsulation client = new KeyEncapsulation("ML-KEM-512");
        byte[] client_public_key = client.generate_keypair();
        byte[] ciphertext = client.encap_secret(client_public_key).getLeft();
        byte[] seed = Rand.randombytes(client.get_keypair_seed_length());

        client.dispose_KEM();

        assertDisposed(() -> client.generate_keypair());
        assertDisposed(() -> client.generate_keypair(seed));
        assertDisposed(() -> client.encap_secret(client_public_key));
        assertDisposed(() -> client.decap_secret(ciphertext));
        assertDisposed(() -> client.get_keypair_seed_length());
        assertDisposed(() -> client.export_public_key());
        assertDisposed(() -> client.export_secret_key());
        assertDisposed(() -> client.print_KeyEncapsulation());
        assertDisposed(() -> client.print_details());
    }

    private static void assertDisposed(Executable executable) {
        IllegalStateException e = Assertions.assertThrows(IllegalStateException.class, executable);
        Assertions.assertEquals("KeyEncapsulation has been disposed", e.getMessage());
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
     * Test that a null algorithm name is rejected like an unsupported one
     */
    @Test
    public void testNullKEMNameExpectedException() {
        Assertions.assertThrows(MechanismNotSupportedError.class, () -> new KeyEncapsulation(null));
    }

    /**
     * Test that a null algorithm name leaves the existing mechanism in place
     */
    @Test
    public void testCreateKEMNewWithNullName() {
        KeyEncapsulation client = new KeyEncapsulation("ML-KEM-512");
        KeyEncapsulation server = new KeyEncapsulation("ML-KEM-512");
        byte[] client_public_key = client.generate_keypair();

        client.create_KEM_new(null);

        Pair<byte[], byte[]> server_pair = server.encap_secret(client_public_key);
        assertArrayEquals(client.decap_secret(server_pair.getLeft()), server_pair.getRight());
    }

    /**
     * Test that generate_keypair() produces a usable key pair when the secret
     * key passed to the constructor has the wrong length. ML-KEM-512 secret
     * keys are 1632 bytes long.
     */
    @ParameterizedTest(name = "Testing secret key length {arguments}")
    @ValueSource(ints = {0, 32, 4096})
    public void testGenerateKeypairWithWrongLengthSecretKey(int secret_key_len) {
        KeyEncapsulation client = new KeyEncapsulation("ML-KEM-512", new byte[secret_key_len]);
        KeyEncapsulation server = new KeyEncapsulation("ML-KEM-512");
        byte[] client_public_key = client.generate_keypair();

        Pair<byte[], byte[]> server_pair = server.encap_secret(client_public_key);
        assertArrayEquals(client.decap_secret(server_pair.getLeft()), server_pair.getRight());
    }

    /**
     * Test that generate_keypair(seed) produces a usable key pair when the
     * secret key passed to the constructor has the wrong length
     */
    @ParameterizedTest(name = "Testing secret key length {arguments}")
    @ValueSource(ints = {0, 32, 4096})
    public void testGenerateKeypairDerandWithWrongLengthSecretKey(int secret_key_len) {
        KeyEncapsulation client = new KeyEncapsulation("ML-KEM-512", new byte[secret_key_len]);
        KeyEncapsulation server = new KeyEncapsulation("ML-KEM-512");
        byte[] seed = Rand.randombytes(client.get_keypair_seed_length());
        byte[] client_public_key = client.generate_keypair(seed);

        Pair<byte[], byte[]> server_pair = server.encap_secret(client_public_key);
        assertArrayEquals(client.decap_secret(server_pair.getLeft()), server_pair.getRight());
    }

    /**
     * Test that native keypair generation refuses key buffers too short to
     * hold the keys instead of writing past their end
     */
    @Test
    public void testNativeGenerateKeypairRejectsShortBuffers() throws Exception {
        KeyEncapsulation kem = new KeyEncapsulation("ML-KEM-512");
        int pk_len = kem.export_public_key().length;
        int sk_len = kem.export_secret_key().length;
        Method generate_keypair = KeyEncapsulation.class.getDeclaredMethod(
                                    "generate_keypair", byte[].class, byte[].class);
        generate_keypair.setAccessible(true);

        Assertions.assertEquals(-1, (int) generate_keypair.invoke(kem, new byte[pk_len - 1], new byte[sk_len]));
        Assertions.assertEquals(-1, (int) generate_keypair.invoke(kem, new byte[pk_len], new byte[sk_len - 1]));
        Assertions.assertEquals(0, (int) generate_keypair.invoke(kem, new byte[pk_len], new byte[sk_len]));
    }

    /**
     * Test that native derandomized keypair generation refuses key buffers too
     * short to hold the keys, and a seed too short to read from
     */
    @Test
    public void testNativeGenerateKeypairDerandRejectsShortBuffers() throws Exception {
        KeyEncapsulation kem = new KeyEncapsulation("ML-KEM-512");
        int pk_len = kem.export_public_key().length;
        int sk_len = kem.export_secret_key().length;
        int seed_len = (int) kem.get_keypair_seed_length();
        Method generate_keypair_derand = KeyEncapsulation.class.getDeclaredMethod(
                                    "generate_keypair_derand", byte[].class, byte[].class, byte[].class);
        generate_keypair_derand.setAccessible(true);

        Assertions.assertEquals(-1, (int) generate_keypair_derand.invoke(kem,
                                    new byte[pk_len - 1], new byte[sk_len], new byte[seed_len]));
        Assertions.assertEquals(-1, (int) generate_keypair_derand.invoke(kem,
                                    new byte[pk_len], new byte[sk_len - 1], new byte[seed_len]));
        Assertions.assertEquals(-1, (int) generate_keypair_derand.invoke(kem,
                                    new byte[pk_len], new byte[sk_len], new byte[seed_len - 1]));
        Assertions.assertEquals(0, (int) generate_keypair_derand.invoke(kem,
                                    new byte[pk_len], new byte[sk_len], new byte[seed_len]));
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

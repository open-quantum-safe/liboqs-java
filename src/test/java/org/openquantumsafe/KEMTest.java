package org.openquantumsafe;

import org.openquantumsafe.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class KEMTest {

    public void test_kem(String kem_name) {
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
        String reset = "\033[0m";
        try {
            assertArrayEquals(shared_secret_client, shared_secret_server);
        } catch (AssertionError e) {
            String red = "\033[0;31m";
            sb.append(red + "FAIL" + reset);
            System.out.println(sb.toString());
            throw e;
        }
        String green = "\033[0;32m";
        sb.append(green + "PASSED" + reset);
        System.out.println(sb.toString());
    }

    @Test
    public void test_all_kems() {
        System.out.println();
        ArrayList<String> enabled_kems = KEMs.get_enabled_KEMs();
        enabled_kems.parallelStream().forEach((kem_name) -> {
            test_kem(kem_name);
        });
    }

    @Test(expected = MechanismNotSupportedError.class)
    public void test_unsupported_kem() {
        KeyEncapsulation test = new KeyEncapsulation("MechanismNotSupported");
    }

}

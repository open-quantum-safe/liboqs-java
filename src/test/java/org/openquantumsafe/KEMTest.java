package org.openquantumsafe;

import org.openquantumsafe.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class KEMTest {

    private final int spaces = 40;

    public void test_kem(String kem_name) {
        System.out.print(kem_name);
        System.out.format("%1$" + (spaces - kem_name.length()) + "s", "");

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
        try {
            assertArrayEquals(shared_secret_client, shared_secret_server);
        } catch (AssertionError e) {
            System.out.println("FAIL");
            throw e;
        }
        System.out.println("PASSED");
    }

    @Test
    public void test_all_kems() {
        System.out.println();
        ArrayList<String> enabled_kems = KEMs.get_enabled_KEMs();

        for (String kem_name : enabled_kems) {
            test_kem(kem_name);
        }
    }

    @Test(expected = MechanismNotSupportedError.class)
    public void test_unsupported_kem() {
        KeyEncapsulation test = new KeyEncapsulation("MechanismNotSupported");
    }

}

package org.openquantumsafe;

import org.openquantumsafe.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class SigTest {

    public void test_sig(String sig_name, byte[] message) {
        StringBuilder sb = new StringBuilder();
        sb.append(sig_name);
        sb.append(String.format("%1$" + (40 - sig_name.length()) + "s", ""));

        // Create signer and verifier
        Signature signer = new Signature(sig_name);
        Signature verifier = new Signature(sig_name);

        // Generate signer key pair
        byte[] signer_public_key = signer.generate_keypair();

        // Sign the message
        byte[] signature = signer.sign(message);

        // Verify the signature
        boolean is_valid = verifier.verify(message, signature, signer_public_key);
        String reset = "\033[0m";
        try {
            assertTrue(is_valid);
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
    public void test_all_sigs() {
        System.out.println();
        byte[] message = "This is the message to sign".getBytes();
        ArrayList<String> enabled_sigs = Sigs.get_enabled_sigs();
        enabled_sigs.parallelStream().forEach((sig_name) -> {
            test_sig(sig_name, message);
        });
    }

    @Test(expected = MechanismNotSupportedError.class)
    public void test_unsupported_sig() {
        Signature test = new Signature("MechanismNotSupported");
    }

}

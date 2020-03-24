package org.openquantumsafe;

import org.openquantumsafe.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class SigTest {

    private final int spaces = 40;

    public void test_sig(String sig_name, byte[] message) {
        System.out.print(sig_name);
        System.out.format("%1$" + (spaces - sig_name.length()) + "s", "");

        // Create signer and verifier
        Signature signer = new Signature(sig_name);
        Signature verifier = new Signature(sig_name);

        // Generate signer key pair
        byte[] signer_public_key = signer.generate_keypair();

        // Sign the message
        byte[] signature = signer.sign(message);

        // Verify the signature
        boolean is_valid = verifier.verify(message, signature, signer_public_key);

        try {
            assertTrue(is_valid);
        } catch (AssertionError e) {
            System.out.println("FAIL");
            throw e;
        }
        System.out.println("PASSED");
    }

    @Test
    public void test_all_sigs() {
        byte[] message = "This is the message to sign".getBytes();

        System.out.println();
        ArrayList<String> enabled_sigs = Sigs.get_enabled_sigs();

        for (String sig_name : enabled_sigs) {
            test_sig(sig_name, message);
        }
    }

    @Test(expected = MechanismNotSupportedError.class)
    public void test_unsupported_sig() {
        Signature test = new Signature("MechanismNotSupported");
    }

}

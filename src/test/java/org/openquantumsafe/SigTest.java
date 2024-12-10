package org.openquantumsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.stream.Stream;

public class SigTest {

    private final byte[] message = "This is the message to sign".getBytes();
    private static ArrayList<String> enabled_sigs;

    /**
     * Before running the tests, get a list of enabled Sigs
     */
    @BeforeAll
    public static void init(){
        System.out.println("Initialize list of enabled Signatures");
        enabled_sigs = Sigs.get_enabled_sigs();
    }

    /**
     * Test all enabled Sigs
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getEnabledSigsAsStream")
    public void testAllSigs(String sig_name) {
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

        assertTrue(is_valid, sig_name);

        // If successful print Sig name, otherwise an exception will be thrown
        sb.append("\033[0;32m").append("PASSED").append("\033[0m");
        System.out.println(sb.toString());
    }
    
    
    /**
     * Test all enabled Sigs with context (if they don't support the context
     * it should fail gracefully)
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getEnabledSigsAsStream")
    public void testAllSigsWithContext(String sig_name) {
        StringBuilder sb = new StringBuilder();
        sb.append(sig_name);
        sb.append(String.format("%1$" + (40 - sig_name.length()) + "s", ""));

        // Create signer and verifier
        Signature signer = new Signature(sig_name);
        Signature verifier = new Signature(sig_name);

        byte[] sampleContext = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10};
        
        // Generate signer key pair
        byte[] signer_public_key = signer.generate_keypair();

        // Sign the message
        byte[] signature = signer.sign(message,sampleContext);

        // Verify the signature
        boolean is_valid = verifier.verify(message, signature, sampleContext, signer_public_key);

        assertTrue(is_valid, sig_name);

        // If successful print Sig name, otherwise an exception will be thrown
        sb.append("\033[0;32m").append("PASSED").append("\033[0m");
        System.out.println(sb.toString());
    }
    

    /**
     * Test the MechanismNotSupported Exception
     */
    @Test
    public void testUnsupportedSigExpectedException() {
        Assertions.assertThrows(MechanismNotSupportedError.class, () -> new Signature("MechanismNotSupported"));
    }

    /**
     * Method to convert the list of Sigs to a stream for input to testAllSigs
     */
    private static Stream<String> getEnabledSigsAsStream() {
        return enabled_sigs.parallelStream();
    }

}

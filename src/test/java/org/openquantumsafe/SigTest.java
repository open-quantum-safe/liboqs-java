package org.openquantumsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

// import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println("Enabled signatures: [" + enabled_sigs + "]" );
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
     * Test that disposing of a Sig twice is harmless
     */
    @Test
    public void testDisposeSigTwice() {
        Signature signer = new Signature("ML-DSA-44");
        signer.generate_keypair();

        signer.dispose_sig();
        signer.dispose_sig();
    }

    /**
     * Test that a Sig cannot be used after it has been disposed of
     */
    @Test
    public void testUseAfterDisposeSigExpectedException() {
        byte[] context = "01234567890".getBytes();
        Signature signer = new Signature("ML-DSA-44");
        byte[] signer_public_key = signer.generate_keypair();
        byte[] signature = signer.sign(message);

        signer.dispose_sig();

        assertDisposed(() -> signer.generate_keypair());
        assertDisposed(() -> signer.sign(message));
        assertDisposed(() -> signer.verify(message, signature, signer_public_key));
        assertDisposed(() -> signer.sign(message, context));
        assertDisposed(() -> signer.verify(message, signature, context, signer_public_key));
    }

    private static void assertDisposed(Executable executable) {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class, executable);
        Assertions.assertEquals("Signature has been disposed", e.getMessage());
    }

    /**
     * Test Sigs with context.
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getContextSupportedAlgsAsStream")
    public void testSigsWithContext(String sig_name) {
    	byte[] context = "01234567890".getBytes();
        StringBuilder sb = new StringBuilder();
        sb.append(sig_name);
        sb.append(String.format("%1$" + (40 - sig_name.length()) + "s", ""));

        // Create signer and verifier
        Signature signer = new Signature(sig_name);
        Signature verifier = new Signature(sig_name);
        
         // Generate signer key pair
        byte[] signer_public_key = signer.generate_keypair();

        // Sign the message
        byte[] signature = signer.sign(message, context);

        // Verify the signature
        boolean is_valid = verifier.verify(message, signature, context, signer_public_key);
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

    /**
     * Method to convert the list of ML-DSA Sigs to a stream for input to testAllSigs
     */
    private static Stream<String> getContextSupportedAlgsAsStream() {
    	return Arrays.asList(
                "ML-DSA-44", "ML-DSA-65", "ML-DSA-87"
            ).parallelStream();
    }
}

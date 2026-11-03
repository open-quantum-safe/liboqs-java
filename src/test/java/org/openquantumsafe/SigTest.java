package org.openquantumsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

// import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class SigTest {

    private final byte[] message = "This is the message to sign".getBytes();
    // The ML-DSA-*-extmu variants sign an externally computed 64-byte mu
    // instead of the message itself
    private final byte[] mu = Rand.randombytes(64);
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

        byte[] to_sign = sig_name.endsWith("-extmu") ? mu : message;

        // Sign the message
        byte[] signature = signer.sign(to_sign);

        // Verify the signature
        boolean is_valid = verifier.verify(to_sign, signature, signer_public_key);

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
        assertDisposed(() -> signer.export_public_key());
        assertDisposed(() -> signer.export_secret_key());
        assertDisposed(() -> signer.print_signature());
        assertDisposed(() -> signer.print_details());
    }

    private static void assertDisposed(Executable executable) {
        IllegalStateException e = Assertions.assertThrows(IllegalStateException.class, executable);
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
     * Test that a null context means no context
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getContextSupportedAlgsAsStream")
    public void testSigsWithNullContext(String sig_name) {
        byte[] context = "01234567890".getBytes();

        // Create signer and verifier
        Signature signer = new Signature(sig_name);
        Signature verifier = new Signature(sig_name);

        // Generate signer key pair
        byte[] signer_public_key = signer.generate_keypair();

        // A null context signature verifies as a signature without context
        byte[] signature = signer.sign(message, null);
        assertTrue(verifier.verify(message, signature, signer_public_key), sig_name);
        assertTrue(verifier.verify(message, signature, null, signer_public_key), sig_name);

        // A signature with a context does not verify under a null context
        byte[] context_signature = signer.sign(message, context);
        assertFalse(verifier.verify(message, context_signature, null, signer_public_key), sig_name);
    }

    /**
     * Test that a failed signing operation is reported as such. ML-DSA limits
     * the context to 255 bytes.
     */
    @ParameterizedTest(name = "Testing {arguments}")
    @MethodSource("getContextSupportedAlgsAsStream")
    public void testContextTooLongExpectedException(String sig_name) {
        Signature signer = new Signature(sig_name);
        signer.generate_keypair();

        RuntimeException e = Assertions.assertThrows(RuntimeException.class,
                                () -> signer.sign(message, new byte[256]));
        Assertions.assertEquals("Cannot sign message", e.getMessage());
    }

    /**
     * Test the MechanismNotSupported Exception
     */
    @Test
    public void testUnsupportedSigExpectedException() {
        Assertions.assertThrows(MechanismNotSupportedError.class, () -> new Signature("MechanismNotSupported"));
    }

    /**
     * Test that a null algorithm name is rejected like an unsupported one
     */
    @Test
    public void testNullSigNameExpectedException() {
        Assertions.assertThrows(MechanismNotSupportedError.class, () -> new Signature(null));
    }

    /**
     * Test that a null algorithm name leaves the existing mechanism in place
     */
    @Test
    public void testCreateSigNewWithNullName() {
        Signature signer = new Signature("ML-DSA-44");
        byte[] signer_public_key = signer.generate_keypair();

        signer.create_sig_new(null);

        assertTrue(signer.verify(message, signer.sign(message), signer_public_key));
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

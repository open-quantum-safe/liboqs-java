package oqs;

/**
 * \class oqs::KEMs
 * \brief Singleton class, contains details about supported/enabled key exchange
 * mechanisms (KEMs)
 */
public class KEMs {
    
    static { System.loadLibrary("oqs-jni"); }

    /**
     * Wrapper for OQS_API int OQS_KEM_alg_count(void);
     * \brief Maximum number of supported KEM algorithms
     * \return Maximum number of supported KEM algorithms
     */
    public static native int max_number_KEMs();
    
}

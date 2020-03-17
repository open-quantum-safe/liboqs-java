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
     *
     * \brief Maximum number of supported KEM algorithms
     * \return Maximum number of supported KEM algorithms
     */
    public static native int max_number_KEMs();

    /**
     * Wrapper for OQS_API int OQS_KEM_alg_is_enabled(const char *method_name);
     *
     * \brief Checks whether the KEM algorithm \a alg_name is enabled
     * \param alg_name Cryptographic algorithm name
     * \return True if the KEM algorithm is enabled, false otherwise
     */
    public static native boolean is_KEM_enabled(String alg_name);

    /**
     * Wrapper for OQS_API const char *OQS_KEM_alg_identifier(size_t i);
     * 
     * \brief KEM algorithm name
     * \param alg_id Cryptographic algorithm numerical id
     * \return KEM algorithm name
     */
    public static native String get_KEM_name(long alg_id);

}

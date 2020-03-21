package org.openquantumsafe;

import java.util.Arrays;

/**
 * \brief Cryptographic scheme not supported
 */
class MechanismNotSupportedError extends RuntimeException {
    
    public MechanismNotSupportedError(String alg_name) {
        super("\"" + alg_name + "\" is not supported by OQS");
    }
    
    public MechanismNotSupportedError(String alg_name, Throwable throwable) {
        super("\"" + alg_name + "\" is not supported by OQS", throwable);
    }
    
}

/**
 * \brief Cryptographic scheme not enabled
 */
class MechanismNotEnabledError extends RuntimeException {
    
    public MechanismNotEnabledError(String alg_name) {
        super("\"" + alg_name + "\" is not enabled by OQS");
    }
    
    public MechanismNotEnabledError(String alg_name, Throwable throwable) {
        super("\"" + alg_name + "\" is not enabled by OQS", throwable);
    }
    
}

/**
 * \brief Key Encapsulation Mechanisms
 */
public class KeyEncapsulation {

    /**
     * \brief KEM algorithm details
     */
    class KeyEncapsulationDetails {
    
        String method_name;
        String alg_version;
        byte claimed_nist_level;
        boolean ind_cca;
        long length_public_key;
        long length_secret_key;
        long length_ciphertext;
        long length_shared_secret;
    
        /**
         * \brief Print KEM algorithm details
         */
        void printKeyEncapsulation() {
            System.out.println("Key Encapsulation Details:");
            System.out.println("\tMethod name: " + this.method_name);
            System.out.println("\tVersion: " + this.alg_version);
            System.out.println("\tClaimed NIST level: " + this.claimed_nist_level);
            System.out.println("\tIs IND_CCA: " + this.ind_cca);
            System.out.println("\tLength public key (bytes): " + this.length_public_key);
            System.out.println("\tLength secret key (bytes): " + this.length_secret_key);
            System.out.println("\tLength ciphertext (bytes): " + this.length_ciphertext);
            System.out.println("\tLength shared secret (bytes): " + this.length_shared_secret);
        }
    
    }
    
    /**
     * Keep native pointers for Java to remember which C memory it is managing.
     */
    private long native_kem_handle_;
    private long native_public_key_handle_;
    private long native_secret_key_handle_;
    
    /**
     * Private object that has the KEM details.
     */
    private KeyEncapsulationDetails alg_details_;
    
    /**
     * \brief Constructs an instance of oqs::KeyEncapsulation
     * \param alg_name Cryptographic algorithm method_name
     */
    public KeyEncapsulation(String alg_name) throws RuntimeException {
        this(alg_name, null);
    }
    
    /**
     * \brief Constructs an instance of oqs::KeyEncapsulation
     * \param alg_name Cryptographic algorithm method_name
     * \param secret_key Secret key
     */
// FIXME: secret key
    public KeyEncapsulation(String alg_name, byte[] secret_key) throws RuntimeException {
        // if (secret_key != null) {
            // secret_key_ = Arrays.copyOf(secret_key, secret_key.length);
        // }
        // KEM not enabled
        if (!KEMs.is_KEM_enabled(alg_name)) {
            // perhaps it's supported
            if (KEMs.is_KEM_supported(alg_name)) {
                throw new MechanismNotEnabledError(alg_name);
            } else {
                throw new MechanismNotSupportedError(alg_name);
            }
        }
        create_KEM_new(alg_name);
        alg_details_ = get_KEM_details();
    }
        
    /**
     * \brief Wrapper for OQS_API OQS_KEM *OQS_KEM_new(const char *method_name).
     * Calls OQS_KEM_new and stores return value to native_kem_handle_.
     */
    public native void create_KEM_new(String method_name);
    
    /**
     * \brief Initialize and fill a KeyEncapsulationDetails object from the native
     * C struct pointed by native_kem_handle_. 
     */
    private native KeyEncapsulationDetails get_KEM_details();

    /**
     * \brief Wrapper for OQS_API OQS_STATUS OQS_KEM_keypair(const OQS_KEM *kem, uint8_t *public_key, uint8_t *secret_key);
     * \param Public key length
     * \param Secret key length
     * \return Status
     */
    private native int generate_keypair(long length_public_key, long length_secret_key);
    
    /**
     * \brief Export public key
     * \param Public key length
     * \return Public key
     */
    private native byte[] export_public_key(long length_public_key);
    
    /**
     * \brief Export secret key
     * \param Secret key length
     * \return Secret key
     */
    private native byte[] export_secret_key(long length_secret_key);
    
    /**
     * \brief Invoke native generate_keypair method using the PK and SK lengths form 
     * alg_details_. Check return value and if != 0 throw RuntimeException. 
     */
    public byte[] generate_keypair() throws RuntimeException {
        int rv_ = generate_keypair(alg_details_.length_public_key, alg_details_.length_secret_key);
        if (rv_ != 0) throw new RuntimeException("Cannot generate keypair");
        return export_public_key(alg_details_.length_public_key);
    }
    
    /**
     * \brief Invoke native export_public_key method using PK length form alg_details_.
     * \return Public key
     */
    public byte[] export_public_key() {
        return export_public_key(alg_details_.length_public_key);
    }
    
    /**
     * \brief Invoke native export_secret_key method using SK length form alg_details_.
     * \return Secret key
     */
    public byte[] export_secret_key() {
        return export_secret_key(alg_details_.length_secret_key);
    }
    
    /**
     * \brief Print KeyEncapsulation. If a KeyEncapsulationDetails object is not
     * initialized, initialize it and fill it using native C code.
     */
    public void print_KeyEncapsulation() {
        if (alg_details_ == null) {
            alg_details_ = get_KEM_details();
        }
        System.out.println("Key Encapsulation Mechanism: " + alg_details_.method_name);
    }

    /**
     * \brief print KEM algorithm details. If a KeyEncapsulationDetails object is
     * not initialized, initialize it and fill it using native C code.
     */
    public void print_details() {
        if (alg_details_ == null) {
            alg_details_ = get_KEM_details();
        }
        alg_details_.printKeyEncapsulation();
    }

}

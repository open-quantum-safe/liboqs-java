package oqs;

import oqs.*;


/**
 * \class MechanismNotSupportedError
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
 * \class oqs::MechanismNotEnabledError
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
 * \class KeyEncapsulation
 * \brief Key encapsulation mechanisms
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
         * \brief OutputStream extraction operator for the KEM algorithm details
         * \param os Output stream
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

    private KeyEncapsulationDetails alg_details_;
    
    private byte[] secret_key_;
    
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
    public KeyEncapsulation(String alg_name, byte[] secret_key) throws RuntimeException {
        secret_key_ = secret_key;
        
        // KEM not enabled
        if (!KEMs.is_KEM_enabled(alg_name)) {
            // perhaps it's supported
            if (KEMs.is_KEM_supported(alg_name)) {
                throw new MechanismNotEnabledError(alg_name);
            } else {
                throw new MechanismNotSupportedError(alg_name);
            }
        }
        alg_details_ = create_KEM_new(alg_name);
    }
        
    /**
     * Wrapper for OQS_API OQS_KEM *OQS_KEM_new(const char *method_name);
     *
     * \brief Callers should always check whether the return value is `NULL`, which indicates either than an
     * invalid algorithm method_name was provided, or that the requested algorithm was disabled at compile-time.
     *
     * \param method_name Name of the desired algorithm; one of the names in `OQS_KEM_algs`.
     * \return An OQS_KEM for the particular algorithm, or `NULL` if the algorithm has been disabled at compile-time.
     */
    public native KeyEncapsulationDetails create_KEM_new(String method_name);
    
    /**
     * \brief Generate public key/secret key pair
     * \return Public key
     */
    public byte[] generate_keypair() throws RuntimeException {
        // byte[] public_key(alg_details_.length_public_key, 0);
        // secret_key_ = byte[](alg_details_.length_secret_key, 0);
        // 
        // OQS_STATUS rv_ = C::OQS_KEM_keypair(kem_.get(), public_key.data(),
        //                                     secret_key_.data());
        // if (rv_ != OQS_STATUS::OQS_SUCCESS) {
        //     throw std::runtime_error("Can not generate keypair");
        // }
        // 
        // return public_key;
        return null;
    }
    
    /**
     * \brief OutputStream extraction operator for KeyEncapsulation
     * \param os Output stream
     * \param ke KeyEncapsulation instance
     */
    public void print_KeyEncapsulation() {
        System.out.println("Key encapsulation mechanism: " + this.alg_details_.method_name);
    }

    /**
     * \brief print KEM algorithm details
     */
    public void print_details() { 
        alg_details_.printKeyEncapsulation();
    }

}

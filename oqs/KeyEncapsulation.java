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
        
        String name;
        String version;
        long claimed_nist_level;
        boolean is_ind_cca;
        long length_public_key;
        long length_secret_key;
        long length_ciphertext;
        long length_shared_secret;
        
        /**
         * \brief KEM algorithm details constructor
         */
        KeyEncapsulationDetails(String name, String version, long claimed_nist_level, boolean is_ind_cca, long length_public_key, long length_secret_key, long length_ciphertext, long length_shared_secret) {
            this.name = name;
            this.version = version;
            this.claimed_nist_level = claimed_nist_level;
            this.is_ind_cca = is_ind_cca;
            this.length_public_key = length_public_key;
            this.length_secret_key = length_secret_key;
            this.length_ciphertext = length_ciphertext;
            this.length_shared_secret = length_shared_secret;
        }
        
        /**
         * \brief OutputStream extraction operator for the KEM algorithm details
         * \param os Output stream
         */
        void printKeyEncapsulation() {
            System.out.println("Key encapsulation mechanism: " + this.name);
            System.out.println("Name: " + this.name + "\n");
            System.out.println("Version: " + this.version + "\n");
            System.out.println("Claimed NIST level: " + this.claimed_nist_level + "\n");
            System.out.println("Is IND_CCA: " + this.is_ind_cca + "\n");
            System.out.println("Length public key (bytes): " + this.length_public_key + "\n");
            System.out.println("Length secret key (bytes): " + this.length_secret_key + "\n");
            System.out.println("Length ciphertext (bytes): " + this.length_ciphertext + "\n");
            System.out.println("Length shared secret (bytes): " + this.length_shared_secret);
        }
        
    }

    private KeyEncapsulationDetails alg_details_;
    
    private byte[] secret_key_;
    
    /**
     * \brief Constructs an instance of oqs::KeyEncapsulation
     * \param alg_name Cryptographic algorithm name
     */
    public KeyEncapsulation(String alg_name) throws RuntimeException {
        this(alg_name, null);
    }
    
    /**
     * \brief Constructs an instance of oqs::KeyEncapsulation
     * \param alg_name Cryptographic algorithm name
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
    
        // kem_.reset(C::OQS_KEM_new(alg_name.c_str()));

        // alg_details_ = new KeyEncapsulationDetails();
        // alg_details_.name = kem_->method_name;
        // alg_details_.version = kem_->alg_version;
        // alg_details_.claimed_nist_level = kem_->claimed_nist_level;
        // alg_details_.is_ind_cca = kem_->ind_cca;
        // alg_details_.length_public_key = kem_->length_public_key;
        // alg_details_.length_secret_key = kem_->length_secret_key;
        // alg_details_.length_ciphertext = kem_->length_ciphertext;
        // alg_details_.length_shared_secret = kem_->length_shared_secret;
    }
    
    /**
     * \brief KEM algorithm details
     * \return KEM algorithm details
     */
    public KeyEncapsulationDetails get_details() { return alg_details_; }

    
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
    public void printKeyEncapsulation() {
        System.out.println("Key encapsulation mechanism: " + this.alg_details_.name);
    }

}

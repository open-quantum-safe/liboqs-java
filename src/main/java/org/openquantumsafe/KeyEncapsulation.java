package org.openquantumsafe;

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
     * \brief Wrapper for OQS_API OQS_STATUS OQS_KEM_encaps(const OQS_KEM *kem, uint8_t *ciphertext, 
     *                                      uint8_t *shared_secret, const uint8_t *public_key);
     * \param Pair <ciphertext, shared secret>
     * \param Public key
     * \param length_ciphertext
     * \param length_shared_secret
     * \return Status
     */
    private native int encap_secret(Pair<byte[], byte[]> pair, byte[] public_key, long length_ciphertext, long length_shared_secret);
    
    
    /**
     * \brief Wrapper for OQS_API OQS_STATUS OQS_KEM_decaps(const OQS_KEM *kem, uint8_t *shared_secret,
     *                                      const unsigned char *ciphertext, const uint8_t *secret_key);
     * \param shared_secret
     * \param ciphertext
     * \return Status
     */
    private native int decap_secret(byte[] shared_secret, byte[] ciphertext);
    
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
     * \brief Invoke native encap_secret method
     * \param Public key
     * \return Pair <ciphertext, shared secret>
     */
    public Pair<byte[], byte[]> encap_secret(byte[] public_key) throws RuntimeException {
        if (public_key.length != alg_details_.length_public_key) {
            throw new RuntimeException("Incorrect public key length");        
        }
        Pair<byte[], byte[]> pair = new Pair<>(new byte[(int) alg_details_.length_ciphertext], new byte[(int) alg_details_.length_shared_secret]);
        int rv_= encap_secret(pair, public_key, alg_details_.length_ciphertext, alg_details_.length_shared_secret);
        if (rv_ != 0) throw new RuntimeException("Cannot encapsulate secret");
        return pair;
    }

    /**
     * \brief Decapsulate secret
     * \param ciphertext Ciphertext
     * \return Shared secret
     */
    public byte[] decap_secret(byte[] ciphertext) throws RuntimeException {
        if (ciphertext.length != alg_details_.length_ciphertext) {
            throw new RuntimeException("Incorrect ciphertext length");        
        }
        byte[] secret_key_ = export_secret_key(alg_details_.length_secret_key);
        if (secret_key_.length != alg_details_.length_secret_key) {
            throw new RuntimeException("Incorrect secret key length, make sure you specify one in the constructor or run generate_keypair()");        
        }
        byte[] shared_secret = new byte[(int) alg_details_.length_shared_secret];
        int rv_ = decap_secret(shared_secret, ciphertext);
        if (rv_ != 0) throw new RuntimeException("Cannot decapsulate secret");
        return shared_secret;
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

#include <oqs/oqs.h>
#include "KeyEncapsulation.h"
#include "handle.h"

/*
 * Class:     org_openquantumsafe_KeyEncapsulation
 * Method:    get_new_KEM
 * Signature: (Ljava/lang/String;)Loqs/KeyEncapsulation/KeyEncapsulationDetails;
 */
JNIEXPORT void JNICALL Java_org_openquantumsafe_KeyEncapsulation_create_1KEM_1new
  (JNIEnv *env, jobject obj, jstring java_str)
{
    // Create get a liboqs::OQS_KEM pointer 
    const char *_nativeString = (*env)->GetStringUTFChars(env, java_str, 0);
    OQS_KEM *kem = OQS_KEM_new(_nativeString);
    (*env)->ReleaseStringUTFChars(env, java_str, _nativeString);
    // Stow the native OQS_KEM pointer in the Java handle.
    setHandle(env, obj, kem, "native_kem_handle_");
}

/*
 * Class:     org_openquantumsafe_KeyEncapsulation
 * Method:    get_KEM_details
 * Signature: ()Lorg/openquantumsafe/KeyEncapsulation/KeyEncapsulationDetails;
 */
JNIEXPORT jobject JNICALL Java_org_openquantumsafe_KeyEncapsulation_get_1KEM_1details
  (JNIEnv *env, jobject obj)
{
    jclass cls = (*env)->FindClass(env, "org/openquantumsafe/KeyEncapsulation$KeyEncapsulationDetails");
    if (cls == NULL) { fprintf(stderr, "\nCould not find class\n"); return NULL; }
    
    // Get the Method ID of the constructor
    jmethodID org_openquantumsafe_oqs_ = (*env)->GetMethodID(env, cls, "<init>", "(Lorg/openquantumsafe/KeyEncapsulation;)V");
    if (NULL == org_openquantumsafe_oqs_) { fprintf(stderr, "\nCould not initialize class\n"); return NULL; }
    
    // Call back constructor to allocate a new instance, with an int argument
    jobject _nativeKED = (*env)->NewObject(env, cls, org_openquantumsafe_oqs_);
    
    OQS_KEM *kem = (OQS_KEM *) getHandle(env, obj, "native_kem_handle_");
    
    // Copy fields from C struct to Java class
    // String method_name;
    jfieldID _method_name = (*env)->GetFieldID(env, cls, "method_name", "Ljava/lang/String;");
    jstring j_method_name = (*env)->NewStringUTF(env, kem->method_name);
    (*env)->SetObjectField(env, _nativeKED, _method_name, j_method_name);
    
    // String alg_version;
    jfieldID _alg_version = (*env)->GetFieldID(env, cls, "alg_version", "Ljava/lang/String;");
    jstring j_alg_version = (*env)->NewStringUTF(env, kem->alg_version);
    (*env)->SetObjectField(env, _nativeKED, _alg_version, j_alg_version);
    
    // byte claimed_nist_level;
    jfieldID _claimed_nist_level = (*env)->GetFieldID(env, cls, "claimed_nist_level", "B");
    (*env)->SetByteField(env, _nativeKED, _claimed_nist_level, (jbyte) kem->claimed_nist_level);
    
    // boolean ind_cca;
    jfieldID _ind_cca = (*env)->GetFieldID(env, cls, "ind_cca", "Z");
    (*env)->SetBooleanField(env, _nativeKED, _ind_cca, (jboolean) kem->ind_cca);
    
    // long length_public_key;
    jfieldID _length_public_key = (*env)->GetFieldID(env, cls, "length_public_key", "J");
    (*env)->SetLongField(env, _nativeKED, _length_public_key, (jlong) kem->length_public_key);
    
    // long length_secret_key;
    jfieldID _length_secret_key = (*env)->GetFieldID(env, cls, "length_secret_key", "J");
    (*env)->SetLongField(env, _nativeKED, _length_secret_key, (jlong) kem->length_secret_key);
    
    // long length_ciphertext;
    jfieldID _length_ciphertext = (*env)->GetFieldID(env, cls, "length_ciphertext", "J");
    (*env)->SetLongField(env, _nativeKED, _length_ciphertext, (jlong) kem->length_ciphertext);
    
    // long length_shared_secret;
    jfieldID _length_shared_secret = (*env)->GetFieldID(env, cls, "length_shared_secret", "J");
    (*env)->SetLongField(env, _nativeKED, _length_shared_secret, (jlong) kem->length_shared_secret);
    
    return _nativeKED;
}

/*
 * Class:     org_openquantumsafe_KeyEncapsulation
 * Method:    generate_keypair
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_org_openquantumsafe_KeyEncapsulation_generate_1keypair
  (JNIEnv *env, jobject obj, jlong length_public_key, jlong length_secret_key)
{
    // Allocate space for the public key and stow it inside the java class
    uint8_t *public_key = (uint8_t *) calloc(length_public_key, sizeof(uint8_t));
    setHandle(env, obj, public_key, "native_public_key_handle_");
    
    // Allocate space for the secret key and stow it inside the java class
    uint8_t *secret_key = (uint8_t *) calloc(length_secret_key, sizeof(uint8_t));
    setHandle(env, obj, secret_key, "native_secret_key_handle_");
    
    // Get pointer to KEM
    OQS_KEM *kem = (OQS_KEM *) getHandle(env, obj, "native_kem_handle_");
    
    // Invoke liboqs KEM keypair generation function
    OQS_STATUS rv_ = OQS_KEM_keypair(kem, public_key, secret_key);
    return (rv_ == OQS_SUCCESS) ? 0 : -1;
}

/*
 * Class:     org_openquantumsafe_KeyEncapsulation
 * Method:    export_public_key
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_openquantumsafe_KeyEncapsulation_export_1public_1key
  (JNIEnv *env, jobject obj, jlong length_public_key)
{
    // retrieve C pointer to the public key
    uint8_t *secret_key = (uint8_t *) getHandle(env, obj, "native_public_key_handle_");

    // create a byte array for the public key that will be passed back to Java
    jbyteArray jpublic_key = (jbyteArray)(*env)->NewByteArray(env, length_public_key);
    if (jpublic_key == NULL) return NULL;
    // copy contents from C bytes to java byte array
    (*env)->SetByteArrayRegion(env, jpublic_key, 0, length_public_key, (jbyte*) secret_key);

    return jpublic_key;
}
  
/*
 * Class:     org_openquantumsafe_KeyEncapsulation
 * Method:    export_secret_key
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_openquantumsafe_KeyEncapsulation_export_1secret_1key
  (JNIEnv *env, jobject obj, jlong length_secret_key)
{
    // retrieve C pointer to the secret key
    uint8_t *secret_key = (uint8_t *) getHandle(env, obj, "native_secret_key_handle_");

    // create a byte array for the secret key that will be passed back to Java
    jbyteArray jsecret_key = (jbyteArray)(*env)->NewByteArray(env, length_secret_key);
    if (jsecret_key == NULL) return NULL;
    // copy contents from C bytes to java byte array
    (*env)->SetByteArrayRegion(env, jsecret_key, 0, length_secret_key, (jbyte*) secret_key);

    return jsecret_key;
}

/*
 * Class:     org_openquantumsafe_KeyEncapsulation
 * Method:    encap_secret
 * Signature: (Lorg/openquantumsafe/KeyEncapsulation/Pair;[BJJ)I
 */
JNIEXPORT jint JNICALL Java_org_openquantumsafe_KeyEncapsulation_encap_1secret
  (JNIEnv *env, jobject obj, jobject pair_obj, jbyteArray jpublic_key, jlong length_ciphertext, jlong length_shared_secret)
{
    // Convert public_key to jbyte array
    jsize len = (*env)->GetArrayLength(env, jpublic_key);
    jbyte *public_key = (*env)->GetByteArrayElements(env, jpublic_key, 0);
    
    // Allocate space for the ciphertext and the shared secret
    uint8_t *ciphertext = (uint8_t *) calloc(length_ciphertext, sizeof(uint8_t));
    uint8_t *shared_secret = (uint8_t *) calloc(length_shared_secret, sizeof(uint8_t));
    
    // Get pointer to KEM and invoke liboqs encapsulate secret function
    OQS_KEM *kem = (OQS_KEM *) getHandle(env, obj, "native_kem_handle_");
    OQS_STATUS rv_ = OQS_KEM_encaps(kem, ciphertext, shared_secret, public_key);
    
    // Release C public_key
    (*env)->ReleaseByteArrayElements(env, jpublic_key, public_key, JNI_ABORT);
    
    // Create new Java byte arrays, fill them with the ciphertext and the shared_secret, and free the C memory
    jbyteArray jctxt_arr = (jbyteArray)(*env)->NewByteArray(env, length_ciphertext);
    jbyteArray jshared_sec_arr = (jbyteArray)(*env)->NewByteArray(env, length_shared_secret);
    (*env)->SetByteArrayRegion(env, jctxt_arr, 0, length_ciphertext, (jbyte*) ciphertext);
    (*env)->SetByteArrayRegion(env, jshared_sec_arr, 0, length_shared_secret, (jbyte*) shared_secret);
    free(ciphertext);
    free(shared_secret);
    
    // Get the pair class of the input object and get field references
    jclass pair_class = (*env)->GetObjectClass(env, pair_obj);
    (*env)->SetObjectField(env, pair_obj, (*env)->GetFieldID(env, pair_class, "left", "Ljava/lang/Object;"), jctxt_arr);
    (*env)->SetObjectField(env, pair_obj, (*env)->GetFieldID(env, pair_class, "right", "Ljava/lang/Object;"), jshared_sec_arr);
    
    return (rv_ == OQS_SUCCESS) ? 0 : -1;
}

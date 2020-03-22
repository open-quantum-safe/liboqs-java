#include <oqs/oqs.h>
#include "Signature.h"
#include "handle.h"

/*
 * Class:     org_openquantumsafe_Signature
 * Method:    create_sig_new
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_openquantumsafe_Signature_create_1sig_1new
  (JNIEnv *env, jobject obj, jstring jstr)
{
    // Create get a liboqs::OQS_SIG pointer
    const char *_nativeString = (*env)->GetStringUTFChars(env, jstr, 0);
    OQS_SIG *sig = OQS_SIG_new(_nativeString);
    (*env)->ReleaseStringUTFChars(env, jstr, _nativeString);
    // Stow the native OQS_SIG pointer in the Java handle.
    setHandle(env, obj, sig, "native_sig_handle_");
}

/*
 * Class:     org_openquantumsafe_Signature
 * Method:    free_sig
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_openquantumsafe_Signature_free_1sig
  (JNIEnv *env, jobject obj, jlong length_secret_key)
{
    uint8_t *secret_key = (uint8_t *) getHandle(env, obj, "native_secret_key_handle_");
    if (secret_key != NULL) {
        OQS_MEM_cleanse(secret_key, length_secret_key);
    }
    OQS_SIG *sig = (OQS_SIG *) getHandle(env, obj, "native_kem_handle_");
    OQS_SIG_free(sig);
}

/*
 * Class:     org_openquantumsafe_Signature
 * Method:    get_sig_details
 * Signature: ()Lorg/openquantumsafe/Signature/SignatureDetails;
 */
JNIEXPORT jobject JNICALL Java_org_openquantumsafe_Signature_get_1sig_1details
  (JNIEnv *env, jobject obj)
{
    jclass cls = (*env)->FindClass(env, "org/openquantumsafe/Signature$SignatureDetails");
    if (cls == NULL) { fprintf(stderr, "\nCould not find class\n"); return NULL; }

    // Get the Method ID of the constructor
    jmethodID constructor_meth_id_ = (*env)->GetMethodID(env, cls, "<init>", "(Lorg/openquantumsafe/Signature;)V");
    if (NULL == constructor_meth_id_) { fprintf(stderr, "\nCould not initialize class\n"); return NULL; }

    // Call back constructor to allocate a new instance, with an int argument
    jobject _nativeKED = (*env)->NewObject(env, cls, constructor_meth_id_);

    OQS_SIG *sig = (OQS_SIG *) getHandle(env, obj, "native_sig_handle_");

    // Copy fields from C struct to Java class
    // String method_name;
    jfieldID _method_name = (*env)->GetFieldID(env, cls, "method_name", "Ljava/lang/String;");
    jstring j_method_name = (*env)->NewStringUTF(env, sig->method_name);
    (*env)->SetObjectField(env, _nativeKED, _method_name, j_method_name);

    // String alg_version;
    jfieldID _alg_version = (*env)->GetFieldID(env, cls, "alg_version", "Ljava/lang/String;");
    jstring j_alg_version = (*env)->NewStringUTF(env, sig->alg_version);
    (*env)->SetObjectField(env, _nativeKED, _alg_version, j_alg_version);

    // byte claimed_nist_level;
    jfieldID _claimed_nist_level = (*env)->GetFieldID(env, cls, "claimed_nist_level", "B");
    (*env)->SetByteField(env, _nativeKED, _claimed_nist_level, (jbyte) sig->claimed_nist_level);

    // boolean is_euf_cma;
    jfieldID _is_euf_cma = (*env)->GetFieldID(env, cls, "is_euf_cma", "Z");
    (*env)->SetBooleanField(env, _nativeKED, _is_euf_cma, (jboolean) sig->euf_cma);

    // long length_public_key;
    jfieldID _length_public_key = (*env)->GetFieldID(env, cls, "length_public_key", "J");
    (*env)->SetLongField(env, _nativeKED, _length_public_key, (jlong) sig->length_public_key);

    // long length_secret_key;
    jfieldID _length_secret_key = (*env)->GetFieldID(env, cls, "length_secret_key", "J");
    (*env)->SetLongField(env, _nativeKED, _length_secret_key, (jlong) sig->length_secret_key);

    // long max_length_signature;
    jfieldID _max_length_signature = (*env)->GetFieldID(env, cls, "max_length_signature", "J");
    (*env)->SetLongField(env, _nativeKED, _max_length_signature, (jlong) sig->length_signature);

    return _nativeKED;
}

/*
 * Class:     org_openquantumsafe_Signature
 * Method:    generate_keypair
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_org_openquantumsafe_Signature_generate_1keypair
  (JNIEnv *env, jobject obj, jlong length_public_key, jlong length_secret_key)
{
    // Allocate space for the public key and stow it inside the java class
    uint8_t *public_key = (uint8_t *) calloc(length_public_key, sizeof(uint8_t));
    setHandle(env, obj, public_key, "native_public_key_handle_");

    // Allocate space for the secret key and stow it inside the java class
    uint8_t *secret_key = (uint8_t *) calloc(length_secret_key, sizeof(uint8_t));
    setHandle(env, obj, secret_key, "native_secret_key_handle_");

    // Get pointer to sig
    OQS_SIG *sig = (OQS_SIG *) getHandle(env, obj, "native_sig_handle_");

    // Invoke liboqs sig keypair generation function
    OQS_STATUS rv_ = OQS_SIG_keypair(sig, public_key, secret_key);
    return (rv_ == OQS_SUCCESS) ? 0 : -1;
}

/*
 * Class:     org_openquantumsafe_Signature
 * Method:    import_secret_key
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_org_openquantumsafe_Signature_import_1secret_1key
  (JNIEnv *env, jobject obj, jbyteArray jsecret_key)
{
    // Convert java secret_key to jbyte array and store the pointer to it
    jbyte *secret_key_native = (*env)->GetByteArrayElements(env, jsecret_key, 0);
    setHandle(env, obj, secret_key_native, "native_secret_key_handle_");
}

/*
 * Class:     org_openquantumsafe_Signature
 * Method:    export_public_key
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_openquantumsafe_Signature_export_1public_1key
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
 * Class:     org_openquantumsafe_Signature
 * Method:    export_secret_key
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_openquantumsafe_Signature_export_1secret_1key
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
 * Class:     org_openquantumsafe_Signature
 * Method:    sign
 * Signature: ([BLjava/lang/Long;[BJ[B)I
 */
JNIEXPORT jint JNICALL Java_org_openquantumsafe_Signature_sign
  (JNIEnv * env, jobject obj, jbyteArray jsignature, jobject sig_len_obj,
      jbyteArray jmessage, jlong message_len, jbyteArray jsecret_key)
{
    // Convert to jbyte arrays
    jbyte *signature_native = (*env)->GetByteArrayElements(env, jsignature, 0);
    jbyte *message_native = (*env)->GetByteArrayElements(env, jmessage, 0);
    jbyte *secret_key_native = (*env)->GetByteArrayElements(env, jsecret_key, 0);

    OQS_SIG *sig = (OQS_SIG *) getHandle(env, obj, "native_sig_handle_");
    size_t len_sig;
    OQS_STATUS rv_ = OQS_SIG_sign(sig, signature_native, &len_sig, message_native,
                                    message_len, secret_key_native);

    // fill java signature bytes
    (*env)->SetByteArrayRegion(env, jsignature, 0, len_sig, (jbyte*) signature_native);

    // fill java object signature length
    jclass mutable_long_class = (*env)->GetObjectClass(env, sig_len_obj);
    jfieldID value_fid = (*env)->GetFieldID(env, mutable_long_class, "value", "J");
    (*env)->SetLongField(env, sig_len_obj, value_fid, (jlong) len_sig);

    // Release C memory
    (*env)->ReleaseByteArrayElements(env, jsignature, signature_native, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, jmessage, message_native, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, jsecret_key, secret_key_native, JNI_ABORT);

    return (rv_ == OQS_SUCCESS) ? 0 : -1;
}


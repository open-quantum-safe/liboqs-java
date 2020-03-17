#include <oqs/oqs.h>
#include "oqs_KEMs.h"

/*
 * Class:     oqs_KEMs
 * Method:    max_number_KEMs
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_oqs_KEMs_max_1number_1KEMs
  (JNIEnv *env, jclass cls)
{
    return (jint) OQS_KEM_alg_count();
}

/*
 * Class:     oqs_KEMs
 * Method:    is_KEM_enabled
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_oqs_KEMs_is_1KEM_1enabled
  (JNIEnv *env, jclass cls, jstring java_str)
{
	const char *_nativeString = (*env)->GetStringUTFChars(env, java_str, 0);
    int is_enabled = OQS_KEM_alg_is_enabled (_nativeString);
	(*env)->ReleaseStringUTFChars(env, java_str, _nativeString);
    return (is_enabled) ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     oqs_KEMs
 * Method:    get_KEM_name
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_oqs_KEMs_get_1KEM_1name
  (JNIEnv *env, jclass cls, jlong alg_id)
{
    const char *_nativeString = OQS_KEM_alg_identifier((size_t) alg_id);
    return (*env)->NewStringUTF(env, _nativeString);
}


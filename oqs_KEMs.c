#include <oqs/oqs.h>
#include "oqs_KEMs.h"

JNIEXPORT jint JNICALL Java_oqs_KEMs_max_1number_1KEMs
  (JNIEnv *env, jclass cls)
{
    int num = OQS_KEM_alg_count();
    // int num = 10;
    return (jint) num;
}

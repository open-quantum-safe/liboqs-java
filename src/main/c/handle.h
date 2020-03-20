#ifndef _HANDLE_H_INCLUDED_
#define _HANDLE_H_INCLUDED_

#include <oqs/oqs.h>

jfieldID getHandleField(JNIEnv *env, jobject obj) {
    jclass c = (*env)->GetObjectClass(env, obj);
    return (*env)->GetFieldID(env, c, "nativeHandle", "J");
}

// use long to store pointer
void *getHandle(JNIEnv *env, jobject obj) {
    jlong handle = (*env)->GetLongField(env, obj, getHandleField(env, obj));
    return (void *)(handle);
}

void setHandle(JNIEnv *env, jobject obj, void *t) {
    jlong handle = (jlong) (t);
    (*env)->SetLongField(env, obj, getHandleField(env, obj), handle);
}

#endif
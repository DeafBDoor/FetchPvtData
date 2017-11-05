#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_richard_fetchpvtdata_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_richard_fetchpvtdata_utils_SuCmdNative_nativeSuCopyFile(
        JNIEnv *env, jobject instance, jstring pvtPath_, jstring targetPath_) {
    const char *pvtPath = env->GetStringUTFChars(pvtPath_, 0);
    const char *targetPath = env->GetStringUTFChars(targetPath_, 0);



    env->ReleaseStringUTFChars(pvtPath_, pvtPath);
    env->ReleaseStringUTFChars(targetPath_, targetPath);

    return 0;
}
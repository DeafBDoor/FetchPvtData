#include <jni.h>
#include <string>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <android/log.h>

#define SU_PATH "/system/xbin/su"
#define SU_CMD "su"
#define SU_FLAG "-c"
#define CP_CMD "/system/bin/cp"

#define TAG "NativeFetchPvtData"

// Effectively exec the process which will execute su command
static void do_child_exec(const char *pvt_path, const char *target_path) {
    // Child do an execl on su to read file data
    if(execl(SU_PATH, SU_CMD, SU_FLAG, CP_CMD, pvt_path, target_path, (char *)NULL)) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "execl call failed!");
        exit(1); // if exec failed, process exit status must be non-zero
    }
}

// Fork a child, then call the function responsible for setting up
// child's process and exec'ing the su (and associated copy) command
// Child's return value will be fed directly to the parent java native method.
static jboolean internal_suCopyFile(const char *pvt_path, const char *target_path) {
    pid_t fork_ret;
    pid_t wait_ret;
    int wstatus;

    jboolean rv = JNI_FALSE;

    fork_ret = fork();
    if (!fork_ret) {
        do_child_exec(pvt_path, target_path);
    }

    // Parent process gets child's pid
    // Wait for the child return check its status.
    wait_ret = wait(&wstatus);
    if (wait_ret >= 0 && WIFEXITED(wstatus)) {
        if (!WEXITSTATUS(wstatus)) {
            rv = JNI_TRUE;
            goto out;
        } else {
            __android_log_print(ANDROID_LOG_ERROR, TAG, "Error executing su call");
            goto out;
        }
    }
    __android_log_print(ANDROID_LOG_ERROR, TAG, "Wait call error!");

    out:
    return rv;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_richard_fetchpvtdata_utils_SuCmdNative_nativeSuCopyFile(
        JNIEnv *env, jobject instance, jstring pvtPath_, jstring targetPath_) {
    const char *pvtPath = env->GetStringUTFChars(pvtPath_, 0);
    const char *targetPath = env->GetStringUTFChars(targetPath_, 0);

    jboolean rv = internal_suCopyFile(pvtPath, targetPath);

    env->ReleaseStringUTFChars(pvtPath_, pvtPath);
    env->ReleaseStringUTFChars(targetPath_, targetPath);

    return rv;
}
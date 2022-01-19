#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL Java_com_catfixture_virgloverlay_ui_activity_virgl_Virgl_dummy(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
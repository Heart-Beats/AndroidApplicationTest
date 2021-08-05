//
// Created by zhanglei on 2021/8/2.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_hl_nativelib_TestJNI_test(JNIEnv *env, jobject thiz) {
    std::string hello = "我是编写的测试原生代码";
    return env->NewStringUTF(hello.c_str());
}
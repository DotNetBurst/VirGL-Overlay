//THIS SHOULD BE INJECTED IN RENDERING CODE AND FULLY REWRITTEN!
#ifndef STATISTICS
#define STATISTICS

#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <epoxy/egl.h>
#include "../vtest/vtest_protocol.h"
#include <jni.h>
#include <string.h>

#include <android/log.h>
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "virgl", __VA_ARGS__)

struct Statistics {
    uint16_t fps;
    uint16_t fpsCount;
    __kernel_suseconds_t lastUpdateTime;
};

void CreateStatistics();
void DestroyStatistics();
void MeasureFPS();
void* Updater(void* arg);

#endif
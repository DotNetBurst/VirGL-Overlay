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

struct Statistics {
    uint16_t fps;
    uint16_t fpsCount;

    __kernel_suseconds_t lastUpdateTime;
    jmethodID UpdateStatisticsID;
    jclass klass;
};

struct Statistics * statistics;

void CreateStatistics(JNIEnv* env);
void MeasureFPS();
void UpdateStatistics(JNIEnv * env);

#endif
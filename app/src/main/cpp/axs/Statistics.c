#include "Statistics.h"


struct timeval tv;
uint16_t fps;
uint16_t fpsCount;
__kernel_time_t lastUpdateTime;

void MeasureFPS() {
    fpsCount++;
    gettimeofday(&tv,NULL);
    if ( tv.tv_sec - lastUpdateTime >= 1) {
        fps = (fpsCount + fps) / 2;
        fpsCount = 0;
        lastUpdateTime = tv.tv_sec;
    }
}

JNIEXPORT jint JNICALL
Java_com_catfixture_virgloverlay_core_overlay_StatisticsOverlay_GetFPS(JNIEnv *env, jclass clazz) {
    return fps;
}
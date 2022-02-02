#include "Statistics.h"
#include "TMPShrinker.h"

struct Statistics * statistics;
struct timeval tv;

void CreateStatistics(JNIEnv* env) {
    statistics = malloc(sizeof(struct Statistics));
    memset(statistics, 0, sizeof(statistics));

    statistics->klass  = (*env)->FindClass (env, "com/catfixture/virgloverlay/core/impl/android/NativeStatistics");
    statistics->UpdateStatisticsID = (*env)->GetStaticMethodID(env, statistics->klass, "UpdateStatistics", "(SJ)V");

    gettimeofday(&tv,NULL);
}

void MeasureFPS() {
    statistics->fpsCount++;
}

void UpdateStatistics(JNIEnv* env) {
    gettimeofday(&tv,NULL);
    if ( tv.tv_sec - statistics->lastUpdateTime >= 1) {
        statistics->fps = statistics->fpsCount;
        statistics->fpsCount = 0;

        (*env)->CallStaticVoidMethod(env, statistics->klass, statistics->UpdateStatisticsID,
             statistics->fps, 0);
        statistics->lastUpdateTime = tv.tv_sec;
    }
}
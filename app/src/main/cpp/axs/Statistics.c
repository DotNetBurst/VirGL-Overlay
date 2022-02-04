#include "Statistics.h"


struct timeval tv;
pthread_t statisticsMainThread;
bool isStopped = false;
struct Statistics * statistics = NULL;


void CreateStatistics() {
    printf("\nSTATS CREATED\n");
    pthread_create(&statisticsMainThread, NULL, &Updater, NULL);
    pthread_detach(statisticsMainThread);
}
void DestroyStatistics() {
    isStopped = true;
    pthread_join(statisticsMainThread, NULL);
}

void* Updater(void* arg) {
    statistics = malloc(sizeof(struct Statistics));
    memset(statistics, 0, sizeof(struct Statistics));
    gettimeofday(&tv,NULL);
    while (!isStopped) {
        statistics->fps = (statistics->fps + statistics->fpsCount) / 2;
        statistics->fpsCount = 0;
        usleep(1000000);
    }

    free(statistics);
}

void MeasureFPS() {
    statistics->fpsCount++;
}

JNIEXPORT jint JNICALL
Java_com_catfixture_virgloverlay_core_overlay_StatisticsOverlay_GetFPS(JNIEnv *env, jclass clazz) {
    if ( statistics != NULL)
        return statistics->fps;
    else return 0;
}
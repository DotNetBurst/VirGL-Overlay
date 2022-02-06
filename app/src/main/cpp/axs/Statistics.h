//THIS SHOULD BE INJECTED IN RENDERING CODE AND FULLY REWRITTEN!
#ifndef STATISTICS
#define STATISTICS

#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include "../vtest/vtest_protocol.h"
#include <jni.h>
#include <string.h>

void MeasureFPS();
void* Updater(void* arg);

#endif
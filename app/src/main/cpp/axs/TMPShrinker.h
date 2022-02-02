//THIS SHOULD BE INJECTED IN RENDERING CODE AND FULLY REWRITTEN!
#ifndef TMP_SHRINKER
#define TMP_SHRINKER

#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <epoxy/egl.h>
#include "../vtest/vtest_protocol.h"

uint8_t ApplyShrinking(int sx, int sy, int sw, int sh);


#endif
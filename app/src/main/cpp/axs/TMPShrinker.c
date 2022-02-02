//THIS SHOULD BE INJECTED IN RENDERING CODE AND FULLY REWRITTEN!
#include "TMPShrinker.h"

struct ConfigProfile* commonConfigProfile;

uint8_t ApplyShrinking(int sx, int sy, int sw, int sh) {
    int targetX = sx;
    int targetY = sy;
    int targetWidth = sw;
    int targetHeight = sh;

    if ( commonConfigProfile->useViewportShrink) {
        const int dw = commonConfigProfile->deviceWidth;
        const int dh = commonConfigProfile->deviceHeight;
        const int shw = commonConfigProfile->shrinkWidth;
        const int shh = commonConfigProfile->shrinkHeight;

        switch (commonConfigProfile->viewportShrinkType) {
            case 0: {
                targetWidth = shw;
                targetHeight = shh;

                if ( commonConfigProfile->centerViewportRect) {
                    targetX = dw / 2 - shw / 2;
                    targetY = dh / 2 - shh / 2;
                }
                break;
            }
            case 1: {
                targetWidth = dw;
                targetHeight = dh;
                break;
            }
            case 2: {
                targetWidth = dw - commonConfigProfile->navBarHeight;
                targetHeight = dh;
                break;
            }
        }

        glViewport(targetX, targetY, targetWidth, targetHeight);
        glBlitFramebuffer(sx,sy+sh,sw+sx,sy,targetX,targetY,targetWidth,targetHeight,GL_COLOR_BUFFER_BIT,GL_NEAREST);
        return 1;
    } else {
        return 0;
    }
}
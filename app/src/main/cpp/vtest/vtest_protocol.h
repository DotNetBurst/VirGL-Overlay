/**************************************************************************
 *
 * Copyright (C) 2015 Red Hat Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 **************************************************************************/
#ifndef VTEST_PROTOCOL
#define VTEST_PROTOCOL

#define VTEST_DEFAULT_SOCKET_NAME "\0/tmp/.virgl_test"

/* 32-bit length field */
/* 32-bit cmd field */
#define VTEST_HDR_SIZE 2
#define VTEST_CMD_LEN 0 /* length of data */
#define VTEST_CMD_ID  1
#define VTEST_CMD_DATA_START 2

/* vtest cmds */
#define VCMD_GET_CAPS 1

#define VCMD_RESOURCE_CREATE 2
#define VCMD_RESOURCE_UNREF 3

#define VCMD_TRANSFER_GET 4
#define VCMD_TRANSFER_PUT 5

#define VCMD_SUBMIT_CMD 6

#define VCMD_RESOURCE_BUSY_WAIT 7

/* pass the process cmd line for debugging */
#define VCMD_CREATE_RENDERER 8

#define VCMD_GET_CAPS2 9

#define VCMD_DT_COMMAND 10

/* get caps */
/* 0 length cmd */
/* resp VCMD_GET_CAPS + caps */

#define VCMD_RES_CREATE_SIZE 10
#define VCMD_RES_CREATE_RES_HANDLE 0
#define VCMD_RES_CREATE_TARGET 1
#define VCMD_RES_CREATE_FORMAT 2
#define VCMD_RES_CREATE_BIND 3
#define VCMD_RES_CREATE_WIDTH 4
#define VCMD_RES_CREATE_HEIGHT 5
#define VCMD_RES_CREATE_DEPTH 6
#define VCMD_RES_CREATE_ARRAY_SIZE 7
#define VCMD_RES_CREATE_LAST_LEVEL 8
#define VCMD_RES_CREATE_NR_SAMPLES 9

#define VCMD_RES_UNREF_SIZE 1
#define VCMD_RES_UNREF_RES_HANDLE 0

#define VCMD_TRANSFER_HDR_SIZE 11
#define VCMD_TRANSFER_RES_HANDLE 0
#define VCMD_TRANSFER_LEVEL 1
#define VCMD_TRANSFER_STRIDE 2
#define VCMD_TRANSFER_LAYER_STRIDE 3
#define VCMD_TRANSFER_X 4
#define VCMD_TRANSFER_Y 5
#define VCMD_TRANSFER_Z 6
#define VCMD_TRANSFER_WIDTH 7
#define VCMD_TRANSFER_HEIGHT 8
#define VCMD_TRANSFER_DEPTH 9
#define VCMD_TRANSFER_DATA_SIZE 10

#define VCMD_BUSY_WAIT_FLAG_WAIT 1

#define VCMD_BUSY_WAIT_SIZE 2
#define VCMD_BUSY_WAIT_HANDLE 0
#define VCMD_BUSY_WAIT_FLAGS 1

#define VCMD_DT_SIZE 8
#define VCMD_DT_CMD 0
#define VCMD_DT_X 1
#define VCMD_DT_Y 2
#define VCMD_DT_WIDTH 3
#define VCMD_DT_HEIGHT 4
#define VCMD_DT_ID 5
#define VCMD_DT_HANDLE 6
#define VCMD_DT_DRAWABLE 7

#define VCMD_DT_CMD_CREATE 0
#define VCMD_DT_CMD_DESTROY 1
#define VCMD_DT_CMD_SET_RECT 2
#define VCMD_DT_CMD_FLUSH 3

struct ConfigProfile {
    const char* socketPath;
    const char* ringBufferPath;

    uint8_t useSocket;
    uint8_t useGLES;
    uint8_t useS3TC;
    uint8_t useVertexShaderHack;
    uint8_t useBlendHack;
    uint8_t useStencilMirror;
    uint8_t useFragmentShaderHack;
    uint8_t useViewportShrink;
    uint8_t centerViewportRect;
    uint8_t useGLX;
    int viewportShrinkType;
    int shrinkWidth;
    int shrinkHeight;
    int deviceWidth;
    int deviceHeight;
    int navBarHeight;
};
struct ConfigProfile *commonConfigProfile;

#endif
 
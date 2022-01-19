# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("virgloverlay")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_definitions(
        #-DENABLE_TRACING=TRACE_WITH_STDERR
        -DHAVE_PTHREAD
        -DPIPE_OS_ANDROID
        -DVIRGL_RENDERER_UNSTABLE_APIS
        -DHAVE_CONFIG_H
        -DHAVE_SYS_UIO_H
        -DNO_GBM
        -DANDROID_JNI
        -DANDROID_ABI=armeabi-v7a
        -DANDROID_ARM_NEON=ON
        -D_LINUX_IPC_H
        -D__aeabi_idiv
        -Dipc_perm=debian_ipc_perm
        -lm -Wl,--no-undefined -landroid -llog
        )

include_directories(
        src
        src/epoxy
        src/gallium/auxiliary
        src/gallium/auxiliary/cso_cache
        src/gallium/auxiliary/os
        src/gallium/auxiliary/tgsi
        src/gallium/auxiliary/util
        src/gallium/include
        src/gallium/include/c11
        src/gallium/include/pipe
        src/gl4es/
        vtest
)

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

        # Provides a relative path to your source file(s).

        vtest/ring.c
        vtest/util.c
        vtest/vtest_renderer.c
        vtest/vtest_server.c
        vtest/vtest_shm.c

        src/epoxy/dispatch_common.c
        src/epoxy/dispatch_egl.c
        src/epoxy/egl_generated_dispatch.c
        src/epoxy/gl_generated_dispatch.c

        src/iov.c
        src/virgl_context.c
        src/virgl_resource.c
        src/virgl_util.c
        src/virglrenderer.c
        src/vrend_blitter.c
        src/vrend_debug.c
        src/vrend_decode.c
        src/vrend_formats.c
        src/vrend_object.c
        src/vrend_renderer.c
        src/vrend_shader.c
        src/vrend_tweaks.c
        src/vrend_winsys.c

        src/gallium/auxiliary/cso_cache/cso_cache.c
        src/gallium/auxiliary/cso_cache/cso_hash.c

        src/gallium/auxiliary/os/os_misc.c

        src/gallium/auxiliary/tgsi/tgsi_build.c
        src/gallium/auxiliary/tgsi/tgsi_dump.c
        src/gallium/auxiliary/tgsi/tgsi_info.c
        src/gallium/auxiliary/tgsi/tgsi_iterate.c
        src/gallium/auxiliary/tgsi/tgsi_parse.c
        src/gallium/auxiliary/tgsi/tgsi_sanity.c
        src/gallium/auxiliary/tgsi/tgsi_scan.c
        src/gallium/auxiliary/tgsi/tgsi_strings.c
        src/gallium/auxiliary/tgsi/tgsi_text.c
        src/gallium/auxiliary/tgsi/tgsi_transform.c
        src/gallium/auxiliary/tgsi/tgsi_ureg.c
        src/gallium/auxiliary/tgsi/tgsi_util.c

        src/gallium/auxiliary/util/u_bitmask.c
        src/gallium/auxiliary/util/u_cpu_detect.c
        src/gallium/auxiliary/util/u_debug.c
        src/gallium/auxiliary/util/u_debug_describe.c
        src/gallium/auxiliary/util/u_format.c
        src/gallium/auxiliary/util/u_format_table.c
        src/gallium/auxiliary/util/u_hash_table.c
        src/gallium/auxiliary/util/u_math.c
        src/gallium/auxiliary/util/u_surface.c
        src/gallium/auxiliary/util/u_texture.c

        src/gl4es/decompress.c
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log
              lgcc
            )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib
                       -ljnigraphics
                       -landroid
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
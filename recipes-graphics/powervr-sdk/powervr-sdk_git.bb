#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "C++ cross-platform 3D graphics SDK. Includes demos & helper \
           code (resource loading etc.) to speed up development of Vulkan, \
           OpenGL ES 2.0 & 3.x applications"
HOMEPAGE = "http://powervr-graphics.github.io/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=402476d9302b00251cc699d23264b191"


PV = "5.17+git"

# R26.2-v5.17's .gitmodules point at Imagination's private github.imgtec.org
# mirrors; fetch the same submodule commits from the public upstreams instead.
SRC_URI = " \
    git://github.com/powervr-graphics/Native_SDK.git;protocol=https;branch=master;name=sdk;destsuffix=git \
    git://github.com/zeux/pugixml.git;protocol=https;branch=master;name=pugixml;destsuffix=git/external/pugixml/source \
    git://github.com/cameron314/concurrentqueue.git;protocol=https;branch=master;name=concurrentqueue;destsuffix=git/external/concurrent_queue/source \
    git://github.com/g-truc/glm.git;protocol=https;branch=master;name=glm;destsuffix=git/external/glm/source \
    git://github.com/KhronosGroup/glslang.git;protocol=https;branch=main;name=glslang;destsuffix=git/external/glslang/source \
    git://github.com/GPUOpen-LibrariesAndSDKs/VulkanMemoryAllocator.git;protocol=https;branch=master;name=vma;destsuffix=git/external/VulkanMemoryAllocator/source \
"

SRCREV_FORMAT = "sdk"
# R26.2-v5.17
SRCREV_sdk = "63f0098acdc11ec9a072c30000d2fa670a955bee"
# submodule commits recorded by R26.2-v5.17
SRCREV_pugixml = "3c59df555b93a7650ae0056da34bacde93f4ef8f"
SRCREV_concurrentqueue = "79cec4c3bf1ca23ea4a03adfcd3c2c3659684dd2"
SRCREV_glm = "658d8960d081e0c9c312d49758c7ef919371b428"
SRCREV_glslang = "8a85691a0740d390761a1008b4696f57facd02c4"
SRCREV_vma = "e9bfb539d853607172f9a1a272dacab60a86e529"

# UNPACKDIR on master (wrynose+), WORKDIR on scarthgap
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}/git"

inherit pkgconfig cmake

PACKAGECONFIG ??= "\
    examples \
    framework \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vulkan', 'vulkan', '', d)} \
"

PACKAGECONFIG[examples] =  "-DPVR_BUILD_EXAMPLES=ON, -DPVR_BUILD_EXAMPLES=OFF"
PACKAGECONFIG[framework] = "-DPVR_BUILD_FRAMEWORK=ON, -DPVR_BUILD_FRAMEWORK=OFF"
PACKAGECONFIG[gles] =  "-DPVR_BUILD_OPENGLES_EXAMPLES=ON,-DPVR_BUILD_OPENGLES_EXAMPLES=OFF, virtual/egl"
PACKAGECONFIG[vulkan] =    "-DPVR_BUILD_VULKAN_EXAMPLES=ON,-DPVR_BUILD_VULKAN_EXAMPLES=OFF, vulkan-loader glslang"
PACKAGECONFIG[wayland] =   "-DPVR_WINDOW_SYSTEM=Wayland,,libxkbcommon wayland wayland-native wayland-protocols"
PACKAGECONFIG[glsc] =      "-DPVR_BUILD_OPENGLSC_EXAMPLES=ON,-DPVR_BUILD_OPENGLSC_EXAMPLES=OFF"
PACKAGECONFIG[opencl] =    "-DPVR_BUILD_OPENCL_EXAMPLES=ON,-DPVR_BUILD_OPENCL_EXAMPLES=OFF, opencl-headers opencl-icd-loader"

EXTRA_OECMAKE += "\
    -D PVR_PREBUILT_DEPENDENCIES=OFF \
    -D CMAKE_CXX_STANDARD=17 \
    -D CMAKE_POLICY_VERSION_MINIMUM=3.5 \
"

do_configure:prepend() {
    chmod +x ${S}/external/glslang/prebuilt/glslangValidator-linux-x86_64
}

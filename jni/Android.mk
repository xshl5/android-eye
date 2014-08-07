#####################################################################
# the build script for NDK for droidipcam project
#

LOCAL_PATH:= $(call my-dir)

###########################################################
#   mp3 encoder
#
include $(CLEAR_VARS)
LOCAL_MODULE := libmp3encoder
LOCAL_CFLAGS := -O2 -Wall -DANDROID -DSTDC_HEADERS -I./libmp3lame/ 
LOCAL_ARM_MODE := arm

#including source files
include $(LOCAL_PATH)/libmp3lame_build.mk

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

###########################################################
#   uPnP port mapping 
#
include $(CLEAR_VARS)
LOCAL_MODULE := libnatpmp
LOCAL_CFLAGS := -O2 -Wall -DANDROID -DLINUX -I./libnatpmp/  
LOCAL_ARM_MODE := arm

#including source files
include $(LOCAL_PATH)/libnatpmp_build.mk

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)   

###########################################################
#   uPnPc port mapping
#
include $(CLEAR_VARS)
LOCAL_MODULE := libminiupnpc
LOCAL_CFLAGS := -O2 -Wall -DANDROID -DLINUX -I./libminiupnpc/
LOCAL_ARM_MODE := arm

#including source files
LOCAL_SRC_FILES += libminiupnpc/miniwget.c \
                   libminiupnpc/minixml.c \
                   libminiupnpc/igd_desc_parse.c \
                   libminiupnpc/minisoap.c \
                   libminiupnpc/miniupnpc.c \
                   libminiupnpc/upnpreplyparse.c \
                   libminiupnpc/upnpcommands.c \
                   libminiupnpc/upnperrors.c \
                   libminiupnpc/connecthostport.c \
                   libminiupnpc/portlistingparse.c \
                   libminiupnpc/receivedata.c \
                   libminiupnpc/minissdpc.c \
                   libminiupnpc/upnpc.c \
                   miniupnpc.cpp

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

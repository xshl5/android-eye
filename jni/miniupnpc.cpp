#include "beginvision.h"

extern "C" {
#include "libminiupnpc/upnpc.h"
}
#include <stdio.h>
#include <string.h>

#define  JNIDEFINE(fname) Java_teaonly_droideye_MainActivity_##fname

extern "C" {
    JNIEXPORT jstring JNICALL JNIDEFINE(nativeXshl5Redirect)(JNIEnv* env, jclass clz,
                                                             jint privatePort, jint externalPort, jint ipv6,
                                                             jstring protocol, jstring description,
                                                             jstring interfaceOrIpaddr);
    JNIEXPORT jint JNICALL JNIDEFINE(nativeXshl5RedirectRemove)(JNIEnv* env, jclass clz,
                                                                jstring externalPort, jstring protocol,
                                                                jint ipv6, jstring interfaceOrIpaddr);
}

jstring JNICALL JNIDEFINE(nativeXshl5Redirect)(JNIEnv* env, jclass clz,
                                                             jint privatePort, jint externalPort, jint ipv6,
                                                             jstring protocol, jstring description,
                                                             jstring interfaceOrIpaddr)
{
    int ret = -1;
    char ext_ipaddr[40] = {0};
    char result[256] = {0};

    ret = xshl5_redirect(privatePort, externalPort, NULL,
                         protocol==NULL? NULL: env->GetStringUTFChars(protocol, NULL),
                         ipv6,
                         interfaceOrIpaddr==NULL? NULL: env->GetStringUTFChars(interfaceOrIpaddr, NULL),
                         description==NULL? NULL: env->GetStringUTFChars(description, NULL),
                         ext_ipaddr);
    if(ret == 0)
    {
        strcpy(result, "http://");
        strcat(result, ext_ipaddr);
        sprintf(result + strlen(result), ":%u", externalPort);
    }
    else
    {
        strcpy(result, "Error:-(");
    }

    return env->NewStringUTF(result);
}

jint JNICALL JNIDEFINE(nativeXshl5RedirectRemove)(JNIEnv* env, jclass clz,
                                                  jstring externalPort, jstring protocol,
                                                  jint ipv6, jstring interfaceOrIpaddr)
{
    const char* exp_port_and_protocols[] = {env->GetStringUTFChars(externalPort, NULL),
                                            env->GetStringUTFChars(protocol, NULL)};

    return xshl5_redirect_remove(exp_port_and_protocols, 1, ipv6,
                                 interfaceOrIpaddr==NULL? NULL: (env->GetStringUTFChars(interfaceOrIpaddr, NULL)));
}


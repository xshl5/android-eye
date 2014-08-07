int xshl5_redirect(unsigned short pri_port, unsigned short ext_port,
                   const char* pri_ip, const char* protocol, int ipv6,
                   const char* interface_or_ipaddr, const char* description,
                   /*out*/char* ext_ipaddr);
int xshl5_redirect_remove(const char* ext_port_and_protocols[], int num,
                    int ipv6, const char* interface_or_ipaddr);

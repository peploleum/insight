#!/bin/bash
sed -ri 's/NIFI_URI/'$NIFI_URI'/' /etc/rsyslog.conf && sed -ri 's/NIFI_PORT/'$NIFI_PORT'/' /etc/rsyslog.conf
service rsyslog start && /usr/sbin/sshd -D && tail -f /var/log/rsyslog.log

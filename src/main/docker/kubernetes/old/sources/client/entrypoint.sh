#!/bin/bash
sed -ri 's/SYSLOG_SERVER_HOST/'$SYSLOG_SERVER_HOST'/' /etc/rsyslog.conf && sed -ri 's/SYSLOG_SERVER_PORT/'$SYSLOG_SERVER_PORT'/' /etc/rsyslog.conf
service rsyslog start && /home/boucle_log.sh && /usr/sbin/sshd -D && tail -f /var/log/rsyslog.log

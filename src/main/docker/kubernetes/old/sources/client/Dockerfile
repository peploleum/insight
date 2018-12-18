FROM 192.168.65.5:8093/compose_rsyslogclient:latest

COPY sshd_config /etc/ssh/sshd_config

COPY	rsyslog.conf /etc/rsyslog.conf
COPY  entrypoint.sh /home/entrypoint.sh
COPY  boucle_log.sh /home/boucle_log.sh
RUN chmod +x /home/entrypoint.sh
RUN chmod +x /home/boucle_log.sh

EXPOSE 23
EXPOSE 514

ENTRYPOINT ["/bin/sh","/home/entrypoint.sh"]

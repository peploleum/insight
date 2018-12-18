FROM 192.168.65.5:8093/compose_rsyslogserver:latest

COPY sshd_config /etc/ssh/sshd_config

COPY	rsyslog.conf /etc/rsyslog.conf
COPY  entrypoint.sh /home/entrypoint.sh
RUN chmod +x /home/entrypoint.sh

EXPOSE 22
EXPOSE 10514

ENTRYPOINT ["/bin/sh","/home/entrypoint.sh"]

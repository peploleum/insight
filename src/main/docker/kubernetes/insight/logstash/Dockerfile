
# https://github.com/elastic/logstash-docker
FROM docker.elastic.co/logstash/logstash-oss:6.4.2


# Add your logstash plugins setup here
# Example: RUN logstash-plugin install logstash-filter-json
RUN logstash-plugin install logstash-input-file
RUN logstash-plugin install logstash-input-kafka
RUN logstash-plugin install logstash-filter-grok
RUN logstash-plugin install logstash-output-http
RUN logstash-plugin install logstash-output-kafka

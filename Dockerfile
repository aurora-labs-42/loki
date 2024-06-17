FROM openjdk:17-slim

# Add Loki-dist.zip to the Docker image
ADD target/*.tar.gz /opt/loki

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

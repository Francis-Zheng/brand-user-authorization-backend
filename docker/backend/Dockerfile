FROM openjdk:8-jre
LABEL maintainer="zdf824254922@gmail.com"

WORKDIR /brand-user-authorization-backend

ADD brand-user-authorization-backend-0.0.1-SNAPSHOT.jar /tmp
ADD entrypoint.sh /tmp
COPY ./data /data

RUN echo "正在安装mongodb" &&   \
    apt update  &&  \
    apt install mongo-tools

CMD bash /tmp/entrypoint.sh




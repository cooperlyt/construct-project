FROM adoptopenjdk/openjdk11-openj9:alpine-slim
RUN echo http://mirrors.ustc.edu.cn/alpine/v3.8/main > /etc/apk/repositories; \
    echo http://mirrors.ustc.edu.cn/alpine/v3.8/community >> /etc/apk/repositories \
    &&  apk update && apk upgrade && apk add netcat-openbsd \
    && apk add --no-cache tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && rm -rf /var/cache/apk/* /tmp/* /var/tmp/* $HOME/.cache \
    && mkdir -p /usr/local/app
ADD @project.build.finalName@.jar /usr/local/app/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
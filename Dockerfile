# 基于java镜像创建新镜像
#FROM java:8
FROM openjdk:8-jdk-alpine
#构建参数
ARG JAR_FILE
ARG WORK_PATH="/opt/run"
# 环境变量
ENV JAVA_OPTS="" \
    JAR_FILE=${JAR_FILE}
# 作者
MAINTAINER ljy
# 将当前目录下的mall-tiny-docker-file.jar包复制到docker容器的/目录下
COPY target/$JAR_FILE $WORK_PATH/

RUN echo '172.16.0.5    core.harbor.domain' >> /etc/hosts

WORKDIR $WORK_PATH
# 指定docker容器启动时执行的命令
ENTRYPOINT exec java $JAVA_OPTS -jar $JAR_FILE
#ENTRYPOINT ["nohup","java","-jar","/opt/model-manager-0.0.1-SNAPSHOT.jar","&"]
# 声明服务运行在8080端口
EXPOSE 8080
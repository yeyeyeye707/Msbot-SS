FROM centos:7

ENV HOME=/home/usr/clash/master
# RUN mkdir -p $HOME
WORKDIR $HOME

ENV TZ Asia/Shanghai

# https://github.com/wanhebin/clash-for-linux
ADD master_23.05.17/ /home/usr/clash/master/

RUN yum -y install wget

CMD bash start.sh ; \
source /etc/profile.d/clash.sh ; \
proxy_on ; \
/bin/bash
# RUN bash start.sh
# RUN source /etc/profile.d/clash.sh
# CMD ["proxy_on"]

# 编辑 clash-docker/.env
# yum -y install wget

# 起服务
# cd clash-docker
# bash start.sh
# source /etc/profile.d/clash.sh
# proxy_on

# 关服务
# cd clash-docker
# bash shutdown.sh
# proxy_off

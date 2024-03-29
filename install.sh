#!/usr/bin/env sh

# 修改权限
chmod 744 stop.sh
chmod 744 restart.sh
chmod 744 init.sh

# msbot mysql 目录
if [ ! -d ~/msbot  ]; then
  mkdir ~/msbot
fi

if [ ! -d ~/msbot/mysql  ]; then
  mkdir ~/msbot/mysql
fi

if [ ! -d ~/msbot/cqhttp  ]; then
  mkdir ~/msbot/cqhttp
fi

if [ ! -d ~/msbot/java  ]; then
  mkdir ~/msbot/java
fi

echo '重新设置配置 Y/n'
read input
if [ $input = 'y' -o $input = 'Y' ]; then
  # 输入一些配置
  sh init.sh
fi


# 起服务
# docker-compose -f docker-compose-msbot.yml build --no-cache
docker-compose -f docker-compose-msbot.yml build
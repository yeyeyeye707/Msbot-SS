#!/usr/bin/env sh

# 修改权限
chmod 744 stop.sh
chmod 744 restart.sh

# mysql 目录
if [ ! -d ~/msbot  ];then
  mkdir ~/msbot
fi

if [ ! -d ~/msbot/mysql  ];then
  mkdir ~/msbot/mysql
fi

# TODO.. 输入一些配置

# 起服务
docker-compose -f docker-compose-msbot.yml build --no-cache
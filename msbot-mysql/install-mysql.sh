#!/usr/bin/env sh

# 启动一个单独的mysql docker容器.
# 不再使用


# 获取mysql 镜像
docker pull mysql

# 停止当前mysql容器
for i in [ `docker ps ` ]; do
	if [[ $i == "mysql" ]]; then
		echo "停止mysql"
		docker stop msbot-mysql
		break
	fi
done
# 删除之前mysql容器
for i in [ `docker ps -a` ]; do
	if [[ $i == "mysql" ]]; then
		echo "删除mysql"
		docker rm msbot-mysql
		break
	fi
done

# mysql 目录
if [ ! -d ~/msbot  ];then
  mkdir ~/msbot
fi

if [ ! -d ~/msbot/mysql  ];then
  mkdir ~/msbot/mysql
fi

# 启动mysql 
docker run -d --restart=always --name msbot-mysql \
-v ~/msbot/mysql/data:/var/lib/mysql \
-v ~/msbot/mysql/log:/var/log/mysql \
-p 32222:3306 \
-e TZ=Asia/Shanghai \
-e MYSQL_ROOT_PASSWORD=123456 \
mysql \
--character-set-server=utf8mb4 \
--collation-server=utf8mb4_general_ci

# 拷文件进实例
docker cp ./ms.sql msbot-mysql:/
sleep 5
docker cp ./initialize-mysql.sh msbot-mysql:/
sleep 10

docker exec -it msbot-mysql /bin/bash -c "sh /initialize-mysql.sh"
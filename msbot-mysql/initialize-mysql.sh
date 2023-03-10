#!/usr/bin/env sh

# 启动一个单独的mysql docker容器.
# 不再使用

mysql -uroot -p123456 -e"source /ms.sql"

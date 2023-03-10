#!/usr/bin/env sh

cqhttp_output=msbot-go-cqhttp/config.yml

IFS=''
echo -n "输入qq号."
read qq

echo -n "输入密码"
read pwd
echo $qq

if [ -f $cqhttp_output  ]; then
  rm $cqhttp_output
fi
# cqhttp
i=0
cat msbot-go-cqhttp/config_dft.yml | while read line
do
    let i++
    if [ $i == 4 ]
    then
        echo "  uin: $qq # QQ账号" >> $cqhttp_output
    elif [ $i == 5 ]
    then
        echo "  password: '$pwd' # 密码为空时使用扫码登录" >> $cqhttp_output
    else
        echo $line >> $cqhttp_output
    fi
done

echo "想办法用go-cqhttp登录"
echo "成功后复制device.json 和 session.token到 msbot-go-cqhttp文件夹下"

# docker
echo "输入机器人昵称"
read name
echo "最高权限QQ"
read master
echo "管理员QQ  半角,逗号分隔"
read manager

env_output=./.env

j=0
if [ -f $env_output  ]; then
  rm $env_output
fi

cat ./.env-dft | while read line
do
    let j++
    if [ $j == 23 ]; then
        echo "BOT_NAME=$name" >> $env_output
    elif [ $j == 25 ]; then
        echo "BOT_QQ=$qq" >> $env_output
    elif [ $j == 27 ]; then
        echo "MASTER_QQ=$master" >> $env_output
    elif [ $j == 29 ]; then
        echo "MANAGER_QQ='$manager'" >> $env_output
    else
        echo $line >> $env_output
    fi
done

exit 0
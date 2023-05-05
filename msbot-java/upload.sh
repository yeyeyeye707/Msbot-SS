#!/usr/bin/expect

spawn sftp yaozhen

expect "sftp>"
    send "cd msbot/msbot-java\r"

expect "sftp>"
    send "put target/msbot.jar .\r"
#    send "put /Users/yao/Documents/guolong/dzzxzz/04.Server/ApiServer/target/dzzxzzApiServer-1.0.0-rel-uber.jar upload/.\n"
expect "sftp>"
    send "quit\r"

expect eof

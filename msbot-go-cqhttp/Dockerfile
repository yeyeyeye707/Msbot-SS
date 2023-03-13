FROM golang:1.20

WORKDIR /usr/src/cqhttp

RUN rm -rf /usr/src/cqhttp/

# 阿里云镜像
# RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# git 代理
# RUN git config --global https.proxy http://127.0.0.1:8118
# RUN git config --global https.proxy https://127.0.0.1:8118


# git 源
RUN git clone https://github.com/Mrs4s/go-cqhttp.git /usr/src/cqhttp/

# 使用本地,把输入选择去掉
# ADD ./go-cqhttp/ /usr/src/cqhttp/

# golang.org代理访问镜像
RUN GOPROXY="https://goproxy.cn" go build -ldflags "-s -w -extldflags '-static'"
# RUN go build -ldflags "-s -w -extldflags '-static'"

# # 找个地方跑一下生成
# COPY ./config.yml .
# COPY ./device.json .
# COPY ./session.token .


CMD ./go-cqhttp
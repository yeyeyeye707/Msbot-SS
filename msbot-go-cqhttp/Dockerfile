FROM golang:1.20

WORKDIR /usr/src/cqhttp

RUN rm -rf /usr/src/cqhttp/

# git 源
RUN git clone https://github.com/Mrs4s/go-cqhttp.git /usr/src/cqhttp/

# 使用本地,把输入选择去掉
# ADD ./go-cqhttp/ /usr/src/cqhttp/

RUN go build -ldflags "-s -w -extldflags '-static'"

# # 找个地方跑一下生成
# COPY ./config.yml .
# COPY ./device.json .
# COPY ./session.token .


CMD ./go-cqhttp
FROM alpine:3.7
MAINTAINER stokpop

RUN apk add --update alpine-sdk openssl-dev
RUN apk add --no-cache git jq

RUN git clone https://github.com/giltene/wrk2.git && cd wrk2 && make

ENTRYPOINT ["/bin/ash"]

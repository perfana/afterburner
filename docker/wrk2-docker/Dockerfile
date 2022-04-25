FROM alpine:3.15.4
MAINTAINER stokpop

RUN apk add --update --no-cache alpine-sdk openssl-dev zlib-dev \
    && apk add --no-cache git jq \
    && apk add --no-cache zsh \
    && sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" \
    && echo "" >> /root/.zshrc && echo "export PROMPT=\"WORKSHOP-T4T \$PROMPT\"" >> /root/.zshrc \
    && echo "cd workshop" >> /root/.zshrc \
    && echo "alias wrk=/wrk2/wrk" >> /root/.zshrc \
    && git clone https://github.com/giltene/wrk2.git && cd wrk2 && make

ENTRYPOINT ["/bin/ash"]

FROM alpine:3.15
MAINTAINER stokpop

RUN apk add --update --no-cache alpine-sdk openssl-dev zlib-dev git jq zsh perl linux-headers \
    && sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" \
    && echo "" >> /root/.zshrc && echo "export PROMPT=\"WORKSHOP-T4T \$PROMPT\"" >> /root/.zshrc \
    && echo "cd workshop" >> /root/.zshrc \
    && echo "alias wrk=/wrk/wrk" >> /root/.zshrc \
    && git clone https://github.com/wg/wrk.git && cd wrk && make

ENTRYPOINT ["/bin/zsh"]

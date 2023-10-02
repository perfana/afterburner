FROM alpine:3.16 as build
MAINTAINER peterpaul@perfana.io
RUN apk add --update --no-cache alpine-sdk openssl-dev zlib-dev git \
    && git clone https://github.com/kinvolk/wrk2 && cd wrk2 && make

FROM alpine:3.16
COPY --from=build /wrk2/wrk /wrk
RUN apk add --update --no-cache git curl jq zsh gcc \
    && sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" \
    && echo "" >> /root/.zshrc && echo "export PROMPT=\"WORKSHOP-T4T \$PROMPT\"" >> /root/.zshrc \
    && echo "cd workshop" >> /root/.zshrc \
    && echo "alias wrk=/wrk" >> /root/.zshrc 

ENTRYPOINT ["/bin/zsh"]

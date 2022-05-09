FROM alpine:3.15.4 as build
MAINTAINER stokpop

RUN apk add --update --no-cache alpine-sdk openssl-dev zlib-dev git \
    && git clone https://github.com/AmpereTravis/wrk2-aarch64.git && cd wrk2-aarch64 && make

FROM alpine:3.15.4
COPY --from=build /wrk2-aarch64/wrk /wrk
RUN apk add --update --no-cache git curl jq zsh gcc \
    && sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" \
    && echo "" >> /root/.zshrc && echo "export PROMPT=\"WORKSHOP-T4T \$PROMPT\"" >> /root/.zshrc \
    && echo "cd workshop" >> /root/.zshrc \
    && echo "alias wrk=/wrk" >> /root/.zshrc

ENTRYPOINT ["/bin/zsh"]

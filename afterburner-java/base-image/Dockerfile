FROM bellsoft/liberica-openjdk-alpine:21-cds

# add glibc for async profiler on aarch64
RUN if [ "$(uname -m)" == "aarch64" ]; then apk add --no-cache gcompat libstdc++; fi

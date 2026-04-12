FROM ubuntu:latest
LABEL authors="nilto"

ENTRYPOINT ["top", "-b"]
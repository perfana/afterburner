FROM alpine:3.8

LABEL maintainer=incenzo.marrazzo@domain.personal
LABEL http_origin=https://www.blazemeter.com/blog/make-use-of-docker-with-jmeter-learn-how
LABEL modified_by=peterpaul@perfana.io

ARG JMETER_VERSION="5.0"

ENV JMETER_HOME /opt/apache-jmeter-${JMETER_VERSION}
ENV JMETER_BIN  ${JMETER_HOME}/bin
ENV MIRROR_HOST http://mirrors.ocf.berkeley.edu/apache/jmeter
ENV JMETER_DOWNLOAD_URL ${MIRROR_HOST}/binaries/apache-jmeter-${JMETER_VERSION}.tgz
ENV JMETER_PLUGINS_DOWNLOAD_URL http://repo1.maven.org/maven2/kg/apc
ENV JMETER_PLUGINS_FOLDER ${JMETER_HOME}/lib/

RUN apk update \
	&& apk upgrade \
	&& apk add ca-certificates \
	&& update-ca-certificates \
        && apk add --update openjdk8-jre tzdata curl unzip bash \
        && cp /usr/share/zoneinfo/Europe/Amsterdam /etc/localtime \
        && echo "Europe/Amsterdam" >  /etc/timezone \
	&& rm -rf /var/cache/apk/* \
	&& mkdir -p /tmp/dependencies  \
	&& curl -L --silent ${JMETER_DOWNLOAD_URL} >  /tmp/dependencies/apache-jmeter-${JMETER_VERSION}.tgz  \
	&& mkdir -p /opt  \
	&& tar -xzf /tmp/dependencies/apache-jmeter-${JMETER_VERSION}.tgz -C /opt  \
	&& rm -rf /tmp/dependencies

# get the PluginsManagerCMD.sh from the jar (the PluginsManagerCMD.sh is in the jar)
RUN cd $JMETER_PLUGINS_FOLDER \
    && curl --silent -O http://search.maven.org/remotecontent?filepath=kg/apc/cmdrunner/2.2/cmdrunner-2.2.jar \
    && cd $JMETER_PLUGINS_FOLDER/ext \
    && curl --silent -O http://search.maven.org/remotecontent?filepath=kg/apc/jmeter-plugins-manager/1.3/jmeter-plugins-manager-1.3.jar \
    && java -cp jmeter-plugins-manager-*.jar org.jmeterplugins.repository.PluginManagerCMDInstaller

RUN cd $JMETER_BIN && \
    chmod u+x PluginsManagerCMD.sh

# a selection of usefull plugins from jmeter-plugins.org
ENV LIST_PLUGINS="jpgc-casutg=2.6,jpgc-csl=0.1,jpgc-dbmon=0.1,jpgc-dummy=0.2,jpgc-functions=2.1,jpgc-jmxmon=0.2,jpgc-perfmon=2.1,jpgc-graphs-basic=2.0,jpgc-graphs-additional=2.0,jpgc-graphs-dist=2.0,jpgc-graphs-vs=2.0,jpgc-cmd=2.2,jpgc-filterresults=2.2,jpgc-ggl=2.0,jpgc-mergeresults=2.1,jpgc-pde=0.1,jpgc-synthesis=2.2,jpgc-sts=2.3"

# Run the PluginsManager to download and install files (jars and scripts files)
# for proxy prefix bot sh commands below with: JVM_ARGS="-Dhttps.proxyHost=$ARG_https_proxyHost -Dhttps.proxyPort=$ARG_https_proxyPort"
RUN cd $JMETER_BIN \
    && ./PluginsManagerCMD.sh install $LIST_PLUGINS \
    && ./PluginsManagerCMD.sh status

ENV PATH $PATH:$JMETER_BIN

COPY launch.sh /

WORKDIR ${JMETER_HOME}

ENTRYPOINT ["/launch.sh"]

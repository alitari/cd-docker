FROM jenkins
USER root
RUN mkdir -p /.docker/qa
RUN chmod a+r -R /.docker/qa
RUN mkdir -p /.docker/acc
RUN chmod a+r -R /.docker/acc
USER jenkins
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt
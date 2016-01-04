#FROM jboss/wildfly:9.0.2.Final
#RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#123 --silent
#ADD wildfly-webservice.war /opt/jboss/wildfly/standalone/deployments/
#CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

FROM jboss/wildfly:9.0.2.Final

ADD commands.cli /opt/jboss/wildfly/customization/
ADD execute.sh /opt/jboss/wildfly/customization/
ADD postgresql-9.4.1207.jar /opt/jboss/wildfly/customization/


RUN /opt/jboss/wildfly/customization/execute.sh

RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#123 --silent
ADD wildfly-webservice.war /opt/jboss/wildfly/standalone/deployments/
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
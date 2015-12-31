# cd-docker
## Prerequisites
The docker Toolbox:
- you must use a bash-shell
- docker: `docker --version` should print out Server version 1.9 or higher
- docker-machine `docker-machine --version` should print out  0.4.0 
- docker-compose `docker-compose` should print out 1.5.2


## Getting started
- clone this repo: `git clone https://github.com/alitari/cd-docker.git`
- navigate to folder infrastructure of this repo `cd cd-docker/infrastructure`
- set up the infrastructure `source ./setupHosts.sh; source ./setupInfra.sh`
- prepare nexus for anonymous deployments:
  - browse to nexus http://$DOCKER_HOST_IP_INFRA:8081/
  - login as admin with password: admin123
  - security -> users -> anonymous -> role management -> add
  - select 'Repo: All maven repositories (Full control)'  
- create jenkins build job
  - browse to jenkins configuration:  http://$DOCKER_HOST_IP_INFRA:8180/configure
  - add maven installation ( automatic install from apache)
  - create new 'freestyle' job http://$DOCKER_HOST_IP_INFRA:8180/newJob
    - in Source-Code-Management use Git with this repo as url: https://github.com/alitari/cd-docker.git
    - add maven goal as build step. Select your maven installation
    - enter 'clean deploy' as maven goals 
    - open the extension area and enter '-DbuildNumber=${BUILD_NUMBER}' as JVM-Options
    - add post build step JUnit-reports and enter wildfly-webservice/target/*/TEST*.xml as file name pattern
    - add post build step 'Track gatling simulation'
    






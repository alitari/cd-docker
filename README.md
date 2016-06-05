# cd-docker
## Prerequisites
- Docker Toolbox

## local setup
- clone this repo: `git clone https://github.com/alitari/cd-docker.git`

### create docker hosts
- `cd environments/local`
- `source ./setupHosts.sh`
- There must exist the 3 hosts:
```
$ docker-machine ls
NAME      ACTIVE   DRIVER       STATE     URL                         SWARM   DOCKER    ERRORS
acc       *        virtualbox   Running   tcp://192.168.99.103:2376           v1.11.2
default   -        virtualbox   Running   tcp://192.168.99.100:2376           v1.11.2
infra     -        virtualbox   Running   tcp://192.168.99.101:2376           v1.11.2
qa        -        virtualbox   Running   tcp://192.168.99.102:2376           v1.11.2
```

### configure maven build
    - buildNumber
    - environment with values of `docker-machine env`
      - `DOCKER_HOST_IP_QA`
      - `DOCKER_HOST_PORT_QA`
      - `DOCKER_CERT_PATH_QA`
 
 


### infrastructure setup

- navigate to folder infrastructure of this repo `cd cd-docker/infrastructure`
- set up the infrastructure `source ./setupHosts.sh; source ./startInfra.sh`
- prepare nexus for anonymous deployments:
  - browse to nexus `http://$DOCKER_HOST_IP_INFRA:8081/`
  - login as admin with password: admin123
  - security -> users -> anonymous -> role management -> add
  - select `Repo: All Maven Repositories (Full control)`
- create jenkins build job
  - browse to jenkins configuration:  `http://$DOCKER_HOST_IP_INFRA:8180/configure`
  - add maven installation ( automatic install from apache)
  - create new 'freestyle' job `http://$DOCKER_HOST_IP_INFRA:8180/newJob`
    - in Source-Code-Management use Git with this repo as url: `https://github.com/alitari/cd-docker.git`
    - add maven goal as build step. Select your maven installation
    - enter `clean deploy` as maven goals 
    - open the extension area and enter `-DbuildNumber=${BUILD_NUMBER}` as JVM-Options
    - add post build step JUnit-reports and enter `wildfly-webservice/target/*/TEST*.xml` as file name pattern
    - add post build step `Track gatling simulation`
    






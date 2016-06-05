# cd-docker
## Prerequisites
- Docker Toolbox

## local setup
- clone this repo: `git clone https://github.com/alitari/cd-docker.git`

### create docker hosts
- `cd environments/local && source ./setupHosts.sh`
- There must exist the 3 hosts:
```
$ docker-machine ls
NAME      ACTIVE   DRIVER       STATE     URL                         SWARM   DOCKER    ERRORS
acc       *        virtualbox   Running   tcp://192.168.99.103:2376           v1.11.2
infra     -        virtualbox   Running   tcp://192.168.99.101:2376           v1.11.2
qa        -        virtualbox   Running   tcp://192.168.99.102:2376           v1.11.2
```


### setup the infra host

- `$ source infraUp.sh`
- now the environment should be ready showing the accoring variables similiar to this:
```
--------------- Environment variables ----------------------

DOCKER_HOST_IP_ACC=192.168.99.103
DOCKER_HOST_PORT_INFRA=2376
DOCKER_HOST_IP_QA=192.168.99.102
DOCKER_HOST=tcp://192.168.99.101:2376
DOCKER_HOST_PORT_QA=2376
DOCKER_CERT_PATH_ACC=C:\Users\Alexander\.docker\machine\machines\acc
DOCKER_MACHINE_NAME=infra
DOCKER_TLS_VERIFY=1
DOCKER_TOOLBOX_INSTALL_PATH=C:\Program Files\Docker Toolbox
DOCKER_CERT_PATH_INFRA=C:\Users\Alexander\.docker\machine\machines\infra
DOCKERREGISTRY_PORT=5000
DOCKER_CERT_PATH_QA=C:\Users\Alexander\.docker\machine\machines\qa
DOCKER_HOST_PORT_ACC=2376
DOCKER_HOST_IP_INFRA=192.168.99.101
DOCKER_CERT_PATH=C:\Users\Alexander\.docker\machine\machines\infra
```
- you can check if the services are running ( be sure that your current machine is 'infra'): `docker ps`
```
CONTAINER ID        IMAGE                                      COMMAND                CREATED             STATUS              PORTS                                             NAMES
686943430f7a        infra_jenkins                              "/bin/tini -- /usr/l   6 minutes ago       Up 6 minutes        0.0.00:8180->8080/tcp, 0.0.0.0:50000->50000/tcp   infra_jenkins_1
c5543b199bb9        registry:2                                 "/bin/registry serve   7 minutes ago       Up 7 minutes        0.0.0.0:5000->5000/tcp                            infra_registry_1
e86a78e6c00f        postgres                                   "/docker-entrypoint.   7 minutes ago       Up 7 minutes        0.0.0.0:5432->5432/tcp                            infra_sonar-db_1
cf97994d5a14        clue/adminer                               "supervisord -c /etc   7 minutes ago       Up 7 minutes        0.0.0.0:8558->80/tcp                              infra_adminer_1
29d411c3b998        konradkleine/docker-registry-frontend:v2   "/bin/sh -c $START_S   9 minutes ago       Up 9 minutes        443/tcp, 0.0.0.0:8080->80/tcp                     infra_registry-frontend_1
def32aa83d1a        sonatype/nexus                             "/bin/sh -c '${JAVA_   10 minutes ago      Up 10 minutes       0.0.0.0:8081->8081/tcp                            infra_nexus_1
```
###  prepare nexus for anonymous deployments:
  - browse to nexus `http://$DOCKER_HOST_IP_INFRA:8081/`
  - login as admin with password: admin123
  - security -> users -> anonymous -> role management -> add
  - select `Repo: All Maven Repositories (Full control)`

### configure maven build
Now we can try a maven build ( cd to the repository root before) : `mvn clean deploy -DbuildNumber=1` 



### configure jenkins
TBD

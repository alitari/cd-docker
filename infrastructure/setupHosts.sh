. ./setDockerEnv.sh
echo "Setting up Docker hosts..."
docker-machine create --driver virtualbox --virtualbox-memory 4096 infra 
setDockerEnv infra

docker-machine ssh infra "sudo mkdir /etc/docker-registry; sudo chmod a+w /etc/docker-registry"
docker-machine ssh infra "sudo mkdir /etc/nexus-data; sudo chmod a+w /etc/nexus-data"
docker-machine ssh infra "sudo mkdir /etc/jenkins_home; sudo chmod a+w /etc/jenkins_home"
docker-machine ssh infra "sudo mkdir /etc/sonar-data; sudo chmod a+w /etc/sonar-data"
docker-machine ssh infra "sudo mkdir /etc/app-data; sudo chmod a+w /etc/app-data"

docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT qa
setDockerEnv qa

docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT acc
setDockerEnv acc

. ./setDockerEnv.sh
echo "Setting up Docker hosts..."
docker-machine create --driver virtualbox --virtualbox-memory 4096 infra 
setDockerEnv infra

docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT qa
setDockerEnv qa

docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT acc
setDockerEnv acc

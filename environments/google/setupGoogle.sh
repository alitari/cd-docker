. ./setDockerEnv.sh
echo "Setting up Docker hosts in Google Cloud Engine..."
#docker-machine create --driver google --google-project dm01-1143 gceinfra 
setDockerEnv gceinfra

#docker-machine create --driver google --google-project dm01-1143 --engine-insecure-registry $DOCKER_HOST_IP_GCEINFRA:$DOCKERREGISTRY_PORT gceqa
setDockerEnv gceqa

#docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT acc
#setDockerEnv acc

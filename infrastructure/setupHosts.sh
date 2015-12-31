echo "Setting up Docker hosts..."
export DOCKERREGISTRY_PORT=5000
docker-machine create --driver virtualbox --virtualbox-memory 4096 infra 
docker-machine start infra
eval "$(docker-machine env infra)"
export DOCKER_HOST_IP_INFRA=${DOCKER_HOST%:*}; DOCKER_HOST_IP_INFRA=${DOCKER_HOST_IP_INFRA#*//};
export DOCKER_HOST_PORT_INFRA=${DOCKER_HOST##*:}
export DOCKER_CERT_PATH_INFRA=$DOCKER_CERT_PATH
echo -e "Docker host INFRA endpoint:\t $DOCKER_HOST_IP_INFRA:$DOCKER_HOST_PORT_INFRA \t cert path:$DOCKER_CERT_PATH_INFRA";
echo -e "Docker registry endpoint:\t $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT"



docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT qa
docker-machine start qa
eval "$(docker-machine env qa)"
export DOCKER_HOST_IP_QA=${DOCKER_HOST%:*}; DOCKER_HOST_IP_QA=${DOCKER_HOST_IP_QA#*//};
export DOCKER_HOST_PORT_QA=${DOCKER_HOST##*:}
export DOCKER_CERT_PATH_QA=$DOCKER_CERT_PATH
echo -e "Docker host QA endpoint:\t $DOCKER_HOST_IP_QA:$DOCKER_HOST_PORT_QA \t cert path:$DOCKER_CERT_PATH_QA";


docker-machine create --driver virtualbox --engine-insecure-registry $DOCKER_HOST_IP_INFRA:$DOCKERREGISTRY_PORT acc
docker-machine start acc
eval "$(docker-machine env acc)"
export DOCKER_HOST_IP_ACC=${DOCKER_HOST%:*}; DOCKER_HOST_IP_ACC=${DOCKER_HOST_IP_ACC#*//};
export DOCKER_HOST_PORT_ACC=${DOCKER_HOST##*:}
export DOCKER_CERT_PATH_ACC=$DOCKER_CERT_PATH
echo -e "Docker host ACC endpoint:\t $DOCKER_HOST_IP_ACC:$DOCKER_HOST_PORT_ACC \t cert path:$DOCKER_CERT_PATH_ACC";


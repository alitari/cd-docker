echo -e "\n\n Setting up infrastructure..."
eval $(docker-machine env infra)
echo $DOCKER_HOST

docker-machine ssh infra "sudo mkdir /etc/docker-registry; sudo chmod a+w /etc/docker-registry"
docker-machine ssh infra "sudo mkdir /etc/nexus-data; sudo chmod a+w /etc/nexus-data"


docker-compose up -d
docker-compose ps
echo -e "------------------------------------------------------------\n\n"

echo -e "--------------- Environment variables ----------------------\n\n"
env | grep DOCKER
. ./setDockerEnv.sh
echo -e "\n\n starting infrastructure services..."

setDockerEnv infra
setDockerEnv qa
setDockerEnv acc


eval $(docker-machine env infra)

docker-compose up -d
docker-compose ps
echo -e "------------------------------------------------------------\n\n"

echo -e "--------------- Environment variables ----------------------\n"
env | grep DOCKER
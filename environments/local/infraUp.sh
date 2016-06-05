. ./setDockerEnv.sh
echo -e "\n\n starting infrastructure services..."

setDockerEnv infra

eval $(docker-machine env infra)
cd infra
docker-compose up -d
docker-compose ps
cd ..


echo -e "------------------------------------------------------------\n\n"

echo -e "--------------- Environment variables ----------------------\n"
env | grep DOCKER
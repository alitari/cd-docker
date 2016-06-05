. ./setDockerEnv.sh
echo -e "\n\n starting infrastructure services..."

setDockerEnv infra
setDockerEnv qa
setDockerEnv acc


eval $(docker-machine env infra)
cd infra
docker-compose up -d
docker-compose ps
cd ..

eval $(docker-machine env qa)
cd qa
docker-compose up -d
docker-compose ps
cd ..

eval $(docker-machine env acc)
cd acc
docker-compose up -d
docker-compose ps
cd ..



echo -e "------------------------------------------------------------\n\n"

echo -e "--------------- Environment variables ----------------------\n"
env | grep DOCKER
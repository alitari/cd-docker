. ./setDockerEnv.sh
echo -e "\n\n starting infrastructure services on Google Compute Engine..."

setDockerEnv gceinfra
setDockerEnv gceqa



eval $(docker-machine env gceinfra)
cd infra
docker-compose up -d
docker-compose ps
cd ..

eval $(docker-machine env gceqa)
cd qa
docker-compose up -d
docker-compose ps
cd ..



echo -e "------------------------------------------------------------\n\n"

echo -e "--------------- Environment variables ----------------------\n"
env | grep DOCKER
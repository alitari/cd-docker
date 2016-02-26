
function setDockerEnv {
    echo "try to start host $1 ..."     
    docker-machine start $1    
    eval "$(docker-machine env $1)"
    echo "setting up Docker environment variables for $1 ..."      
    PF=`echo "$1" | sed 's/./\U&/g'`
    DOCKER_HOST_IP=`docker-machine ssh $1 ifconfig eth0 | awk '/inet addr/ {gsub("addr:", "", $2); print $2}'`
    export DOCKER_HOST_IP_$PF=$DOCKER_HOST_IP
    export DOCKER_HOST_PORT_$PF=${DOCKER_HOST##*:}
    export DOCKER_CERT_PATH_$PF=$DOCKER_CERT_PATH
    export DOCKERREGISTRY_PORT=5000
    env | grep _$PF
}



 
 




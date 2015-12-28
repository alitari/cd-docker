#docker run --net host -it --rm --name mvn-build -v "$PWD":/usr/src/mymaven -v $MAVEN_REPO:/root/.m2 -w /usr/src/mymaven maven:latest "mvn clean deploy -DbuildNumber=1 -DqaDockerHostIp=$DOCKER_HOST_IP_QA -DqaDockerCertPath=$DOCKER_CERT_PATH_QA DaccDockerHostIp=$DOCKER_HOST_IP_ACC -DaccDockerCertPath=$DOCKER_CERT_PATH_ACC -DinfraDockerHostIp=$DOCKER_HOST_IP_INFRA"

mvn clean deploy -DbuildNumber=1 
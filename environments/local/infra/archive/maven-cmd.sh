docker run --net host -it --rm --name mvn-build -v "$PWD":/usr/src/mymaven -v ~/.m2:/root/.m2 -v ~/.docker:/root/.docker -w /usr/src/mymaven maven:latest mvn clean install -Dbuild.number=110 -DqaDockerHostIp= 

docker run  -it --rm --name mvn-build -v "$PWD":/usr/src/mymaven -v ~/.m2:/root/.m2  -w /usr/src/mymaven maven:latest mvn gatling:execute -f gatling_pom.xml 





docker run --net host -it --rm --name mvn-build -v "$PWD":/usr/src/mymaven -v ~/.m2:/root/.m2 -v ~/.docker515:/root/.docker -w /usr/src/mymaven maven:latest mvn clean install -Dbuild.number=101 -DdockerHostIp=192.168.0.16
docker run  -it --rm --name mvn-build -v "$PWD":/usr/src/mymaven -v ~/.m2:/root/.m2  -w /usr/src/mymaven maven:latest mvn clean gatling:execute  -DbaseUrl=http://192.168.0.16:8078





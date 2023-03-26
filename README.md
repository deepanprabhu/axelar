# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.4/maven-plugin/reference/html/#build-image)



## Commands

### Setup MySQL Docker image
* Install podman from https://podman.io/getting-started/installation
* `podman pull docker.io/library/mysql`
* `podman run --name=mk-mysql -p3306:3306 -v mysql-volume:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:latest`
* `podman exec -it mk-mysql bash` - This brings the bash inside docker image
* `mysql -h localhost -P 3306 --protocol=tcp -u root` - run this in the bash to enter into mysql
* `create database axelar` - Exit and run this program

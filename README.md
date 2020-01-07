# Deployment Generator

This repository contains source code for the generator frontend and for the generator backend. Also it includes the Dockerfiles for building the Docker Images.

## Prerequisites
- Docker

Requirements backend only:
- Java 11
- Maven for Dependencies

Requirements frontend only:
- Angular 8
- NPM for Dependencies

## Deploy in Docker
The latest Docker Images are accessible on Docker Hub. For supporting all features of the application it is necessary to run some docker images with privileged rights.
- [Frontend](https://hub.docker.com/r/nfeldhausen/frontend)
- [Backend](https://hub.docker.com/r/nfeldhausen/backend)

Follow these steps to deploy the application:
1. Deploy Docker in Docker with privileged rights and without certificates to support TCP connections

```bash
$ docker run -dp 2375:2375 -e DOCKER_TLS_CERTDIR="" --privileged docker:dind
```

2. Deploy the backend and provide the IP of the docker container
```bash
$ docker run -dp 8080:8080 -e DOCKER_HOST=tcp://<IP_OF_DOCKER_CONTAINER>:2375 nfeldhausen/backend
```

3. Deploy the frontend and provide the IP of the backend

```bash
$ docker run -dp 80:80 -e API_BACKEND=<IP_OF_BACKEND_CONTAINER>:8080 nfeldhausen/frontend
```

4. Your application should be accessible at [http://localhost:80](http://localhost:80).



## Building Docker Images

This section describes how to build the Docker Images yourself. Starting point for each step is the main folder of this repository.

1. Build the Java application and copy the jar file into the Docker folder

```bash
$ cd generator-backend
$ mvn package
$ cp target/backend-*.jar ../Docker/backend/backend.jar
```

2. Build the Angular application and copy the output folder into the Docker folder

```bash
$ cd generator-frontend
$ ng build --prod
$ cp -r dist/generator-frontend/ ../Docker/frontend
```

3. Build the Backend Docker Image
```bash
$ cd Docker/backend
$ docker build -t backend .
```

4. Build the Frontend Docker Image (If you created new endpoints in the backend make sure to edit the nginx.conf aswell)
```bash
$ cd Docker/frontend
$ docker build -t frontend .
```

## Frontend development environment

1. Checkout this repository
```bash
$ git clone git@github.com:nfeldhausen/DeploymentGenerator.git
```

2. Install all necessary dependencies

```bash
$ cd generator-frontend
$ npm install
```

3. Fix [bug](https://github.com/windhandel/angular-http-deserializer/issues/1) in one of the dependencies. Change line **77** from file **generator-frontend/node_modules/angular-http-deserializer/index.js** from

```javascript
throw new Error('Array deserialization error. Object must be array.');
```
to
```javascript
return [];
```

4. Start serving the application

```bash
$ npm start
```
5. The frontend should be accessible via http://localhost:4200.

### Setting backend endpoint
This frontend tunnels all traffic from the client to the backend to the backend, because this is necessary for the Docker Images to work properly.  
If you use a backend which does not serve requests on localhost or port 8080, you have to edit the file **proxy.conf.json**.  
If you want to address new service endpoints, add a corresponding entry to the **proxy.conf.json**.

**Do not forget to restart the whole Angular application, because Angular won't detect changes in this file.**

### Structure
The chart below contains all important folders and files for further development.
```
generator-frontend
├── src                                 # Contains all HTML Template files
│   ├── app
│   │   ├── advanced                    # Contains all HTML Templates of the advanced TAB
│   │   ├── animations                  # Contains all used animations
│   │   ├── directives                  # Contains all used directives
│   │   ├── error-modal                 # Contains the error modal used in the advanced TAB
│   │   ├── model                       # Contains all used models
│   │   ├── result                      # Contains all HTML Templates of the result page
│   │   ├── services                    # Contains all services
│   │   ├── template                    # Contains all HTML Templates of the templates TAB
│   │   ├── welcome                     # Contains all HTML Templates ot the welcome TAB
│   │   ├── wizard                      # Contains all HTMLTemplates of the wizard TAB
│   │   ├── app-routing.module.ts       # Contains app routing (router links etc.)
│   │   ├── app.component.html          # HTML Template file of the app
│   │   └── app.module.ts               # Contains imports of all used imports etc.
│   └── index.html                      # Main HTML Template (Contains body etc.)
└── proxy.conf.json                     # Contains all entries to proxy requests to the backend
```



## Backend development environment

1. Checkout this repository
```bash
$ git clone git@github.com:nfeldhausen/DeploymentGenerator.git
```

2. Import project **generator-backend** into your IDE (project folder should contain an IntelliJ project)
3. Start serving the application by starting main method in file **src/main/java/de.th.bingen.master.backend/BackendApplication.java**
4. The backend should serve requests via **localhost:8080**


### Adding Kubernetes Templates

To add template files it is necessary to follow these steps:
1. Create a new folder in **src/main/resources/kubernetesTemplates**
2. For every Deployment/StatefulSet create a new YAML file in the folder. Include Services/ConfigMaps/Secrets in the same YAML file as the deployment and delimit diffrent types with **---** . **Warning:** Make sure to name all Deployments/StatefulSets/Services in a multi-container deployment uniquely.
3. Restart the backend. If the backend does not know a specific property of a YAML file, the backend will omit loading the template and will display an error message. In order to use the template, it is necessary to further develop the Kubernetes model.

### Structure
The chart below contains all important folders and files for further development.
```
generator-backend
├── src
│   └── main
│       ├── java/de.th.bingen.master.backend
│       │   ├── configuration                   # Contains configuration files for the backend
│       │   ├── controller                      # Contains all controllers
│       │   ├── helper                          # Contains helper classes
│       │   ├── model                           # Contains the Kubernetes model aswell as the request/response models
│       │   ├── services                        # Contains all services 
│       │   └── BackendApplication.java         # Main class of the backend
│       └── resources
│           ├── kubernetesTemplates             # Contains all templates
│           └── application.yaml                # Contains configuration for databases and the default Docker Host
└── pom.xml                                     # Contains definition of maven dependencies
```
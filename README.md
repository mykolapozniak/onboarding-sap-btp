# SAP BTP Projects

This repository contains projects demonstrating different aspects of SAP Business Technology Platform (BTP) development.

## 1. Manifest_project

A Spring Boot application demonstrating authentication and authorization using XSUAA (Extended Services User Account and Authentication) service on SAP BTP Cloud Foundry environment.

<details>
  <summary>Click to expand</summary>

### Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Security
- SAP XSUAA Authentication
- SAP Cloud Platform Cloud Foundry
- Maven

### Project Structure

- `java-tutorial/` - Main application directory
  - `src/` - Source code
  - `web/` - Application router configuration
  - `manifest.yml` - Cloud Foundry deployment descriptor
  - `xs-security.json` - XSUAA service configuration

### How It Works

1. The application implements a simple "Hello World" REST service with OAuth 2.0 authentication
2. Uses XSUAA for authentication and authorization
3. Application Router (web folder) handles user authentication
4. Implements role-based access control using Spring Security
5. Requires "Display" scope for accessing endpoints

### How to Run

> Make sure you are in `java_tutorial` directory

1. Change `url` in `manifest.yml` to yours:
```yml
destinations: >
        [
          {
            "name":"helloworld",
            "url":"<- Change here ->,
            "forwardAuthToken": true
          }
        ]
```
2. Change `redirect-uris` in `xs-security.json` to yours:
```JSON
"oauth2-configuration": {
        "redirect-uris": [
            "<- Change here ->"
    ]
  }
```

3. Build the application:

```bash
mvn clean install
```
4. Install `approuter` package in web directory:

```bash
cd web
npm install @sap/approuter --save
```

5. Deploy to SAP BTP Cloud Foundry:

> Make sure you always run cf push in the folder where the `manifest.yml` file is located.

```bash
cf push
```

6. Create XSUAA service instance:

```bash
cf create-service xsuaa application javauaa -c xs-security.json
```

7. Bind the service:

```bash
cf bind-service helloworld javauaa
cf bind-service web javauaa
```
8. Assign Roles to a User in SAP BTP Cockpit:

9. Access the application through the App Router URL provided after deployment:

### Security

- Authentication is handled by XSUAA service
- Application uses stateless session management
- JWT tokens are used for authorization
- Role templates defined in `xs-security.json`
- Protected endpoints require specific scopes

### Tutorial
<details>
  <summary>Click to Step-by-step tutorial</summary>

1. Create two basic Spring Boot project

2.  Configure Spring Boot

- Edit `pom.xml` to include Spring Boot dependencies

3. Create a Simple Controller

4.  Set Up the Web Frontend

-  Create web directory and initialize npm
```bash

mkdir -p web
cd web
npm init -y
npm install express @sap/approuter

# Create package.json
cat > package.json << EOF
{
  "name": "web",
  "version": "1.0.0",
  "description": "Web frontend for HelloWorld app",
  "main": "index.js",
  "scripts": {
    "start": "node node_modules/@sap/approuter/approuter.js"
  },
  "dependencies": {
    "@sap/approuter": "^10.0.0",
    "express": "^4.17.1"
  }
}
EOF
cd ..
```

5. Configure Security and Routing
Create security configuration with basic scopes, roles and role collections in `xs-security.json`:
> Also you can create role collections in SAP BTP cockpit
```bash
cat > xs-security.json << EOF
{
  "xsappname" : "helloworld",
  "tenant-mode" : "dedicated",
  "scopes": [
    {
      "name": "\$XSAPPNAME.Display",
      "description": "Display content"
    }
  ],
  "role-templates": [
    {
      "name": "Viewer",
      "description": "View content",
      "scope-references": [
        "\$XSAPPNAME.Display"
      ]
    }
  ],
  "role-collections": [
    {
      "name": "Viewer",
      "role-template-references": [
        "\$XSAPPNAME.Viewer"
      ]
    }
  ],
  "oauth2-configuration": {
    "redirect-uris": [
      "https://*.cfapps.us10-001.hana.ondemand.com/**"
    ]
  }
}
EOF
``` 
6.  Configure Routing

```
cat > web/xs-app.json << EOF
{
  "authenticationMethod": "route",
  "routes": [
    {
      "source": "^/helloworld/(.*)$",
      "target": "\$1",
      "destination": "helloworld",
      "authenticationType": "xsuaa",
      "csrfProtection": false
    }
  ]
}
EOF
```

7.  Create Cloud Foundry Deployment Manifest

```bash
cat > manifest.yml << EOF
applications:
  - name: helloworld
    random-route: true
    path: ./target/java-tutorial-0.0.1-SNAPSHOT.jar
    memory: 1024M
    buildpacks:
      - sap_java_buildpack_jakarta
    env:
      TARGET_RUNTIME: tomcat
      JBP_CONFIG_COMPONENTS: "jres: ['com.sap.xs.java.buildpack.jdk.SAPMachineJDK']"
      JBP_CONFIG_SAP_MACHINE_JDK : "{ version: 21.+ }"
    services:
      - javauaa
  - name: web
    random-route: true
    path: web
    memory: 1024M
    env:
      destinations: >
        [
          {
            "name":"helloworld",
            "url":"https://helloworld-reliable-echidna-ln.cfapps.us10-001.hana.ondemand.com/",
            "forwardAuthToken": true
          }
        ]
    services:
      - javauaa
EOF
```

8. Create and bind services

```bash
# Create XSUAA Service
cf create-service xsuaa application javauaa -c xs-security.json
```
9. Build and Deploy
```bash
# Build the Java backend
mvn clean package
# Deploy both applications
cf push
```
10.  Update Destination URL

After deployment, update the destination URL in the manifest.yml file with the actual URL of your helloworld application, then redeploy the web app

11. Access the Application

Access your application at the route displayed in the CF push output.

</details>

</details>

## 2. Manifest_log_project
Same as a first app, but with log service. Just add logging service to the `manifest.yml` file and bind it to the application.
## 3. Destination_apps

A Spring Boot application demonstrating the usage of SAP BTP Destination Service to make HTTP calls between applications, with security implementation using XSUAA and Spring Security.

<details>
  <summary>Click to expand</summary>

### Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Security
- SAP Cloud SDK
- SAP BTP Destination Service
- XSUAA Authentication
- Maven

### Project Structure

- `java-tutorial/` - Main application directory (uses XSUAA)
  - `src/` 
    - `MainService.java` - Service for handling destination calls
    - `MainController.java` - Secured REST endpoints
  - `manifest.yml` - Cloud Foundry deployment descriptor
  - `xs-security.json` - XSUAA service configuration

- `example/` - Main application directory (uses Spring Security)
  - `src/` 
    - `MainController.java` - Secured REST endpoints
  - `manifest.yml` - Cloud Foundry deployment descriptor
  - `.env` - stores environmental variables

### Security Implementation

- Uses XSUAA for authentication and authorization
- Spring Security configuration for endpoint protection
- Token-based authentication with JWT
- Secure communication between applications

### How to Run
1. Configure `example/.env` using `example/.env.template`:
```bash
cp .env.example .env
nano .env # or you can use text editor

# After that just set enviromantal variables
```
2. Create required services:
```bash
cf create-service xsuaa application javauaa -c xs-security.json
cf create-service destination lite destination-service
```

3. Configure destination in SAP BTP Cockpit:
   - Name: destination-unsecured-app
   - URL: Your target application URL
   - Authentication: BasicAuthentication (for this example)

4. Build the application(both of them):
```bash
mvn clean install
```

5. Deploy to SAP BTP Cloud Foundry(both of them):
```bash
cf push
```

5. Bind the services:
```bash
cf bind-service helloworld destination-service
cf bind-service helloworld javauaa
```

### Key Features

- Integration with SAP BTP Destination Service
- Secured endpoints using Spring Security and XSUAA
- Support for HTTP destinations
- Automatic handling of destination configurations
- Error handling for destination calls

### Configuration

Example destination configuration in `manifest.yml`:
```yaml
services:
  - destination-service
  - javauaa
env:
  destinations: >
    [
      {
        "name":"helloworld",
        "url":"https://your-app-url",
        "forwardAuthToken": true
      }
    ]
```

To access the application:
1. Assign appropriate roles in SAP BTP Cockpit
2. Use the application router URL
3. Authenticate with your SAP BTP credentials

### Tutorial
<details>
  <summary>Click to expand step-by-step tutorial</summary>


1. Create a two Spring Boot Applications

```bash
mvn archetype:generate \
    -DgroupId=com.example.java.tutorial \
    -DartifactId=helloworld \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DarchetypeVersion=1.4 \
    -DinteractiveMode=false

cd helloworld
# do the same, but change name
```
2. Add simple endpoint which returns String in second application
3. Configure Spring Boot Parent and Dependencies

Update your `pom.xml` to include Spring Boot parent and necessary dependencies:

```xml
 
        <!-- SAP Cloud SDK -->
        <dependency>
            <groupId>com.sap.cloud.sdk</groupId>
            <artifactId>sdk-core</artifactId>
            <version>${cloud.sdk.version}</version>
        </dependency>
        
        <!-- XSUAA -->
        <dependency>
            <groupId>com.sap.cloud.security.xsuaa</groupId>
            <artifactId>xsuaa-spring-boot-starter</artifactId>
            <version>2.13.0</version>
        </dependency>
        
 ```

4. Create the Application Structure

```bash
# Create package structure
mkdir -p src/main/java/com/example/java/tutorial
mkdir -p src/main/resources
mkdir -p src/test/resources
mkdir -p web

# Create test properties file
echo "spring.security.user.name=admin" > src/test/resources/application-test.properties
echo "spring.security.user.password=admin" >> src/test/resources/application-test.properties
```

5. Create the DestinationService Class
> You can use this snippet
```java
        HttpDestination destination = DestinationAccessor.getLoader().tryGetDestination(DESTINATION_NAME)
                .get().asHttp();
        HttpClient client = HttpClientAccessor.getHttpClient(destination);
        HttpGet httpGet = new HttpGet(REL_URL);
        HttpResponse httpResponse = null;
        String responseString = "";
        try {
            httpResponse = client.execute(httpGet);
            responseString = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseString;
    }
```

6. Create the DestinationController Class
> You can use this snippet
```java
    @GetMapping
    @PreAuthorize("hasRole('Viewer') or hasAuthority('Display')")
    public ResponseEntity<String> readAnotherAppData() {
    // Call the service to fetch data and return the response
    return ResponseEntity.ok(service.getAnotherAppData());
}
```

7. Create the xs-app.json File for App Router
```bash
cat > web/xs-app.json << 'EOF'
{
  "authenticationMethod": "route",
  "routes": [
    {
      "source": "^/helloworld/(.*)$",
      "target": "$1",
      "destination": "helloworld",
      "authenticationType": "xsuaa",
      "csrfProtection": false
    }
  ]
}
EOF
```

8. Set Up the Node.js App Router
```bash
# Navigate to the web directory
cd web

# Initialize package.json
npm init -y

# Install required dependencies
npm install @sap/approuter --save

# Create start script in package.json
cat > package.json << 'EOF'
{
  "name": "approuter",
  "version": "1.0.0",
  "description": "App Router for SAP BTP Application",
  "main": "index.js",
  "scripts": {
    "start": "node node_modules/@sap/approuter/approuter.js"
  },
  "dependencies": {
    "@sap/approuter": "^11.0.0"
  }
}
EOF

# Return to the project root
cd ..
```

9. Create SAP BTP Deployment Files

- Create manifest.yml
-  Create XSUAA service configuration

10. Build and Deploy the Application

```bash
# Build the Java application
mvn clean package
# Create XSUAA service
cf create-service xsuaa application xsuaa-service -c xs-security.json

# Deploy the applications
cf push
```
</details>
</details>

## 4. Mta_app

A simple multi-target application (MTA) deployed on SAP Business Technology Platform (BTP) Cloud Foundry environment. The application consists of a Java backend service and a web frontend module.

<details>
  <summary>Click to expand</summary>


### Project Structure

```
java-tutorial/
├── web/                  # Web frontend module
│   ├── xs-app.json      # Application router configuration
│   └── ...
├── src/                  # Java backend sources
├── mta.yaml             # MTA deployment descriptor
├── xs-security.json     # Security configuration
└── manifest.yml         # Cloud Foundry manifest
```

### Components

- **Java Backend Module (helloworld)**
  - Built with Java and Tomcat
  - Uses SAP Java Buildpack Jakarta
  - JDK Version: 21+
  - Memory allocation: 1024MB

- **Web Frontend Module (web)**
  - Application Router
  - Handles authentication and routing
  - Routes requests to the Java backend

### Prerequisites

1. SAP BTP Cloud Foundry Environment account
2. Cloud Foundry CLI installed
3. MultiApps CF CLI Plugin installed
4. Java JDK 21 or higher
5. Maven
6. Node.js and npm

### Required Services

- XSUAA Service (Authentication & Authorization)
- Application Logging Service

### Configuration

#### Security
The application uses XSUAA for authentication with the following configuration:
- Application name: helloworld
- Tenant mode: dedicated
- Scope: $XSAPPNAME.Display
- Role: Viewer


### Building and Deployment
1. Change routing url:
```yml
properties:
          name: helloworld
          url: <- Change here ->
          forwardAuthToken: true
```
2. Build the MTA archive:
```bash
mbt build
```

3. Deploy to Cloud Foundry:
```bash
cf deploy mta_archives/helloworld-app_0.0.1.mtar
```

### Troubleshooting

If you encounter deployment issues:

1. Check the application logs:
```bash
cf logs helloworld --recent
```

2. Verify service bindings:
```bash
cf services
```

3. Ensure all required services are created and bound:
```bash
cf create-service xsuaa application javauaa
cf create-service application-logs lite app-logging-service
```

### Tutorial
<details>
  <summary>Click to expand step-by-step tutorial</summary>

1. Copy first project 
```bash
cp -r 1.Manifest_project/java-tutorial 4.Mta_app/java-tutorial
```
2. Create mta.yaml
> This file describes deployment of app and all used applications, services


3. Deploy to SAP BTP platform
```bash
mbt build -t gen --mtar mta.mtar
cf deploy gen/mta.mtar --abort-on-error --delete-services
# flag --abort-on-error stoped application deployment if any error occured
# flag --delete-service deletes all services and creates new ones 
```

The application will be accessible at: `https://<app-host>.cfapps.eu10.hana.ondemand.com/helloworld/`

</details>
</details>

## 5. OData-project
A Spring Boot application demonstrating integration with OData services through SAP BTP Destination Service. This project implements a proxy controller to forward requests to the Northwind OData service.

<details>
  <summary>Click to expand</summary>

### Technologies Used

- Java 21
- Spring Boot 3.4.4
- SAP Cloud SDK
- SAP BTP Destination Service
- OData V4
- Lombok
- Maven

### Project Structure

- `odata-explore/` - Main application directory
  - `src/main/java/com/example/odata_explore/`
    - `ProxyController.java` - REST controller handling incoming requests
    - `DestinationService.java` - Service managing destination connections
  - `manifest.yml` - Cloud Foundry deployment descriptor

### Components

#### ProxyController
- Handles all incoming HTTP GET requests
- Forwards requests to the configured OData service
- Supports query parameters

#### DestinationService
- Manages connection to Northwind OData service
- Handles request transformation and routing
- Implements error handling

### Configuration

1. Configure Destination in SAP BTP Cockpit:
   - Name: `Northwind`
   - URL: OData service URL
   - Authentication: NoAuthentication
   - ProxyType: Internet

2. Required services:
```yaml
services:
  - destination-service
```

### How to run 
1. Create destination service:
```bash
cf create-service destination lite destination-service
```
2. Configure Northwind destination in SAP BTP Cockpit
3. Build the application
```bash
mvn clean install
```
4. Deploy to Cloud Foundry
```bash
cf push
```
5. Bind the service:
```bash
cf bind-service odata-explore destination-service
```
### Usage Example
Access OData endpoints through the proxy:
1. Get all Categories:
```http
https://<-your adress->/Categories
```

2. Get specific Product:
```http
https://<-your adress->/Products(1)
```
3. Query with filters
```http
https://<-your adress->/Products?$filter=Price gt 20
```

### Tutorial
<details>
  <summary>Click to expand step-by-step tutorial</summary>

1. Create Spring Boot project

```bash
mkdir odata-explore
cd odata-explore
```

Generate a Spring Boot project using Spring Initialzr:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project \
-d language=java -d bootVersion=3.2.0 -d baseDir=. \
-d groupId=com.example.odata.explore -d artifactId=odata-explore \
-d name=odata-explore -d packageName=com.example.odata.explore \
-d dependencies=web,lombok -d javaVersion=21 | tar -xzvf -
```

2. Create service interface

Create directory structure:

```bash
mkdir -p src/main/java/com/example/odata/explore/service
```

Create `src/main/java/com/example/odata/explore/service/DestinationService.java`:

```java
public interface DestinationService {
    String getData(HttpServletRequest request, Map<String, String> allParams);
}
```

3. Create controller

Create directory:

```bash
mkdir -p src/main/java/com/example/odata/explore/controller
```

Create `src/main/java/com/example/odata/explore/controller/ProxyController.java`:

```java

    @GetMapping("/**")
    public ResponseEntity<String> getData(HttpServletRequest request, @RequestParam Map<String, String> allParams) {
        return ResponseEntity.ok(destinationService.getData(request, allParams));
    }
}
```

4. Implement service class

Create a service implementation class (you'll need to implement the `getData` method based on your specific requirements):

```bash
touch src/main/java/com/example/odata/explore/service/DestinationServiceImpl.java
```

5. Update pom.xml

Add SAP Cloud SDK dependencies to your `pom.xml`:

```bash
# Edit pom.xml to add SAP Cloud SDK dependencies for connectivity
```

6. Create Cloud Foundry manifest

Create `manifest.yaml` in the root directory:

```yaml
applications:
  - name: o-data-app
    random-route: true
    path: ./target/odata-explore-0.0.1-SNAPSHOT.jar
    memory: 1024M
    buildpacks:
      - sap_java_buildpack_jakarta
    env:
      TARGET_RUNTIME: tomcat
      JBP_CONFIG_COMPONENTS: "jres: ['com.sap.xs.java.buildpack.jdk.SAPMachineJDK']"
      JBP_CONFIG_SAP_MACHINE_JDK : "{ version: 21.+ }"
    services:
      - app-logging-service
      - destination-service
```

7. Build the application

```bash
mvn clean package
```

8. Create services and deploy to SAP BTP platform

```bash
# Create required services
cf create-service application-logs lite app-logging-service
cf create-service destination lite destination-service

# Bind the services to the application
cf bind-service o-data-app app-logging-service
cf bind-service o-data-app destination-service

# Deploy the application
cf push
```

9. Access the application

Once deployed, the application will proxy requests to OData services configured in your SAP BTP destinations.

Access via: `https://<your-app-url>/<destination-path>`

</details>

</details>

## 7. Hana_cloud
A Spring Boot application demonstrating integration with SAP HANA Cloud database service on SAP BTP Cloud Foundry environment, with security implementation using XSUAA.

<details>
  <summary>Click to expand</summary>

### Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Security
- SAP HANA Cloud
- XSUAA Authentication
- SAP Java Buildpack Jakarta
- Maven

### Project Structure

- `java-tutorial/` - Main application directory
  - `src/` - Source code
  - `web/` - Application router configuration
  - `mta.yaml` - MTA deployment descriptor
  - `xs-security.json` - Security configuration

### Components

- **Java Backend Module (helloworld)**
  - Built with Java and Tomcat
  - Connects to HANA Cloud database
  - Protected by XSUAA authentication

- **Web Frontend Module (web)**
  - Application Router
  - Handles authentication and routing
  - Routes requests to the Java backend

### Required Services

- HANA Cloud (Trial) Service
- XSUAA Service (Authentication)
- Application Logging Service

### Configuration

The application is configured in [`mta.yaml`](7.Hana_cloud/java-tutorial/mta.yaml) with the following services:

```yaml
resources:
  - name: hana-db
    type: org.cloudfoundry.managed-service
    parameters:
      service: hana-cloud-trial
      service-plan: hana
      service-name: hana-db
```

### Building and Deployment
1. Create required services:

Create SAP HANA Cloud instance in SAP BTP Cloud foundry
> Make sure that you a making Hana instance in BTP CF environment
```bash
cf create-service xsuaa application javauaa
cf create-service application-logs lite app-logging-service
```
2. Change routing url in `mta.yaml`:
```bash
properties:
  name: helloworld
  url: <- Change to your application URL ->
  forwardAuthToken: true
```

3. Build the MTA archive:
```bash
mbt build
```
4. Build the MTA archive:
```bash
cf deploy mta_archives/helloworld-app_0.0.1.mtar
```

### Security

- Authentication handled by XSUAA service
- Application uses stateless session management
- Protected endpoints require specific scopes
- Role-based access control configured in `mta.yaml`

### Tutorial
<details>
  <summary>Click to expand step-by-step tutorial</summary>

1. Prepare your SAP BTP environment
> Subscribe Hana service instance and create database

2. Create a new Spring Boot project

```bash
mkdir java-tutorial
cd java-tutorial
```

3. Initialize Maven project

Create a basic Maven project structure or use Spring Initializr:

```bash
mvn archetype:generate -DgroupId=com.example.java.tutorial \
  -DartifactId=java-tutorial \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 \
  -DinteractiveMode=false
```

4. Set up project dependencies

Update `pom.xml` to include Spring Boot, JPA, Lombok, and other dependencies:

```bash
# Navigate to the project directory
cd java-tutorial

# Update pom.xml with required dependencies:
# - spring-boot-starter-web
# - spring-boot-starter-data-jpa
# - lombok
# - h2 (for development)
```

5. Create the model class

```bash
mkdir -p src/main/java/com/example/java/tutorial/model
```

Create `Book.java` in the model package with appropriate fields (id, title, author, etc.)

6. Create the repository interface

```bash
mkdir -p src/main/java/com/example/java/tutorial/repository
```

Create `BookRepository.java` extending JpaRepository

7. Create the exception classes

```bash
mkdir -p src/main/java/com/example/java/tutorial/exception
```

Create `ResourceNotFoundException.java` to handle cases when books aren't found

8. Create the service layer

```bash
mkdir -p src/main/java/com/example/java/tutorial/service
```

Create `BookService.java` interface and `BookServiceImpl.java` implementation

9. Create the controller layer

```bash
mkdir -p src/main/java/com/example/java/tutorial/controller
```

Create `BookController.java` with REST endpoints:
- GET /books - Get all books
- GET /books/{id} - Get book by ID
- POST /books - Create a new book
- DELETE /books/{id} - Delete a book by ID

10. Configure application properties

Create appropriate application properties files:

```bash
mkdir -p src/main/resources
touch src/main/resources/application.properties
touch src/main/resources/application-cf.properties
```

Edit `application-cf.properties` with HANA database configuration for cloud deployment

11. Set up SAP Cloud Platform configuration

```bash
mkdir -p web
```

Create `xs-app.json` in the web directory to configure routes for SAP Cloud Platform deployment

12. Create mta.yaml

13. Deploy to SAP BTP platform
```bash
mbt build -t gen --mtar mta.mtar
cf deploy gen/mta.mtar --abort-on-error --delete-services
# flag --abort-on-error stoped application deployment if any error occurred
# flag --delete-service deletes all services and creates new ones 
```

</details>

</details>

## 8. Manifest_multitenant
A Spring Boot application demonstrating how to implement a multitenant application on SAP BTP Cloud Foundry environment using the SAP SaaS Provisioning service.

<details>
  <summary>Click to expand</summary>

### Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Security
- SAP XSUAA Authentication (Multitenant)
- SAP SaaS Provisioning Service
- SAP BTP Application Router
- Maven

### Project Structure

- `java-tutorial/` - Main application directory
  - `src/` - Source code
    - `TenantProvisioningController.java` - Handles tenant subscription/unsubscription
    - `WebSecurityConfig.java` - Security configuration with tenant-aware settings
    - `MainController.java` - Main endpoint displaying tenant information
  - `web/` - Application router configuration
    - `xs-app.json` - Routing configuration with tenant-specific routes
  - `manifest.yml` - Cloud Foundry deployment descriptor
  - `xs-security.json` - XSUAA security configuration
  - `saas-config.json` - SaaS provisioning configuration

### Key Features

- Multi-tenancy support with tenant isolation
- Tenant subscription/unsubscription callbacks
- Dynamic tenant URL generation
- Tenant-aware security configuration
- Subscription management API

### Security Implementation

The application uses a shared tenant mode in XSUAA with:
- Custom scopes for tenant management
- Tenant-specific authentication
- Callback endpoints for tenant provisioning
- Special role for subscription management

### How It Works

1. Tenant subscribes to the application via the SAP BTP Cockpit
2. Subscription request triggers callback to `/callback/v1.0/tenants/{tenantId}`
3. Application registers the tenant and returns a tenant-specific URL
4. Tenants access the application through their subdomain URL
5. Security context includes tenant information for data isolation
6. Each tenant has its own JWT token with tenant-specific scopes

### How to Run

1. Create required services:
```bash
cf create-service saas-registry application helloworld-registry -c saas-config.json
cf create-service xsuaa application javauaa -c xs-security.json
cf create-service application-logs lite app-logging-service
```

2. Update URLs in configuration files:
 - Change `TENANT_HOST_PATTERN` in `manifest.yml`
 - Update `BASE_DOMAIN` in `TentantProvisioningController.java`
 - Adjust `appUrls.onSubscription` in `saas-config.java`

3. Build the application:
```bash
mvn clean install
```
4. Install the approuter:
```bash
cd web
npm install @sap/approuter --save
```
5. Deploy to Cloud Foundry:
```bash
cf push
```
6. Make the application available for subscription:
```bash
cf update-service helloworld-registry -c saas-config.json
```

7. Create a route for the consumer subaccount
```bash
cf map-route <- xsuaa-app -> cfapps.eu10.hana.ondemand.com --hostname <- customer subdomain- >-<- app-name ->
# In my case:
cf map-route web cfapps.us10-001.hana.ondemand.com --hostname customer-hosw9v1y-web  

```

8. Restage app
```bash
cf restage <- app name ->
```
8. Subscribe a tenant to your application
- Go to the subaccount that will subscribe to the app
- Navigate to Service Marketplace
- Find your application (Hello World App)
- Create a subscription


### Tenant-specific URLs
After subscription, tenants access the application through:
```http
https://{tenant-subdomain}.{base-domain}/helloworld
```
### Multitenant Configuration
The `xs-security.json` is configured with:
```json
"tenant-mode": "shared"
```
And includes special scopes for tenant onboarding:

```json
{
  "name": "$XSAPPNAME.Callback",
  "grant-as-authority-to-apps": [
    "$XSAPPNAME(application,sap-provisioning,tenant-onboarding)"
  ]
}
```
### Tutorial
<details>
  <summary>Click to expand step-by-step tutorial</summary>

1. Create Spring Boot Project

```bash
mvn archetype:generate -DgroupId=com.leverx.imwrdo -DartifactId=helloworld -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```

Or use Spring Initializer:

2. Configure Dependencies

Update `pom.xml` with the following dependencies:
- Spring Boot Starter Web and Actuator
- SAP XSUAA Spring Boot Starter
- SAP Spring Security
- Lombok
- Spring Data JPA
- SAP HANA JDBC Driver
- Liquibase HANA DB Extension
- SAP Cloud Instance Manager Client
- SAP Cloud SDK

3. Create Application Structure

Create the following package structure:
```
src/main/java/com/leverx/imwrdo/helloworld/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── model/            # Entity classes
├── repository/       # Data repositories
├── service/          # Business logic services
│   └── tenant/       # Tenant-related services
└── HelloworldApplication.java
```
4. Create Data Model

Create entity classes in the `model` package with JPA annotations.
5. Create service manager configuration
> It will be used for Hana-schema instances creation

6.  Create Tenant Management Classes

- Create `LiquibaseService`  for tenant schema management
- Create `TenantDBContainerService`  for DB containers management
- Create `TenantDataSourceService`  for dynamic DataSource switching for appropriate tenants
- Create tenant resolver classes to identify tenants from requests

7.  Configure Liquibase

Create Liquibase changelog files in `src/main/resources/db/changelog/`

8. Set Up SAP BTP Configuration

1. Create `manifest.yml` file for Cloud Foundry deployment with:
    - Application name, memory, routes
    - Environment variables including `TENANT_HOST_PATTERN`
    - Service bindings to UAA and API services

9.  Configure SAP BTP Security

1. Create XSUAA service instance
2. Configure Spring Security to use XSUAA

10. Build the Application

```bash
mvn clean package
```

11. Deploy to SAP BTP

```bash
cf push -f manifest.yml
```

12. Register the Application for Multi-Tenancy

```bash
cf create-service saas-registry application -c '{"xsappname":"<xsapp-name>", "appName":"<app-name>", "displayName":"<display-name>", "description":"<description>", "category":"<category>"}'
cf bind-service helloworld <saas-registry-service-name>
cf restart helloworld
```

13. Subscribe Tenants

Tenants can now subscribe to your application through the SAP BTP Cockpit.
The application uses the `TENANT_HOST_PATTERN` to identify tenants from the request URL and create tenant-specific database schemas using the `LiquibaseService`.

</details>  


</details>


## 9. MTA_Multitenant
A multi-tenant Spring Boot application deployed on SAP BTP Cloud Foundry environment. This project demonstrates database multi-tenancy with tenant isolation, dynamic schema management, and tenant-aware security using XSUAA and the SAP Service Manager.
<details>
  <summary>Click to expand</summary>


### Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Security
- SAP XSUAA Authentication (Multitenant)
- SAP Service Manager Client
- SAP SaaS Provisioning Service
- Liquibase for database migrations
- Maven

### Project Structure

- `helloworld/` - Main application directory
  - `src/` - Source code
    - `controller/` - REST controllers for handling API requests
    - `service/` - Business logic for tenant and book management
    - `repository/` - Data access layer
    - `config/` - Security and application configuration
    - `util/` - Utility classes for tenant schema management
  - `pom.xml` - Maven build configuration
- `web/` - Application router configuration
  - `xs-app.json` - Routing configuration with tenant-specific routes
- `mta.yaml` - Multi-target application deployment descriptor
- `service-config/` - Folder with Service configuration

### Key Features

- **Multi-Tenancy**: Each tenant has its own isolated database schema.
- **Dynamic Schema Management**: Tenant schemas are created and managed dynamically using Liquibase.
- **Tenant Subscription Management**: Handles tenant subscription and unsubscription events via SaaS Provisioning Service.
- **Tenant-Aware Security**: Implements tenant-specific authentication and authorization using XSUAA.
- **RESTful APIs**: Provides endpoints for managing books and tenant-specific data.

### How It Works

1. **Tenant Subscription**:
   - A tenant subscribes to the application via SAP BTP Cockpit.
   - The application creates a database service instance for the tenant using Service Manager
   - A schema is created and initialized with Liquibase migrations
   - A dedicated data source is created for the tenant
2. **Request Processing**
   - When a request arrives, the tenant ID is extracted from the JWT token
   - The ``MultitenantDataSourceRouter`` routes to the corresponding tenant data source

3. **Tenant Isolation**:
   - Each tenant's data is stored in a separate schema.
   - The application dynamically switches schemas based on the tenant context.
   - Hibernate connects to the tenant-specific schema
   - Data operations are isolated to the tenant's schema

4. **Security**:
   - Authentication and authorization are handled by XSUAA in shared tenant mode.
   - JWT tokens include tenant-specific scopes for secure access.

### How to Run

1. Build archive:
  ```bash
   mbt build
   ```
2. Deploy project:
  ```bash
   cf deploy mta_archives/helloworld-app_0.0.1.mtar
   ```
3. Subscribe a Tenant

  - Navigate to the Service Marketplace in the subscribing subaccount
  - Find the application and create a subscription

4. Create a route for the consumer subaccount
```bash
cf map-route <- app-router-app -> cfapps.eu10.hana.ondemand.com --hostname <- customer subdomain- >-<- app-router-app ->
# In my case:
cf map-route web cfapps.us10-001.hana.ondemand.com --hostname customer-hosw9v1y-web  
```

5. Restage app
```bash
cf restage <- app name ->
```

</details>
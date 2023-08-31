# owasp-nightmare
A Spring Boot app showcasing a CSRF vulnerability and fix.

## What does it do?
You can sign in to the app via Google login and post messages in a shared chat (stored in memory).

There is an accompanying [static page](src/main/resources/static/surf.html) that can be visited to demonstrate a CSRF attack.

### Fixed Version
The `fixed` branch updates the application to resist CSRF attacks.

## Running the App

### Google Login

This application uses Google login. To setup Google login, follow the instructions [here](https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/oauth2/login).

### Running
You can run this app from the main method in the [OwaspNightmareApplication class](src/main/java/ca/tjug/owaspnightmare/OwaspNightmareApplication.java) from your IDE of choice, or via Maven:
```bash
./mvnw spring-boot:run
```

You will need to configure the client ID and secret for your Google login client in the [application.yml](src/main/resources/application.yml) file or via runtime configuration (environment variables or system properties).

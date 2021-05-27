# Scale Up Your App With Distributed Systems
Multi-Module Gradle project which includes: gps, web, rewards and shared project.
## Build and run project
### Build GRADLE project:
- gradle build

### Build DOCKER with docker compose build:
1. Building up mySql: 
    - docker -compose up mysql
3. Building up webApp first because it generates all users: 
    - docker -compose up web
4. Building up other two apps: gpsApp and rewardsApp:
    - docker -compose up gps
    - docker -compose up rewards

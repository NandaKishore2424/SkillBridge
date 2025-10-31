# Deployment Guide - SkillBridge

## Prerequisites

### System Requirements
- Java 17 or higher
- Node.js 16 or higher
- PostgreSQL 13 or higher
- Gradle 7.x
- 2GB RAM minimum
- 10GB storage minimum

### Required Tools
- Git
- Docker (optional)
- npm/yarn
- Maven/Gradle

## Environment Setup

### 1. Database Setup

#### 1.1 Using Supabase
1. Create a Supabase account
2. Create a new project
3. Get the connection details:
   - Database URL
   - Database username
   - Database password

#### 1.2 Database Migration
```bash
# Run the database migration script
psql <database_url> -f skillbridge_backup.sql
```

### 2. Backend Deployment

#### 2.1 Environment Variables
Create a `.env` file:
```env
# Database Configuration
SUPABASE_URL=your_supabase_url
SUPABASE_USER=your_supabase_user
SUPABASE_PASSWORD=your_supabase_password

# JWT Configuration
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000

# Server Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

#### 2.2 Build Process
```bash
# Clone repository
git clone https://github.com/NandaKishore2424/SkillBridge.git
cd SkillBridge

# Build application
./gradlew clean build

# Run tests
./gradlew test

# Start application
java -jar build/libs/skillbridge-0.0.1-SNAPSHOT.jar
```

#### 2.3 Using Docker
```bash
# Build Docker image
docker build -t skillbridge-backend .

# Run container
docker run -d \
  --name skillbridge-backend \
  -p 8080:8080 \
  --env-file .env \
  skillbridge-backend
```

### 3. Frontend Deployment

#### 3.1 Environment Setup
Create a `.env` file in the frontend directory:
```env
REACT_APP_API_URL=http://your-backend-url:8080
REACT_APP_ENV=production
```

#### 3.2 Build Process
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Build for production
npm run build

# Start production server
npm start
```

#### 3.3 Using Docker
```bash
# Build Docker image
docker build -t skillbridge-frontend .

# Run container
docker run -d \
  --name skillbridge-frontend \
  -p 3000:3000 \
  --env-file .env \
  skillbridge-frontend
```

### 4. Monitoring Setup

#### 4.1 Application Metrics
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

#### 4.2 Logging Configuration
```yaml
logging:
  level:
    root: INFO
    com.college.skillbridge: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/skillbridge.log
```

### 5. Security Configuration

#### 5.1 SSL/TLS Setup
```yaml
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEY_STORE_PASSWORD}
    key-store-type: PKCS12
    key-alias: tomcat
    enabled: true
```

#### 5.2 CORS Configuration
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://your-frontend-domain.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

### 6. Performance Optimization

#### 6.1 JVM Configuration
```bash
java -Xms1g -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar build/libs/skillbridge-0.0.1-SNAPSHOT.jar
```

#### 6.2 Database Connection Pool
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
```

### 7. Backup Strategy

#### 7.1 Database Backup
```bash
#!/bin/bash
# backup.sh
pg_dump -U $DB_USER -h $DB_HOST $DB_NAME > backup_$(date +%Y%m%d).sql
```

#### 7.2 Application Backup
```bash
#!/bin/bash
# app_backup.sh
tar -czf backup_$(date +%Y%m%d).tar.gz \
    ./config \
    ./logs \
    .env
```

### 8. Monitoring & Maintenance

#### 8.1 Health Check Endpoints
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

#### 8.2 Regular Maintenance Tasks
1. Log rotation
2. Database backup
3. Security updates
4. Performance monitoring
5. Error tracking

### 9. Troubleshooting

#### 9.1 Common Issues
1. Database connection issues
   ```bash
   # Check database connectivity
   pg_isready -h $DB_HOST -p $DB_PORT -d $DB_NAME
   ```

2. Memory issues
   ```bash
   # Check memory usage
   jmap -heap <pid>
   ```

3. Performance issues
   ```bash
   # Check thread dumps
   jstack <pid>
   ```

#### 9.2 Logs Location
- Application logs: `/logs/skillbridge.log`
- Access logs: `/logs/access.log`
- Error logs: `/logs/error.log`

### 10. Scaling Strategy

#### 10.1 Horizontal Scaling
```yaml
# docker-compose.yml for scaling
version: '3'
services:
  backend:
    image: skillbridge-backend
    deploy:
      replicas: 3
    ports:
      - "8080-8082:8080"
```

#### 10.2 Load Balancer Configuration
```nginx
# nginx.conf
upstream backend {
    server backend1:8080;
    server backend2:8081;
    server backend3:8082;
}

server {
    listen 80;
    location /api {
        proxy_pass http://backend;
    }
}
```
.
├── backend
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       ├── java
│   │       │   └── com
│   │       │       └── shopbackend
│   │       │           ├── ShopBackendApplication.java
│   │       │           └── controller
│   │       │               └── ShopController.java
│   │       └── resources
│   │           └── application.properties
│   └── target
│       ├── classes
│       │   ├── application.properties
│       │   └── com
│       │       └── shopbackend
│       │           ├── ShopBackendApplication.class
│       │           └── controller
│       │               └── ShopController.class
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       └── compile
│       │           └── default-compile
│       │               ├── createdFiles.lst
│       │               └── inputFiles.lst
│       ├── shopbackend-0.0.1-SNAPSHOT.jar
│       └── shopbackend-0.0.1-SNAPSHOT.jar.original
├── docker-compose.yml
└── frontend
    ├── Dockerfile
    ├── images
    │   └── shop1.jpg
    └── index.html


    -------------------------------------------------------------------

   Complete Step-by-Step Guide: Full Project Setup, Deployment & Git Push on EC2 Root Home
1. EC2 Par Root User Ban Jao
SSH se normal user (ubuntu/ec2-user) se connect karo, fir root bano:

bash
sudo -i
cd /root
2. System Update and Install Required Software
bash
# Update system
apt update -y
apt upgrade -y

# Install Docker & Docker Compose
apt install -y docker.io git maven openjdk-17-jdk curl nano

systemctl start docker
systemctl enable docker

curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Setup Git global config (apna naam & email daal)
git config --global user.name "Your Name"
git config --global user.email "your-email@example.com"
3. Project Folder Structure and Files Banayein Root Home Me
bash
cd /root
mkdir -p backend/src/main/java/com/shopbackend/controller
mkdir -p backend/src/main/resources
mkdir -p frontend/images
4. Backend Java Code (Spring Boot)
Backend main app file /root/backend/src/main/java/com/shopbackend/ShopBackendApplication.java

bash
nano /root/backend/src/main/java/com/shopbackend/ShopBackendApplication.java
Paste and save:

java
package com.shopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopBackendApplication.class, args);
    }
}
Backend controller file /root/backend/src/main/java/com/shopbackend/controller/ShopController.java

bash
nano /root/backend/src/main/java/com/shopbackend/controller/ShopController.java
Paste and save:

java
package com.shopbackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @PostMapping("/order")
    public ResponseEntity<String> placeOrder(@RequestBody String orderDetails) {
        return new ResponseEntity<>("Order placed successfully: " + orderDetails, HttpStatus.OK);
    }

    @GetMapping("/photos")
    public String[] getShopPhotos() {
        return new String[]{"/images/shop1.jpg", "/images/shop2.jpg"};
    }
}
application.properties /root/backend/src/main/resources/application.properties

bash
nano /root/backend/src/main/resources/application.properties
Paste and save:

text
spring.datasource.url=jdbc:mysql://mysql:3306/shopdb
spring.datasource.username=root
spring.datasource.password=rootpassword
spring.jpa.hibernate.ddl-auto=update
server.port=8080
Create pom.xml in /root/backend/pom.xml with this minimum content (paste in nano):

xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.shopbackend</groupId>
  <artifactId>shopbackend</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.0</version>
    <relativePath/>
  </parent>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>runtime</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
5. Frontend Files
/root/frontend/index.html

bash
nano /root/frontend/index.html
Paste and save:

xml
<!DOCTYPE html>
<html>
<head>
    <title>Shop Photos</title>
</head>
<body>
    <h1>Welcome to My Shop</h1>
    <div id="photos"></div>

    <h2>Place Order</h2>
    <textarea id="orderDetails" rows="4" cols="50"></textarea><br>
    <button onclick="placeOrder()">Place Order</button>

    <script>
        fetch('/api/shop/photos')
        .then(response => response.json())
        .then(photos => {
            const photosDiv = document.getElementById('photos');
            photos.forEach(photo => {
                const img = document.createElement('img');
                img.src = photo;
                img.style.width = "300px";
                img.style.margin = "10px";
                photosDiv.appendChild(img);
            });
        });

        function placeOrder() {
            const orderDetails = document.getElementById('orderDetails').value;
            fetch('/api/shop/order', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(orderDetails)
            })
            .then(res => res.text())
            .then(msg => alert(msg));
        }
    </script>
</body>
</html>
Photos upload karni hain /root/frontend/images/shop1.jpg aur /root/frontend/images/shop2.jpg

(For this, tu nano se nahi karega, balki agar terminal me upload karna hai to scp ya kisi tarah upload karna padega, par tu agar full code hi dono photos bhi generate karwana chahta hai toh bata, main batata hoon.)

Frontend Dockerfile /root/frontend/Dockerfile:

bash
nano /root/frontend/Dockerfile
Paste and save:

text
FROM nginx:alpine
COPY . /usr/share/nginx/html
EXPOSE 80
6. Backend Dockerfile /root/backend/Dockerfile
bash
nano /root/backend/Dockerfile
Paste and save:

text
FROM openjdk:17
WORKDIR /app
COPY target/shopbackend.jar shopbackend.jar
EXPOSE 8080
CMD ["java", "-jar", "shopbackend.jar"]
7. Docker Compose File /root/docker-compose.yml
bash
nano /root/docker-compose.yml
Paste and save:

text
version: '3.8'
services:
  mysql:
    image: mysql:8
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: shopdb
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  backend:
    build: ./backend
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/shopdb
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql-data:
8. Backend Build & Deploy
bash
cd /root/backend
mvn clean package  # ye jar banayega target/shopbackend.jar

cd /root
docker-compose up --build -d
9. Git Repo Setup & Push Code from EC2 Root
Agar tu apne EC2 se hi repo me push karna chahta hai to:

bash
cd /root

# Initialize git repo agar pehle se nahi hai
git init

# Git remote set karo, apna repo URL daal ke:
git remote add origin <your_git_repo_url>

# Add files
git add backend frontend docker-compose.yml

git commit -m "Initial commit from EC2 root server"

# Push karo
git push -u origin master
(Note: Agar private repo hai to SSH keys ya username/password credentials setup karne honge.)

Final Notes:
Ye bhi dekh le ki Security Groups me ports 80 aur 8080 khule hon.

Sab kuch root ke home /root me ho raha hai, bina local upload ya clone ke.

Agar chahiye toh main photos banane ka simple base64 to JPG conversion ya koi aur madad bhi karwa sakta hoon poori terminal se direct. Bas bata!

bhai check kaise kru frontend backend chl rha ki nhi aur database k
bhai check kaise kru frontend backend chl rha ki nhi aur database k
To check if your frontend, backend, and database are running properly on your AWS EC2 root server, follow these steps:

1. Check Backend (Java Spring Boot API) Status
Verify if the backend container is running:

bash
docker ps
Look for the backend container running and listening on port 8080.

Test Backend API directly on the server:

bash
curl http://localhost:8080/api/shop/photos
You should get a JSON array of image paths like ["/images/shop1.jpg","/images/shop2.jpg"].

Test order API (POST):

bash
curl -X POST -H "Content-Type: application/json" -d '"test order details"' http://localhost:8080/api/shop/order
You should get a response: Order placed successfully: "test order details".

2. Check Frontend (Nginx Serving HTML)
Verify frontend container is running:

bash
docker ps
Look for the frontend container running and listening on port 80.

From inside the EC2 instance, test frontend reachability:

bash
curl http://localhost
You should see the HTML content of your index.html.

From your local browser, open:

text
http://<EC2_PUBLIC_IP>/
You should see your shop photos and order form interface.

3. Check MySQL Database Status
Verify MySQL container running:

bash
docker ps
Look for MySQL container.

Connect to MySQL inside the container:

bash
docker exec -it <mysql_container_name_or_id> mysql -uroot -p
Enter the password (e.g., rootpassword).

Run these SQL commands to check database and tables:

sql
SHOW DATABASES;
USE shopdb;
SHOW TABLES;
You should see shopdb and any tables created by JPA.

4. Check Docker Logs for Errors
For backend logs:

bash
docker logs <backend_container_name_or_id>
For frontend logs:

bash
docker logs <frontend_container_name_or_id>
For MySQL logs:

bash
docker logs <mysql_container_name_or_id>
Tips
Use docker ps frequently to verify all containers are running.

Check EC2 Security Group settings to ensure port 80 (frontend) and 8080 (backend) are open to your IP.

If curl on localhost works but browser access doesn't, it's usually a networking/security group problem.

If you want, I can guide you with exact commands to identify container names and test further. Just ask!

curl http://localhost
ky dalu exactly
curl http://localhost ky dalu exactly
Haan bhai, frontend check karne ke liye tu EC2 pe root user se ye exact command dal:

bash
curl http://localhost
Iska kya hona chahiye?

Agar tera Nginx frontend container sahi chal raha hai, to output me teri index.html wali HTML dikhegi (ya kam se kam uska source pura dikhega terminal pe).

Agar koi error aata hai ya blank output aata hai, to possible reasons:

Frontend container nahi chal raha hai.

Docker Compose me kuch gadbad hai.

Port 80 par koi dusra service chal rahi ho to conflict ho sakta hai.

Check karne ke liye:

Saare containers dekhne ke liye:

bash
docker ps
Dekh le koi container frontend ya nginx naam se 80 port par running hai ya nahi.

Agar sab normal hai, to:

bash
curl http://localhost
Ye command tu root ke terminal pe chalaye.

Agar HTML aata hai to frontend OK hai!

Bonus:

Browser se bhi check kar sakta hai:

text
http://<EC2_PUBLIC_IP>/
(Apna EC2 ka public IP daal)

Agar curl se output nahi aa raha, bata kya output aa raha hai — main bataunga kya problem ho sakta hai.
Aur agar HTML aa gya toh samajh le frontend chal raha hai!
    


## 🔐 Securing Power Distribution Through Web-Enabled Energy Theft Detection using ESP8266 & AWS

An IoT-based smart energy monitoring and theft detection system that uses an ESP8266 microcontroller to measure electrical parameters and send real-time data to a cloud-hosted Spring Boot application deployed on AWS EC2.

The system continuously monitors voltage and current, detects abnormal consumption patterns, and triggers alerts both locally and on a web dashboard.

---

## 📌 Project Overview

Electricity theft is a serious issue in power distribution systems and results in major financial losses. Traditional detection methods rely heavily on manual inspection and cannot provide real-time monitoring.

This project introduces a cloud-connected energy monitoring system that:

* Continuously measures voltage and current
* Calculates real-time power consumption
* Detects abnormal differences between supply and load
* Sends alerts instantly
* Allows remote monitoring through a web dashboard

---

## 🏗 System Architecture

The system consists of three layers:

### 1️⃣ Device Layer (ESP8266)

* Connects to WiFi network
* Reads voltage and current sensor values
* Calculates power
* Formats data in JSON
* Sends data to cloud server via REST API
* Receives response from backend

---

### 2️⃣ Backend Layer (Spring Boot – Java)

Developed using:

* Java
* Spring Boot
* Maven
* MySQL
* Spring Data JPA

Responsibilities:

* Receives sensor data via REST APIs
* Stores readings in database
* Executes theft detection logic
* Updates theft status
* Serves web dashboard
* Provides historical logs

---

### 3️⃣ Cloud Layer (AWS EC2)

The application is deployed on:

* Amazon Web Services
* Amazon EC2

Features:

* Public IP-based access
* Security group configuration for HTTP/8080
* Executable JAR deployment
* Remote global access

---

## 🔌 Hardware Components

* **ESP8266** – WiFi-enabled microcontroller
* **ACS712 Current Sensor** – Measures load current
* **Voltage Sensor Module** – Measures line voltage
* **Relay Module** – Disconnects supply during theft detection
* **Buzzer / LED** – Provides local alert indication

---

## 💻 Software Architecture

### 🔹 Controller Layer

Uses `@RestController`

Handles HTTP requests such as:

* `POST /api/energy-data`
* `GET /api/status`

---

### 🔹 Service Layer

Contains business logic:

* Power calculation
* Threshold comparison
* Theft detection algorithm
* Alert triggering

---

### 🔹 Repository Layer

Uses Spring Data JPA.

* Saves sensor readings
* Fetches historical data
* Manages database operations

---

### 🔹 Entity Layer

Represents database table with fields like:

* id
* voltage
* current
* power
* timestamp
* theftStatus

---

## 📦 Maven Dependency Management

This project uses Maven for dependency management.

Key dependencies:

* `spring-boot-starter-web`
* `spring-boot-starter-data-jpa`
* `mysql-connector-j`
* `spring-boot-starter-thymeleaf` (if UI used)

### Build the project:

```bash
mvn clean package
```

### Run the project:

```bash
java -jar target/project-name.jar
```

---

## ⚡ Theft Detection Logic

This is the core feature of the system.

The system compares:

Supply-side current vs Load-side current.

If the difference exceeds a predefined threshold:

* Theft is flagged
* Relay disconnects power
* Buzzer activates
* Status is updated on cloud
* Dashboard shows theft alert

In simple terms:

If expected power and measured power differ significantly, the system detects it as unauthorized usage and triggers alerts instantly.

---

## 🔄 Data Flow

Sensor
→ ESP8266
→ WiFi
→ Internet
→ AWS EC2
→ Spring Boot API
→ MySQL Database
→ Web Dashboard

When users open the deployed server via public IP, they can monitor:

* Voltage
* Current
* Power
* Theft Status
* Historical Logs

---

## 🔒 Security Features

* Cloud-hosted backend
* Controlled REST API endpoints
* Server-side validation
* Can integrate JWT authentication
* Real-time monitoring reduces prolonged theft

---

## 🎯 Advantages

* Real-time energy monitoring
* Remote accessibility
* Cloud scalability
* Automatic theft detection
* Reduced manual inspection
* Instant alert mechanism

---

## 🏭 Applications

* Smart grid systems
* Industrial power monitoring
* Residential apartment monitoring
* Government electricity boards
* Utility distribution networks

---

## 🎓 Viva Explanation (Short Version)

This project secures power distribution using IoT and cloud computing. The ESP8266 collects voltage and current data and sends it to a Spring Boot backend hosted on AWS EC2. The backend processes the data, stores it in MySQL, and runs theft detection logic. If abnormal consumption is detected, the system triggers alerts and can disconnect supply using a relay. The web dashboard allows real-time monitoring from anywhere through cloud access.

---

## 👤 Author

Your Name
Department / College
GitHub: [https://github.com/your-username](https://github.com/Bathreesh)


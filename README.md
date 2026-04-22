

# 🎤 Eurovision Song Contest 2026 – Voting System

This project is a **Spring Boot application** designed to manage voting and point calculation for the Eurovision Song Contest 2026.

It uses **JPA/Hibernate (ORM)** with a **MySQL database** to handle complex voting rules for both **Jury** and **Public voters** across Semi-Finals and the Grand Final.

---

## 🚀 Getting Started

### ✅ Prerequisites

* Docker installed on your machine
* MySQL (included in the container setup)

---

### 🐳 Installation (Docker)

```bash
# Clone the repository
git clone(https://github.com/petra-Nam/EuroVision)

# Navigate into the project
cd eurovision-2026

# Build Docker image
docker build -t eurovision-2026 .

# Run the container
docker run -p 8080:8080 eurovision-2026
```

The application will be available at:
👉 [http://localhost:8080](http://localhost:8080)

---

## 🛠 Features

### 🎶 Voting Logic

**Jury Voting**

* Fixed points system: `1–8, 10, 12`
* Prevents duplicate point assignment per jury member

**Public Voting**

* Up to **20 votes per user**
* Vote-based ranking system (hit-count model)

**Eligibility Rules**

* ❌ No self-voting (country cannot vote for itself)
* 🎤 Semi-Final restriction (vote only in assigned semi-final)
* ⭐ Automatic qualifiers (Big 5 + Host) handled persistently

---

### 🏆 Dynamic Grand Final Aggregation

The Grand Final lineup is **calculated dynamically** to ensure accuracy:

* Includes **6 pre-qualified countries** (Big 5 + Host)
* Adds **Top 10 from Semi-Final 1**
* Adds **Top 10 from Semi-Final 2**

✅ Prevents database inconsistencies
✅ Always reflects real-time results

---

## 📊 Database Architecture

The system is based on an **Enhanced Entity-Relationship Model (EERM)**:

* **Voters**

  * Jury
  * Public

* **Songs & Shows**

  * Many-to-Many relationship via ORM

* **Points**

  * Stores Jury rankings

* **PublicVotes**

  * Stores audience vote counts

---

## 🧱 Tech Stack

* Java
* Spring Boot
* Hibernate / JPA
* MySQL
* Docker

---

## 📁 Project Structure

```bash
src/
 ├── main/
 │   ├── java/
 │   ├── resources/
 │
 ├── test/

docs/
 └── EERM.pdf
```

---

## 📦 Submission Items

* ✅ Docker Container
* ✅ GitHub Repository
* ✅ EERM PDF (located in `/docs`)

---

## 👨‍💻 Author

Petra Namuyiga

---

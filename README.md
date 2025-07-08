# Data-Processing-Summarization-Pipeline
Absolutely, Aditi! Let’s start by creating a clean and informative **README.md** file that you can place at the root of your project. It will clearly document your architecture, setup steps, and usage.

---

## 📦 Data Processing & Summarization Pipeline

A full-stack, multi-service project that takes a website URL, scrapes relevant content, summarizes it using a Gemini-powered LLM, and stores everything in a Postgres database via a centralized Spring Boot service.

---

## 🧱 Architecture Overview

```mermaid
graph TD;
    UI[🔵 UI: React + TypeScript]
    DS[🟢 DataService: Spring Boot (JPA + Postgres)]
    FS[🟣 FastAPI Scraper Service]
    FL[🔴 Flask Summarizer Service (Gemini LLM)]
    DB[(🗄️ PostgreSQL DB)]

    UI --> DS
    DS --> DB
    DS --> FS
    FS --> DS
    DS --> FL
    FL --> DS
    DS --> DB
```

---

## ⚙️ Components

### 1. 🔵 UI (React with TypeScript)
- Accepts user input (website URL)
- Sends request to DataService
- Displays status and summary

### 2. 🟢 DataService (Spring Boot)
- Uses JPA with Postgres
- Tables: `user_table`, `web_scrape_table`
- Workflow:
  1. Stores incoming URL
  2. Calls FastAPI Scraper
  3. Updates DB with scraped content
  4. Sends scraped data to Flask Summarizer
  5. Updates DB with summarized text

### 3. 🟣 FastAPI Scraper
- Uses `requests` + `BeautifulSoup`
- Extracts relevant content from URLs
- Returns structured data

### 4. 🔴 Flask Summarizer
- Calls Gemini API (LLM)
- Uses custom prompt with scraped data
- Returns summarized output

### 5. 🗄️ PostgreSQL + pgAdmin
- Hosted via Docker
- Accessible via pgAdmin for inspection & admin tasks

---

## 🛠️ Development Tools

| Tool       | Purpose                            |
|------------|------------------------------------|
| Docker     | Containerize and run Postgres      |
| pgAdmin    | Visual DB management               |
| IntelliJ   | Spring Boot backend development    |
| PyCharm    | Python microservices (FastAPI/Flask) |
| venv       | Python virtual environments        |
| Gradle     | Spring Boot project build          |
| TypeScript | Typed development in React         |

---

## 🚀 Setup & Usage

### 🐳 Start Postgres in Docker
```bash
docker-compose up -d
```
> Make sure `docker-compose.yml` sets up Postgres and optionally pgAdmin.

### ⚙️ Backend Setup (Spring Boot)
1. Import project in IntelliJ
2. Run using Gradle
3. Configure `application.properties` with Postgres credentials

### 🧪 FastAPI Setup
```bash
cd fastapi-scraper
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
uvicorn main:app --reload
```

### 🧠 Flask Summarizer Setup
```bash
cd flask-summarizer-service
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python app.py
```

### 🖼️ Frontend Setup
```bash
cd react-app
npm install
npm start
```

---

## 📄 API Flow Summary

| Step | Service         | Description                            |
|------|------------------|----------------------------------------|
| 1    | UI               | User submits website URL               |
| 2    | DataService      | Saves URL to DB                        |
| 3    | FastAPI Scraper  | Scrapes and returns page content       |
| 4    | DataService      | Updates DB with scraped content        |
| 5    | Flask Summarizer | Summarizes scraped content via Gemini |
| 6    | DataService      | Updates DB with summarized text        |

---

## ✅ Status Indicators

| Status in DB         | Meaning                         |
|----------------------|----------------------------------|
| `PENDING_SCRAPE`     | Waiting for scraping             |
| `SCRAPE_COMPLETE`    | Scraping completed successfully  |
| `SUMMARIZED`         | Summary received                 |
| `ERROR_*`            | Process error                    |

---

Want to include deployment instructions next (like Dockerizing the whole stack or setting up CI/CD)? I can help draft those too. Let’s build this into something that’s not just functional—but polished and impressive.

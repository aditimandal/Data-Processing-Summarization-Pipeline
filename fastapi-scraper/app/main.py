from fastapi import FastAPI
from app.routers import scraper

app = FastAPI()

# Include the scraper routes
app.include_router(scraper.router)

@app.get("/")
def read_root():
    return {"message": "FastAPI Scraper Service is running"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="127.0.0.1", port=8001, reload=True)
from fastapi import APIRouter, HTTPException
import requests
from bs4 import BeautifulSoup

router = APIRouter()  # Creates an API router for handling routes


def scrape_website(url: str):
    try:
        # Send an HTTP GET request to fetch the website content
        response = requests.get(url, headers={"User-Agent": "Mozilla/5.0"})
        response.raise_for_status()  # Raise an error if the request fails (e.g., 404 or 500)

        # Parse the HTML content using BeautifulSoup
        soup = BeautifulSoup(response.text, "html.parser")

        # Extract the title of the page
        title = soup.title.string if soup.title else "No title found"

        # Extract all meta descriptions
        meta_description = ""
        meta_tag = soup.find("meta", attrs={"name": "description"})
        if meta_tag:
            meta_description = meta_tag.get("content", "")

        # Extract all text content from the page (removes script, style, and unnecessary elements)
        for script in soup(["script", "style"]):
            script.extract()  # Remove script and style elements

        text_content = soup.get_text(separator=" ", strip=True)  # Extract and clean text content

        # Return extracted data as JSON
        return {
            "title": title,
            "meta_description": meta_description,
            "content": text_content[:5000]  # Limiting content to 5000 characters to avoid excessive data
        }

    except requests.exceptions.RequestException as e:
        raise HTTPException(status_code=500, detail=f"Request error: {str(e)}")


@router.get("/scrape")
def scrape(url: str):
    return scrape_website(url)


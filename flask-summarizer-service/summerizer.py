import groq
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Get API key from environment variable
api_key = os.getenv('GROQ_API_KEY')
if not api_key:
    raise ValueError("GROQ_API_KEY environment variable is not set")

# Set up your Groq API key
client = groq.Client(api_key=api_key)

def summarize_text(text: str) :
    # """Summarizes text using Groq's LLaMA model and returns a structured text format."""
    """Summarizes text using Groq's LLaMA model and extracts the 5 most important points separately."""

    if not text.strip():
        return {"error": "Input text cannot be empty."}

    summary_prompt = f"""
    You are a professional content summarizer. Your job is to analyze the content provided and return a clean, human-readable summary using markdown format. Do **not** wrap anything in JSON or code blocks.

    **Instructions:**

    1. **Title (5–8 words):**
       - Start with a bold, concise title summarizing the main topic or theme of the content.

    2. **Detailed Structured Summary (Markdown Format):**
       - Use proper headings (`##`, `###`) for major sections or topics found in the content.
       - Under each heading, use bullet points to explain important facts, figures, updates, features, or takeaways.
       - Make sure all critical and useful information from the original content is captured.
       - If the content includes **key topics, categories, tutorials, examples, stats**, etc., structure them clearly.
       - If the content includes **important links**, include them in markdown link format: `[Link Text](URL)`

   
    3. **Concise Summary Section:**
       - At the end, add a section titled `### 🔍 Quick Summary` with 10–15 lines summarizing the most important insights and takeaways from the content.

    **What to Avoid:**
    - Do NOT return any JSON or object-based structure.
    - Do NOT include any code block formatting (like triple backticks ```).
    - Do NOT write "Here is the JSON" or "Output Format".

    Now, summarize the following content:

    {text}
    """






    points_prompt = f"""
            Extract the 5 most important key points from the following content:

            **Requirements:**
            - Provide exactly 5 points.
            - Ensure each point is a standalone, insightful takeaway.

            **Content:**
            {text}
        """


    summary_response = client.chat.completions.create(
        model="llama3-70b-8192",
        messages=[{"role": "system", "content": "Summarize the text with structured Markdown format."},
                  {"role": "user", "content": summary_prompt}],
        temperature=0.5,
        max_tokens=800
    )
    print("Summary API Raw Response:", summary_response)  # Debugging print



    # Get the 5 most important points
    points_response = client.chat.completions.create(
        model="llama3-70b-8192",
        messages=[{"role": "system", "content": "Extract the 5 most important points from the text."},
                  {"role": "user", "content": points_prompt}],
        temperature=0.5,
        max_tokens=300
    )

    print("Points API Raw Response:", points_response)  # Debugging print


    #  Extract text safely using dictionary keys
    summary_text = summary_response.choices[0].message.content.strip()
    points_text = points_response.choices[0].message.content.strip()

    # return summary_text # Returning separately
    return {
        "summary": summary_text,
        "important_points": points_text
    }




from flask import Flask, request, jsonify
from summerizer import summarize_text  # Import function directly


app = Flask(__name__)  # Initialize Flask app

@app.route('/summarize', methods=['POST'])
def summarize():
    """Receives JSON with scraped text and returns summarized text."""
    try:
        data = request.get_json()  # This ensures JSON is extracted properly
        print("Received JSON:", data)  # Debugging print

        if not data or "text" not in data:
            return jsonify({"error": "No content provided"}), 400

        content = data["text"]  # Extract "text"
        is_important_notes_needed = data.get("isImportantNotesNeeded", False)  # Default to False if not provided
        print("Extracted Content:", content)  # Debugging print
        print("Important Notes Needed:", is_important_notes_needed)  # Debugging print

        summary_data = summarize_text(content)   # Call the summarization function
        print("Summary Data:", summary_data)  # Debugging print

        if is_important_notes_needed:  # Now handling boolean value directly
            response = jsonify({
                "summary": summary_data["summary"],
                "important_points": summary_data["important_points"]
            })
        else:
            response = jsonify({
                "summary": summary_data["summary"]
            })

        print("Returning JSON:", response.get_json())  # Debugging print
        return response

    except Exception as e:
        print("Error occurred:", str(e))  # Print error for debugging
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5001, debug=True)  # Run Flask app
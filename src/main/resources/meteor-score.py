import nltk
from nltk.translate.meteor_score import meteor_score
import json

# Download WordNet for synonym recognition (one-time setup)
nltk.download('wordnet')


def read_json_file(file_path):
  """
  Reads a JSON file and returns a list of dictionaries containing source and target text.

  Args:
      file_path (str): Path to the JSON file.

  Returns:
      list: List of dictionaries containing "sourceLanguage", "targetLanguage", 
            "requestBody", and "responseBody" keys.
  """
  with open(file_path, 'r', encoding='utf-8') as file:
    data = json.load(file)
  return data


def extract_response_bodies(data, key):
  """
  Extracts all values stored under the "responseBody" key from a list of dictionaries.

  Args:
      data (list): List of dictionaries containing JSON data.

  Returns:
      list: List of strings containing the "responseBody" values.
  """
  response_bodies = []
  for item in data:
    response_bodies.append(item[key])
  return response_bodies

def compare_responses(api_responses, reference_responses):
  average_score = 0

  for i in range(len(api_responses)):
    translated = api_responses[i]
    reference = reference_responses[i]

    # Tokenize the strings (modify for your use case)
    reference_tokens = reference.split()
    translated_tokens = translated.split()

    # Calculate METEOR score for this pair (optional, modify for your use case)
    score = meteor_score([reference_tokens], translated_tokens)
    average_score += score
    print(f"METEOR Score for string {i+1}: {score:.4f}")
  
  average_score = float(average_score) / len(api_responses)
  return average_score

def main():
  api_translations_file_path = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/libre-responses.json"
  reference_translations_file_path = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/google-v2-responses.json"

  # Read JSON files
  api_translations_data = read_json_file(api_translations_file_path)
  reference_translations_data = read_json_file(reference_translations_file_path)

  # Extract response bodies
  api_responses = extract_response_bodies(api_translations_data, "translatedText")
  reference_responses = extract_response_bodies(reference_translations_data, "responseBody")
  print("Average score: " + str(compare_responses(api_responses, reference_responses)))

if __name__ == "__main__":
  main()

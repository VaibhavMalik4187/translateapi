import nltk
from nltk.translate.meteor_score import meteor_score
import json
import matplotlib.pyplot as plt
import numpy as np

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


def extract_keys(data, key):
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
  total_score = 0
  total_weight = 0
  average_score = 0
  scores = []
  skipped = 0

  for i in range(len(api_responses)):
    translated = api_responses[i]
    reference = reference_responses[i]

    # Check for None values before tokenization
    # if translated is None or reference is None:
    if translated is None:
      print(f"Skipping comparison for pair {i+1}: API response is None")
      skipped += 1
      continue
    elif reference is None:
      print(f"Skipping comparison for pair {i+1}: Reference response is None")
      skipped += 1
      continue


    # Tokenize the strings (modify for your use case)
    reference_tokens = reference.split()
    translated_tokens = translated.split()

    # Calculate METEOR score for this pair (optional, modify for your use case)
    score = meteor_score([reference_tokens], translated_tokens)
    length = len(translated_tokens)
    if score < 0.5:
        print("API translation: " + api_responses[i])
        print("Reference translation: " + reference_responses[i])
        print("-------------------------------------------------------------------")
    average_score += score
    print("Average score: " + str(float(average_score) / (i + 1 - skipped)))

    # Update weighted score and total weight
    total_score += score * length
    total_weight += length
    scores.append(total_score/total_weight)

    print(f"METEOR Score for string {i+1}: {score:.4f}")

  plot_meteor_score(scores)
  weighted_average_score = total_score / total_weight
  return weighted_average_score
  # average_score = float(average_score) / (len(api_responses) - skipped)
  # return average_score

def plot_latencies(api_responses_data, reference_responses_data):
    api_latencies = extract_keys(api_responses_data, "latency")
    reference_latencies = extract_keys(reference_responses_data, "latency")

    api_data = np.array(api_latencies)
    reference_data = np.array(reference_latencies)

    # Creates an array from 0 to length-1
    x_axis = np.arange(len(api_latencies))
    plt.plot(x_axis, api_data, label="AWS Translate API")
    plt.plot(x_axis, reference_data, label="Google Translate V2")

    # Add labels and title
    plt.xlabel("Text sample index in the json data")
    plt.ylabel("Latency in milliseconds")
    plt.title("Latency comparision for the AWS Translate and Google Translate V2 APIs")

    # Add legend
    plt.legend()

    # Show the plot
    plt.grid(True)  # Optional: add gridlines for better readability
    plt.show()

def plot_meteor_score(meteor_score_data):
    score_data = np.array(meteor_score_data)

    # Creates an array from 0 to length-1
    x_axis = np.arange(len(meteor_score_data))
    plt.plot(x_axis, score_data, label="Meteor score")

    # Add labels and title
    plt.xlabel("Text sample index in the json data")
    plt.ylabel("Meteor score")
    plt.title("Meteor scores of AWS translations using Google translations as reference")

    # Add legend
    plt.legend()

    # Show the plot
    plt.grid(True)  # Optional: add gridlines for better readability
    plt.show()

def main():
  # api_translations_file_path = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/libre-responses.json"
  api_translations_file_path = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/aws-responses.json"
  # api_translations_file_path = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/google-v2-responses-with-latency.json"
  reference_translations_file_path = "/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/google-v2-responses-with-latency.json"

  # Read JSON files
  api_translations_data = read_json_file(api_translations_file_path)
  reference_translations_data = read_json_file(reference_translations_file_path)

  # Extract response bodies
  api_responses = extract_keys(api_translations_data, "translatedText")
  reference_responses = extract_keys(reference_translations_data, "translatedText")
  print("Average score: " + str(compare_responses(api_responses, reference_responses)))

  plot_latencies(api_translations_data, reference_translations_data)

if __name__ == "__main__":
  main()

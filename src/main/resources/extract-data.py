import json

def extract_questions(input_file, output_file):
  """Extracts 'questions' values from a JSON file and saves them to a new JSON file.

  Args:
    input_file: The path to the input JSON file.
    output_file: The path to the output JSON file.
  """

  with open(input_file, 'r') as f:
    data = json.load(f)

  # questions = [item['question'] for item in data]
  text = [item['rohtext'] for item in data]

  with open(output_file, 'w') as f:
    json.dump(text, f, indent=2)

input_file = '/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/data.json'
output_file = '/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/german-text.json'

extract_questions(input_file, output_file)

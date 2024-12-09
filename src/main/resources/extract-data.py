import json
import csv

def write_to_csv(json_data, csv_file):
    with open(csv_file, 'w', newline='') as csvfile:
        fieldnames = ['Source Language', 'Target Language', 'Request Body', 'Response Body']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        for hit in json_data['hits']['hits']:
            source_language = hit['fields']['attributes.language'][0]
            target_language = hit['fields']['attributes.targetLanguage'][0]
            request_body = hit['fields']['attributes.requestBody'][0]
            if len(request_body) > 2:
                request_body = request_body[1:-1]
            responses = hit['fields']['attributes.responseBody']
            combined_response = ""
            for response_str in responses:
                response_json = json.loads(response_str)
                for i, translated_text in enumerate(response_json):
                    text = translated_text["translatedText"]
                    combined_response += text
                    if i < len(response_json) - 1:
                        combined_response += " "  # Add space only if it's not the last text

            writer.writerow({
                'Source Language': source_language,
                'Target Language': target_language,
                'Request Body': request_body,
                'Response Body': combined_response
            })


input_file = '/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/final-data.json'
output_file = '/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/filtered-data.csv'

with open(input_file, 'r') as f:
    json_data = json.load(f)
write_to_csv(json_data, output_file)

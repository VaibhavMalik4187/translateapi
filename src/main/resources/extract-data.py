import json
import csv

def write_to_csv(json_data, json_file):
    data = []

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
        data.append({
            "sourceLanguage": source_language,
            "targetLanguage": target_language,
            "requestBody": request_body,
            "responseBody": combined_response
        })

    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=4)


input_file = '/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/final-data.json'
output_file = '/Users/vaibhav.malik/Downloads/translateapi/src/main/resources/filtered-data.json'

with open(input_file, 'r') as f:
    json_data = json.load(f)
write_to_csv(json_data, output_file)

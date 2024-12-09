import nltk
from nltk.translate.meteor_score import meteor_score
# Download WordNet for synonym recognition (one-time setup)
nltk.download('wordnet')
def read_text_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        text = file.read()
        # Split the text by double newlines to get the paragraphs
        paragraphs = text.split("\n\n")  # Two newlines indicate a paragraph gap
        return paragraphs
file_pathV2 = "/content/V2ResultsSmallStrings.txt"
file_pathaws = "/content/awsResultsSmallStrings.txt"
paragraphsV2 = read_text_file(file_pathV2)
paragraphsaws = read_text_file(file_pathaws)
num_paragraphs = min(len(paragraphsV2), len(paragraphsaws))
for i in range(num_paragraphs):
    reference = paragraphsV2[i]
    translated = paragraphsaws[i]
    # Tokenize the strings
    reference_tokens = reference.split()
    translated_tokens = translated.split()
    # Calculate METEOR score for this pair of paragraphs
    score = meteor_score([reference_tokens], translated_tokens)
    print(f"METEOR Score for paragraph {i+1}: {score:.4f}")



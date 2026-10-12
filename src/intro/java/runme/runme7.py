import string
from collections import Counter
from pathlib import Path

text = Path("runme7.txt").read_text()

for ch in string.punctuation:
    text = text.replace(ch, " ")

counter = Counter(text.split())

# Here we see character as the most common word
print(counter.most_common(100))

for k, v in counter.items():
    if "character" in k.lower():
        print(k, v)

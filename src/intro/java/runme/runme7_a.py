import re
from collections import Counter
from urllib.request import Request, urlopen

URL = "https://docs.oracle.com/javase/specs/jls/se25/html/jls-3.html"


def fetch_page(url: str) -> str:
    request = Request(
        url,
        headers={"User-Agent": "Mozilla/5.0"},
    )

    with urlopen(request) as response:
        return response.read().decode("utf-8")


def html_to_text(html: str) -> str:
    # Remove script/style blocks.
    html = re.sub(
        r"<(?:script|style).*?>.*?</(?:script|style)>",
        " ",
        html,
        flags=re.DOTALL | re.IGNORECASE,
    )

    # Remove all remaining tags.
    text = re.sub(r"<[^>]+>", " ", html)

    # Decode the most common HTML entities.
    replacements = {
        "&nbsp;": " ",
        "&amp;": "&",
        "&lt;": "<",
        "&gt;": ">",
        "&quot;": '"',
        "&#39;": "'",
    }

    for old, new in replacements.items():
        text = text.replace(old, new)

    return text


def tokenize(text: str) -> list[str]:
    # Keep alphabetic words only.
    return re.findall(r"\b[a-zA-Z]+\b", text.lower())


def main() -> None:
    html = fetch_page(URL)
    text = html_to_text(html)
    words = tokenize(text)

    counts = Counter(words)

    print(f"Total words: {len(words)}")
    print("\nMost frequent words:")
    for word, count in counts.most_common(50):
        print(f"{word:20} {count}")

    # Very rough singular/plural candidates.
    print("\nPotential singular/plural pairs:")

    candidates = []

    for singular, singular_count in counts.items():
        plural = singular + "s"

        if plural in counts:
            total = singular_count + counts[plural]
            candidates.append(
                (total, singular, plural, singular_count, counts[plural])
            )

    for total, singular, plural, singular_count, plural_count in sorted(
        candidates, reverse=True
    )[:50]:
        print(
            f"{singular:20} {plural:20} "
            f"singular={singular_count:4} "
            f"plural={plural_count:4} "
            f"total={total:4}"
        )


if __name__ == "__main__":
    main()

import requests
from bs4 import BeautifulSoup


def fetch_html(url):
    try:
        response = requests.get(url)
        response.raise_for_status()  # Raises an error for bad responses (4xx or 5xx)
        return response.text  # Returns the HTML content
    except requests.exceptions.RequestException as e:
        print("Error fetching HTML:", e)
        return None

if __name__ == '__main__':
    html_content = fetch_html("https://www.gimy.ai/eps/196876-3-5.html")
    # soup = BeautifulSoup(html_content, 'html.parser')
    # div_element = soup.find("div", id="zanpiancms_player")
    html_content = html_content[html_content.find("https:\/\/v8.dious.cc"):]

    html_content = html_content[:html_content.find("\"")-1]
    html_content = html_content.replace("\\" ,"")
    print(html_content)
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
    html_content = fetch_html("https://gimy.ai/eps/281800-3-13.html")
    soup = BeautifulSoup(html_content, 'html.parser')
    # html_content = html_content[html_content.find("https:\/\/v8.dious.cc"):]
    #
    # html_content = html_content[:html_content.find("\"")-1]
    # html_content = html_content.replace("\\" ,"")
    # print(html_content)


    div_element = soup.find("div", class_="details-play-title")
    print(div_element)
    print(div_element.find('a').text + div_element.find('span').text)

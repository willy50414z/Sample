from bs4 import BeautifulSoup
import os
from util import request_util, ffmpeg_util


def download(url, dir):
    if url.find("gimy") > 0:
        html_content = request_util.fetch_html(url)

        # video_name
        soup = BeautifulSoup(html_content, 'html.parser')
        div_element = soup.find("div", class_="details-play-title")
        video_name = div_element.find('a').text + div_element.find('span').text + ".mp4"
        video_name = video_name.replace(" ", "")
        video_name = video_name.replace("-", "")
        video_name = video_name.replace(":", "")

        # m3ur_url
        m3ur_url = html_content[:html_content.rfind(".m3u8")]
        m3ur_url = m3ur_url[m3ur_url.rfind("\"") + 1:] + ".m3u8"
        m3ur_url = m3ur_url.replace("\\", "")
        # soup.find("div", class_="playerCnt")

        print(f"download url[{m3ur_url}] to [{os.path.join(dir, video_name)}]")
        ffmpeg_util.download_video_from_m3u8(m3ur_url,
                                             os.path.join(dir, video_name))
        # ffmpeg_util.download_video_from_m3u8("https://vip.lz-cdn2.com/20220319/88_2e8c99b2/1200k/hls/mixed.m3u8", os.path.join(dir, video_name))

from util import request_util, ffmpeg_util


def download(url, path):
    if url.find("gimy") > 0:
        m3ur_url = request_util.fetch_html(url)
        m3ur_url = m3ur_url[m3ur_url.find("https:\/\/v8.dious.cc"):]
        m3ur_url = m3ur_url[:m3ur_url.find("\"")]
        m3ur_url = m3ur_url.replace("\\", "")
        ffmpeg_util.download_video_from_m3u8(m3ur_url, path)

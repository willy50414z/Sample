from service import video_download_service
from util import youtube_util, ffmpeg_util

# Function to download a YouTube video
# <innertube.py> 檔 223列，將「ANDROID_MUSIC」改為「WEB」


# Example usage
if __name__ == "__main__":
    video_download_service.download(f"https://gimy.ai/eps/1479-2-1.html", f"C:/tmp/")
    # ffmpeg_util.download_video_from_m3u8(m3ur_url, os.path.join(dir, "xx"))
    # for i in range(1, 7):
    #     video_download_service.download(f"https://vip.lz-cdn2.com/20220319/88_2e8c99b2/index.m3u8", f"C:/tmp/百分之三第四季第{i}集.mp4")
    # for i in range(1, 3):
    #     video_download_service.download(f"https://gimy.ai/eps/131499-3-{i}.html", f"C:/tmp/百分之三第三季第{i}集.mp4")
    # for i in range(1, 7):
    #     video_download_service.download(f"https://gimy.ai/eps/140130-3-{i}.html", f"C:/tmp/百分之三第三季第{i}集.mp4")
    # for i in [8]:
    #     video_download_service.download(f"https://gimy.ai/eps/131499-3-{i}.html", f"C:/tmp/百分之三第三季第{i}集.mp4")
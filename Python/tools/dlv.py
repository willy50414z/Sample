import glob
from time import sleep

from service import video_download_service
import logging
from datetime import datetime
import shutil
import os

from util import youtube_util, ffmpeg_util

# Function to download a YouTube video
# <innertube.py> 檔 223列，將「ANDROID_MUSIC」改為「WEB」
dlv_root_dir = "/volume1/homes/Willy/video"

logger = logging.getLogger('my_logger')
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler(f'{dlv_root_dir}/log/app.log')
file_handler.setLevel(logging.DEBUG)
logger.addHandler(file_handler)


def get_date():
    now = datetime.now()
    return now.strftime('%Y%m%d %H:%M:%S')


def print_log(message):
    logger.info(f"{get_date()} - {message}")


def get_content(file):
    with open(file, 'r') as file:
        return file.read()


# Example usage
if __name__ == "__main__":
    print_log("start dvl")
    directory = dlv_root_dir + '/waitForDownload/'
    files = [f for f in os.listdir(directory) if os.path.isfile(os.path.join(directory, f))]
    if len(files) > 0:
        for file in files:
            dling_file_path = dlv_root_dir + '/downloading/' + file
            finish_file_path = dlv_root_dir + '/finish/' + file
            wait_dl_path = directory + file

            print_log(f"move {wait_dl_path} to {dling_file_path}")
            shutil.move(wait_dl_path, dling_file_path)
            content = get_content(dling_file_path)
            print_log(f"read url from [{dling_file_path}] file content[{content}]")
            video_download_service.download(content, f"{dlv_root_dir}/video")
            print_log(f"finish download, move [{dling_file_path}] to [{finish_file_path}]")
            shutil.move(dling_file_path, finish_file_path)
    print_log(f"finish download all files, wait 30 mins for next run")
    sleep(1800)
    # ffmpeg_util.download_video_from_m3u8(m3ur_url, os.path.join(dir, "xx"))
    # for i in range(1, 7):
    #     video_download_service.download(f"https://vip.lz-cdn2.com/20220319/88_2e8c99b2/index.m3u8", f"C:/tmp/百分之三第四季第{i}集.mp4")
    # for i in range(1, 3):
    #     video_download_service.download(f"https://gimy.ai/eps/131499-3-{i}.html", f"C:/tmp/百分之三第三季第{i}集.mp4")
    # for i in range(1, 7):
    #     video_download_service.download(f"https://gimy.ai/eps/140130-3-{i}.html", f"C:/tmp/百分之三第三季第{i}集.mp4")
    # for i in [8]:
    #     video_download_service.download(f"https://gimy.ai/eps/131499-3-{i}.html", f"C:/tmp/百分之三第三季第{i}集.mp4")

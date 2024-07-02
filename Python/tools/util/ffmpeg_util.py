import ffmpeg


def download_video_from_m3u8(m3u8_url, output_path):
    try:
        ffmpeg.input(m3u8_url).output(output_path).run(overwrite_output=True)
        print("Download successful!")
    except Exception as e:
        print(f"Error: {e}")


if __name__ == '__main__':
    download_video_from_m3u8("https://vip.lz-cdn2.com/20220319/88_2e8c99b2/index.m3u8", "C:/tmp/幽靈公主HD中字播放線路量子雲.mp4")



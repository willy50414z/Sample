import ffmpeg

def download_video_from_m3u8(m3u8_url, output_path):
    try:
        ffmpeg.input(m3u8_url).output(output_path).run(overwrite_output=True)
        print("Download successful!")
    except Exception as e:
        print(f"Error: {e}")

# Example usage
if __name__ == "__main__":
    m3u8_url = "https://v8.dious.cc/20221130/PQx4arn6/index.m3u"
    output_path = "C:/tmp/百分之三第二季第07集.mp4"
    download_video_from_m3u8(m3u8_url, output_path)

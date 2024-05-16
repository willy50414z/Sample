from pytube import YouTube
from pytube import Playlist


def download_video(url, output_dir):
    try:
        yt = YouTube(url)
        video = yt.streams.get_highest_resolution()
        video.download(output_dir)
        print("Download successful!")
    except Exception as e:
        print("Error:", e)


def download_playlist(url, output_dir):
    playlist = Playlist(url)
    print(f"start download youtube playlist, size[{len(playlist.video_urls)}]")

    download_video_idx = 1
    for url in playlist.video_urls:
        print(f"download video process {download_video_idx} / {len(playlist.video_urls)}")
        download_video(url, output_dir)

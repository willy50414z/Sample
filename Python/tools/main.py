import flask
from flask import request
from service import video_download_service

app = flask.Flask(__name__)

@app.route("/")
@app.route("/hello")
def hello():
	query_params = request.args
	video_download_service.download(query_params["url"], f"/var/services/homes/Willy/video/")
	return query_params["url"]

if __name__ == "__main__":
	app.run(host="0.0.0.0", port="5000", debug=False)
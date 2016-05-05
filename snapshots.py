# -*- coding: utf-8 -*-


# "ffmpeg -ss 00:02:06 -i test_video.mp4 -f image2 -y tes1.jpg"
# "ffmpeg -ss 75 -i test_video.mp4 -f image2 -y tes1.jpg"
# 视频截图命令
# 建议使用第二个命令，其中75表示截图秒数，正好和下面获得的该视频总秒数配合
# "ffprobe -v quiet -print_format json -show_format -show_streams test_video.mp4"
# 查看视频文件信息，并以json格式输出
# 其中 {"streams" : {"duration" : 该视频的总秒数 }}

import os
import sys
import json



if __name__ == "__main__":
    video_name = "test_video"
#    video_name = sys.argv[1]
    dirname = os.path.splitext(video_name)[0]

    if not os.path.exists(dirname):
        os.mkdir(dirname)

    print(json.loads(os.popen("ffprobe -v quiet -print_format json "
             + "-show_format -show_streams" +
             " test_video.mp4").read()))

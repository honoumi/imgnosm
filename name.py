#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys,os
#os.system(sys.argv[1])
def rename():
#    base_path="/Users/chengkai/Movies/clip"
    path=""
    raw_input(path)
    filelist=os.listdir(path)#该文件夹下所有的文件（包括文件夹）
    count=1
    for files in filelist:#遍历所有文件
        Olddir=os.path.join(path,files)#原来的文件路径
#        if os.path.isdir(Olddir):#如果是文件夹则跳过
 #           continue
        filename=os.path.splitext(files)[0]#文件名
        filetype=os.path.splitext(files)[1]#文件扩展名
        try:
            count = int(filename)
        except ValueError:
            pass
        Newdir=os.path.join(path,"s1e13-"+str(count/300)+"m"+str((count%300)/5)+"s_"+str(count%5)+filetype)#新的文件路径
        os.rename(Olddir,Newdir)#重命名

rename()

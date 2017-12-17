#encoding=utf8

import os,re,sys,shutil,tempfile
import xml.dom.minidom

def swapwholefilematchedpattern(regexpattern, tostring, filepath):
    '''
        从整个文件内容中正则匹配替换内容
    '''
    size = os.path.getsize(filepath)
    if size <= 0:
        return False

    oldfile = file(filepath, "r")
    newfile = tempfile.TemporaryFile("w+")

    c = oldfile.read(size)
    needCopy = False
    pattern = re.compile(regexpattern, re.M | re.S)
    matchobj = pattern.search(c)
    if None != matchobj and 0 != matchobj.start(0):
        newfile.write(matchobj.string[0:matchobj.start(0)])
        newfile.write(tostring)
        newfile.write(matchobj.string[matchobj.end(0):])
        needCopy = True

    oldfile.close()

    if needCopy:
        os.remove(filepath)
        oldfile = file(filepath, "w")
        newfile.seek(0)
        for l in newfile.readlines():
            oldfile.write(l)
        oldfile.close()

    newfile.close()

    return needCopy

def swapmatchedline(regexline, tolinestring, filepath):
    '''
        替换指定文件filepath中，完全匹配regexline正则式的一整行为tolinestring。
    '''
    oldfile = file(filepath, "r")
    newfile = tempfile.TemporaryFile("w+")

    needCopy = False
    pattern = re.compile(regexline)
    for l in oldfile.readlines():
        matchobj = pattern.match(l)
        if None == matchobj:
            newfile.write(l)
            continue
        if 0 != matchobj.start(0) or len(l) != matchobj.end(0):
            newfile.write(l)
            continue
        newfile.write(tolinestring)
        needCopy = True

    oldfile.close()

    if needCopy:
        os.remove(filepath)
        oldfile = file(filepath, "w")
        newfile.seek(0)
        for l in newfile.readlines():
            oldfile.write(l)
        oldfile.close()

    newfile.close()

def removematchedline(regexpattern, filepath):
    '''
        删除指定文件filepath中，匹配regexpattern正则式的一行。
    '''
    oldfile = file(filepath, "r")
    newfile = tempfile.TemporaryFile("w+")

    needCopy = False
    pattern = re.compile(regexpattern)
    for l in oldfile.readlines():
        matchobj = pattern.search(l)
        if None == matchobj:
            newfile.write(l)
        else:
            needCopy = True
        continue

    oldfile.close()

    if needCopy:
        os.remove(filepath)
        oldfile = file(filepath, "w")
        newfile.seek(0)
        for l in newfile.readlines():
            oldfile.write(l)
        oldfile.close()

    newfile.close()

def swapmatchedlineinfolderfiles(regexline, tolinestring, folderpath):
    '''
        批量对folderpath文件夹下的文件调用swapmatchedline()函数
    '''
    subs = os.listdir(folderpath)
    for item in subs:
        if 0 == cmp(item, ".svn"):
            continue
        itemfullpath = os.path.join(folderpath, item)
        if os.path.isdir(itemfullpath):
            swapmatchedlineinfolderfiles(regexline, tolinestring, itemfullpath)
            continue
        swapmatchedline(regexline, tolinestring, itemfullpath)

def copyfolder(srcpath, dstpath):
    '''
        覆盖拷贝文件夹，跳过.svn文件夹。
    '''
    if False == os.path.isdir(srcpath):
        if os.path.exists(dstpath):
            if os.path.isdir(dstpath):
                sys.stderr.write("**************** \"" + srcpath + "\" is a file, but \"" + dstpath + "\" is a folder, and it does exist.")
                return
            else:
                os.remove(dstpath)
        shutil.copyfile(srcpath, dstpath)
        return
    else:
        if os.path.exists(dstpath):
            if False == os.path.isdir(dstpath):
                sys.stderr.write("**************** \"" + srcpath + "\" is a folder, but \"" + dstpath + "\" is a file, and it does exist.")
                return
        else:
            os.makedirs(dstpath)
        subs = os.listdir(srcpath)
        for s in subs:
            if 0 == cmp(s, ".svn"):
                continue
            copyfolder(os.path.join(srcpath, s), os.path.join(dstpath, s))

def removeallfiles(path):
    '''
        删除所有文件，跳过.svn文件夹
    '''
    if False == os.path.isdir(path):
        try:
            os.remove(path)
        except:
            print "*** Delete file failed. " + path
        return

    sub = os.listdir(path)
    for item in sub:
        if 0 == cmp(item, ".svn"):
            continue
        removeallfiles(os.path.join(path, item))

def getAppVersion(projectPath):
    manifestXmlDom = xml.dom.minidom.parse(os.path.join(projectPath, "AndroidManifest.xml"))
    rootElement = manifestXmlDom.getElementsByTagName("manifest")[0]
    versionCode = rootElement.getAttribute("android:versionCode")
    versionName = rootElement.getAttribute("android:versionName")
    return (versionName, versionCode)

def getMinMaxApiLevel(projectPath):
    '''
        取得minSdkVersion和maxSdkVersion字符串元组
        如果未定义，则返回'-1'
    '''
    manifestXmlDom = xml.dom.minidom.parse(os.path.join(projectPath, "AndroidManifest.xml"))
    rootElement = manifestXmlDom.getElementsByTagName("uses-sdk")[0]
    minSdkVersion = rootElement.getAttribute("android:minSdkVersion")
    if 0==len(minSdkVersion):
        minSdkVersion='-1'
    maxSdkVersion = rootElement.getAttribute("android:maxSdkVersion")
    if 0==len(maxSdkVersion):
        maxSdkVersion='-1'
    return (minSdkVersion, maxSdkVersion)


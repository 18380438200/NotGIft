#encoding=utf8

import os,sys,string,buildutils

if __name__ == '__main__':
    if (len(sys.argv) < 2):
        sys.exit(1)

    prjPath = os.path.join(os.path.split(os.path.realpath(sys.argv[0]))[0], "..")
    pathAndFile = os.path.split(sys.argv[1])
    (appVersinaName, appVersionCode) = buildutils.getAppVersion(prjPath)
    os.rename(sys.argv[1], os.path.join(pathAndFile[0], string.replace(pathAndFile[1], 'release', 'v' + appVersionCode)))


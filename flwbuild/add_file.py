#encoding=utf8

import sys,os

if __name__ == '__main__':
    if (len(sys.argv) < 4):
        sys.exit(1)

    curWorkingPath = os.getcwd()
    curPythonScriptPath = os.path.split(os.path.realpath(sys.argv[0]))[0]

    os.chdir(curPythonScriptPath)

    os.execl(sys.argv[1], sys.argv[1], "a", sys.argv[2], sys.argv[3])

    os.chdir(curWorkingPath)


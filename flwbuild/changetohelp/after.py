#encoding=utf8

import sys,os

if __name__ == '__main__':
    os.system("svn revert -R \"" + os.getcwd() + "\"")


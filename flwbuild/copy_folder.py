#encoding=utf8

import sys,buildutils

if __name__ == '__main__':
    if (len(sys.argv) < 3):
        sys.exit(1)

    buildutils.copyfolder(sys.argv[1], sys.argv[2])
 

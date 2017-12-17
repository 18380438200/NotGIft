#encoding=utf8

import sys,os,string
sys.path.append(os.path.join(os.getcwd(), "flwbuild"))
import buildutils

def changeXmlJavaToHDVersion(path):
    if False == os.path.isdir(path):
        (filename, fileextname) = os.path.splitext(path)
        if 0 == cmp(".java", string.lower(fileextname)):
            buildutils.swapmatchedline("import\\s+cn\\.ahabox\\.feiliwu_help\\.R;\\s*\\r?\\n", "import cn.ahabox.feiliwu.R;\n", path)
            buildutils.swapmatchedline("import\\s+cn\\.ahabox\\.feiliwu_help\\.BuildConfig;\\s*\\r?\\n", "import cn.ahabox.feiliwu.BuildConfig;\n", path)
            buildutils.swapwholefilematchedpattern("\"content://cn\\.ahabox\\.feiliwu_help\"", "\"content://cn.ahabox.feiliwu\"", path)
            #buildutils.swapmatchedline("\\s*String\\s+FULL_PACKAGE_NAME\\s+=\\s+\"cn\\.ahabox\\.feiliwu_help\";\\s*\\r?\\n", "		String FULL_PACKAGE_NAME = \"cn.ahabox.feiliwu\";\n", path)
        #elif 0 == cmp(".xml", string.lower(fileextname)):
            #buildutils.swapwholefilematchedpattern("@string/app_name", "@string/app_name_help", path)
        return

    for item in os.listdir(path):
        if 0 == cmp(item, ".svn"):
            continue
        changeXmlJavaToHDVersion(os.path.join(path, item))


def changeVersionForHD():
    prjPath = os.path.join(os.path.split(os.path.realpath(sys.argv[0]))[0], "../..")
    (appVersinaName, appVersionCode) = buildutils.getAppVersion(prjPath)
    appVersionCode = str(string.atoi(appVersionCode) + 10000000)
    appVerCode = string.atoi(appVersionCode)
    appVerName = str(appVerCode / 10000000) + appVersinaName[1:]
    buildutils.swapmatchedline("\\s*android:versionCode\\s*=\\s*\"\\d+\"\\s*\\r?\\n", "    android:versionCode=\"" + appVersionCode + "\"\n", os.path.join(prjPath, "AndroidManifest.xml"))
    buildutils.swapmatchedline("\\s*android:versionName\\s*=\\s*\"\\d+\\.\\d+\\.\\d+\"\\s*>\\s*\\r?\\n", "    android:versionName=\"" + appVerName + "\">\n", os.path.join(prjPath, "AndroidManifest.xml"))

    #sync_ver.do_sync_ver(prjPath, False)


if __name__ == '__main__':
    delfilelist = file(os.path.join(os.path.split(os.path.realpath(sys.argv[0]))[0], "before_delete_files.txt"), "r")
    for l in delfilelist.readlines():
        try:
            os.remove(os.path.join(os.getcwd(), string.strip(l)))
        except:
            print "*** Delete file failed. " + os.path.join(os.getcwd(), string.strip(l))
    delfilelist.close()

    delfolderlist = file(os.path.join(os.path.split(os.path.realpath(sys.argv[0]))[0], "before_delete_folders.txt"), "r")
    for l in delfolderlist.readlines():
        buildutils.removeallfiles(os.path.join(os.getcwd(), string.strip(l)))
    delfolderlist.close()

    sub = os.listdir(os.getcwd() + "/app/src/main/res")
    for item in sub:
        if 0 == cmp(item, ".svn"):
            continue
        if len(item) <= 7:
            continue
        if 0 != item.find("values-"):
            continue
        if (True == item.startswith("values")):
            continue
        buildutils.removeallfiles(os.getcwd() + "/res/" + item)

    changeXmlJavaToHDVersion(os.path.join(os.getcwd(), "app"))
    #changeXmlJavaToHDVersion(os.path.join(os.getcwd(), "res"))

    buildutils.swapmatchedline("\\s*package\\s*=\\s*\"cn\\.ahabox\\.feiliwu_help\"\\s*\\r?\\n", "    package=\"cn.ahabox.feiliwu\"\n", os.path.join(os.getcwd(), "app/src/main/AndroidManifest.xml"))
    buildutils.swapmatchedline("\\s*android\:authorities\\s*=\\s*\"cn\\.ahabox\\.feiliwu_help\"\\s*\\r?\\n", "            android:authorities=\"cn.ahabox.feiliwu\"\n", os.path.join(os.getcwd(), "app/src/main/AndroidManifest.xml"))
    #changeVersionForHD()
    buildutils.copyfolder(os.path.join(os.path.split(os.path.realpath(sys.argv[0]))[0], "changefiles"), os.getcwd())

    buildutils.swapmatchedline("\\s*android\:name\\s*=\\s*\"cn\\.ahabox\\.feiliwu_help\"\\s*\\r?\\n", "            android:name=\"cn.ahabox.feiliwu\"\n", os.path.join(os.getcwd(), "app/src/main/AndroidManifest.xml"))
    buildutils.swapmatchedline("\\s*android\:value\\s*=\\s*\"574cfe66e0f55aeaf1001ad8\"\\s/>\\r?\\n", "            android:value=\"5684bf39e0f55adc5f000fc1\" />\n", os.path.join(os.getcwd(), "app/src/main/AndroidManifest.xml"))
    buildutils.swapmatchedline("\\s*android\:value\\s*=\\s*\"1e5ef1463f27f9aacd0daa87ea4227cf\"\\s>\\r?\\n", "            android:value=\"5813f3c9c56473267d732ed8a9bbcb97\" >\n", os.path.join(os.getcwd(), "app/src/main/AndroidManifest.xml"))
    #buildutils.swapwholefilematchedpattern("File\\s+Feedback", "File Feedback2", "src/DrawerHelper.java")
    buildutils.swapwholefilematchedpattern("cn.ahabox.feiliwu_help", "cn.ahabox.feiliwu", "app/build.gradle")
    buildutils.swapwholefilematchedpattern("@string/app_name_help", "@string/app_name", "app/src/main/AndroidManifest.xml")

    #微信支付参数替换
    buildutils.swapwholefilematchedpattern("wx80c95ce838ce3ff7", "wx3336b44d23b568ae", "app/src/main/AndroidManifest.xml")
    buildutils.swapwholefilematchedpattern("wx80c95ce838ce3ff7", "wx3336b44d23b568ae", "app/src/main/java/cn/ahabox/config/Config.java")
    #服务器地址修改
    buildutils.swapwholefilematchedpattern("http://demo.feiliwu.com.cn/app_api/", "http://www.feiliwu.com.cn/app_api/", "app/src/main/java/cn/ahabox/interfaces/Api.java")
    #buildutils.swapwholefilematchedpattern("http://demo.feiliwu.com.cn", "http://www.feiliwu.com.cn", "app/src/main/java/cn/ahabox/activity/WebViewActivity.java")
    #buildutils.swapwholefilematchedpattern("<!--\\s+<uses-permission android:name=\"com.android.vending.BILLING\"\\s+/>\\s+-->", "<uses-permission android:name=\"com.android.vending.BILLING\" />", "app/src/main/AndroidManifest.xml")
    #buildutils.swapwholefilematchedpattern("FILEMANAGER_HD_PRODUCT_ID\\s+=\\s+10", "FILEMANAGER_HD_PRODUCT_ID = 11", "src/com/cm/depends/CMInfocCommon.java")

    
    
    
    

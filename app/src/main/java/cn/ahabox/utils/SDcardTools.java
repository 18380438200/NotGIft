package cn.ahabox.utils;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ahabox.config.Config;

/**
 * Created by libo on 2015/11/24.
 *
 * 文件操作工具类
 */
public class SDcardTools {

	public static boolean isHaveSDcard(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 *
	 * @param data
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFileToSDcard(byte[] data,String fileName){
		String filePath = getSDPath();
		File dir = new File(filePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(filePath+"/"+fileName);
		try {
			if(!file.exists()){
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(data);
				bos.flush();
				bos.close();
				fos.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String getSDPath(){
		String sdDir=null;
		if(isHaveSDcard()){
			sdDir = Config.ROOT;
		}
		return sdDir;
	}

	public static List<String> getFileFormSDcard(File dir,String type){
		List<String> listFilesName = new ArrayList();
		if(isHaveSDcard() && dir != null){
			File[] files = dir.listFiles();
			if(files !=null){
				for(int i=0; i<files.length; i++){
					if(files[i].getName().indexOf(".")>=0){
						String filesResult = files[i].getName()
								.substring(files[i].getName().indexOf("."));
						if(filesResult.toLowerCase().equals(type.toLowerCase())){
							listFilesName.add(files[i].getName());
						}

					}
				}
			}
		}
		return listFilesName;
	}

	/*** 获取文件夹总大小 ***/
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 保存将数据换行保存
	 * @param record
	 * @param fileName
	 */
	public static void saveStrArray(String record,String fileName) {
		//换行符
		String lineFeed = "\r\n";
		File file = new File(Config.ROOT + fileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		//写入错误日志
		FileWriter writer = null;
		try {
			//如果文件中存在这个关键字，就不用再存入文件
			List strs = getStrArray(fileName);
			for(int i=0;i<strs.size();i++){
				if(strs.get(i).equals(record))
					return;
			}
			writer = new FileWriter(file,true);
			writer.write(record + lineFeed);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != writer){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 向文件里缓存页面数据
	 * @param json
	 * @param fileName
	 */
	public static void noNetWorkCache(String json,String fileName) {
		File file = new File(Config.ROOT + "cache/" + fileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		//写入错误日志
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(json);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != writer){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 获取缓存数据
	 */
	public static String getFirstPageData(String fileName){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(Config.ROOT + "cache/" + fileName));
			String line;
			while((line = bufferedReader.readLine()) != null){
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 从文本文件中截取每个字符串
	 * @return
	 */
	public static List<String> getStrArray(String fileName){
		List<String> list = new ArrayList<>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(Config.ROOT+fileName));
			String line;
			while((line = bufferedReader.readLine()) != null){
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void delFile(File file){
		if(file.isFile()){
			file.delete();
		}else if(file.isDirectory()){
			File file1s[] = file.listFiles();
			for(int i=0;i<file1s.length;i++){
				//用迭代法删除
				delFile(file1s[i]);
			}
			file.delete();
		}
	}

}

/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.file;

import com.github.dr.webapi.util.log.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
//Java

public class FileUtil {

	private File file;

	private String filepath;

	public FileUtil(File file){
		this.file = file;
		this.filepath = file.getPath();
		if(!file.exists()) {
			file.mkdirs();
		}
	}

	public FileUtil(String filepath){
		filepath = filepath;
		file = new File(filepath);
	}

	private FileUtil(File file, String filepath){
		file = file;
		filepath = filepath;
	}

	public static FileUtil File() {
		return File(null);
	}

	public static FileUtil File(String tofile) {
		File file;
		String filepath = null;
		String to = tofile;
		if (null!=tofile) {
			final String pth = "/";
			if(!pth.equals(String.valueOf(tofile.charAt(0)))) {
				to = "/" + tofile;
			}
		}
		try {
			File directory = new File("");
			if (null==tofile) {
				file = new File(directory.getCanonicalPath());
			} else {
				filepath=directory.getCanonicalPath()+to;
				file = new File(filepath);
			}
		} catch (Exception e) {

			if (null==tofile) {
				file = new File(System.getProperty("user.dir"));
			} else {
				filepath=System.getProperty("user.dir")+to;
				file = new File(filepath);
			}
		}
		return new FileUtil(file,filepath);
	}

	public boolean exists() {
		return (file.exists());
	}

	public File getFile() {
		return file;
	}

	public String getPath() {
		return filepath;
	}

	public FileUtil toPath(String filename) {
		String path = filepath+"/"+filename;
		filepath = path;
		file = new File(path);
		return this;
	}

	public List<File> getFileList() {
		File[] array = file.listFiles();
		List<File> fileList = new ArrayList<File>();
		for(int i=0;i<array.length;i++){
			if(!array[i].isDirectory()) {
				if(array[i].isFile()) {
					fileList.add(array[i]);
				}
			}
		}
		return fileList;
	}

	/**
	 *
	 * @param log
	 * @param cover 是否尾部写入
	 */
	public void writeFile(Object log, boolean cover) {
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.error("Mk file",e);
			}
		}
		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file,cover), "UTF-8")) {
			osw.write(log.toString());
			osw.flush();
		} catch (Exception e) {
			Log.error("writeByteFile",e);
		}
	}

	public OutputStream writeByteOutputStream(boolean cover) throws Exception {
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!file.exists()) {
			file.createNewFile();
		}
		return new FileOutputStream(file,cover);
	}

	public FileInputStream getInputsStream() {
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			return new FileInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStreamReader readInputsStream() {
		try {
			return new InputStreamReader(new FileInputStream(file), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object readFileData(boolean list) {
		try {
			return readFileData(list,new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object readFileData(boolean list, InputStreamReader isr) {
		try {
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			if(list){
				List<String> FileContent = new ArrayList<String>();
				while ((line = br.readLine()) != null) {
					FileContent.add(line);
				}
				return FileContent;
			} else {
				String FileContent = "";
				while ((line = br.readLine()) != null) {
					FileContent += line;
					FileContent += "\r\n";
				}
				return FileContent;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
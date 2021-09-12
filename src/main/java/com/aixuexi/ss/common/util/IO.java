package com.aixuexi.ss.common.util;

import java.io.*;

public class IO {

	public static String read(String dir, String name) throws Exception {
		File f = new File(dir, name);
		if (!f.exists()) {
			throw new Exception("Could not find the file " + name + " in " + dir);
		}

		final StringBuilder sb = new StringBuilder();
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		in.close();

		return sb.toString();
	}

	public static String read(String name) throws Exception {
		File f = new File(name);
		if (!f.exists()) {
			throw new Exception("Could not find the file " + name);
		}

		final StringBuilder sb = new StringBuilder();
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		in.close();

		return sb.toString();
	}
	
	public static String readFile(File f) {
        final StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try {
            {
                if (!f.exists()) {
                    f.createNewFile();
                }
            }
            reader = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        return content.toString();
    }

	public static void write(String dir, String content) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir))));
			writer.write(content);
			writer.flush();
		} catch (FileNotFoundException e) {
			LOG.log(e);
		} catch (IOException e) {
			LOG.log(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				LOG.log(e);
			}
		}
	}
	public void copy(final String oldPath,String newPath){
		try { 
			(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
			File a=new File(oldPath); 
			String[] file=a.list(); 
			File temp=null; 
			for (int i = 0; i < file.length; i++) { 
			if(oldPath.endsWith(File.separator)){ 
			temp=new File(oldPath+file[i]); 
			} 
			else{ 
			temp=new File(oldPath+File.separator+file[i]); 
			} 

			if(temp.isFile()){ 
			FileInputStream input = new FileInputStream(temp); 
			FileOutputStream output = new FileOutputStream(newPath + "/" + 
			(temp.getName()).toString()); 
			byte[] b = new byte[1024 * 5]; 
			int len; 
			while ( (len = input.read(b)) != -1) { 
			output.write(b, 0, len); 
			} 
			output.flush(); 
			output.close(); 
			input.close(); 
			} 
			if(temp.isDirectory()){//如果是子文件夹 
			copy(oldPath+"/"+file[i],newPath+"/"+file[i]); 
			} 
			} 
			} 
			catch (Exception e) { 
			System.out.println("复制整个文件夹内容操作出错"); 
			e.printStackTrace(); 

			} 	
	}
	/**
	 * @desp 删除指定目录下的所有文件
	 * @param path为路径，如果path为一个文件名称，也可以删除
	 */
	public static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	/*
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    */
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	}  
}

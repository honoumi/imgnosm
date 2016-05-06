package imgnosm.imghash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoragePool {
	
	public static class ImgHashNode	{
		String hash;
		String imgName;
		
		public ImgHashNode(String hash, String imgName)	{
			this.hash = hash;
			this.imgName = imgName;
		}
		
		public String getHash() { 
			return this.hash;
		}
		
		public String getImgName() { 
			return this.imgName; 
		}
	}
	
	public static List<ImgHashNode> pool = new ArrayList<ImgHashNode>();
	
	public static void init()	{
		String path = System.getProperty("user.dir") + "\\img\\";
		
		List<File> list = readAllFile(path);
		
		for (File file : list) {
			String filepath = file.getAbsolutePath();
			String filename = getFileNameNoEx(file.getName());

			String hash = Fingerprint.getFingerprintPhash(filepath);

			pool.add(new ImgHashNode(hash, filename));
		}
	}
	
	public static void readAllFile(String filePath, List<File> list) {
		File f = null;
		f = new File(filePath);
		File[] files = f.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				readAllFile(file.getAbsolutePath(), list);
			} else {
				list.add(file);
			}
		}
	}
	
	public static List<File> readAllFile(String path)	{
		List<File> list = new ArrayList<File>();
		readAllFile(path, list);
		return list;
	}

	public static String searchAllFile(String filePath, String hash) {
		List<File> list = new ArrayList<File>();
		List<String> resultList = new ArrayList<String>();
		readAllFile(filePath, list);

		for (File file : list) {
			String file_path = file.getAbsolutePath();
			String filename = getFileNameNoEx(file.getName());
			if (Imghash.hammingDistance(hash,
					Fingerprint.getFingerprintPhash(file_path)) < 4)
				return filename;
		}

		return "null";
	}

	/*
	 * 获取文件扩展名
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/*
	 * 获取不带扩展名的文件名
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}
	
	public static String search(String hash)	
	{
		for(ImgHashNode ni : pool)	{
			if(hash.equals(ni.getHash()))
				return ni.getImgName();
		}
		
		return null;
	}
}
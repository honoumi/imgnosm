package imgnosm.imghash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.Jedis;

public class StoragePool {
	
	

	public static class ImgHashNode {
		String hash;
		String imgName;

		public ImgHashNode(String hash, String imgName) {
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
	public static Jedis jedis;

	public static void init() {
		String path = System.getProperty("user.dir") + "\\img\\";

		List<File> list = readAllFile(path);

		for (File file : list) {
			String filepath = file.getAbsolutePath();
			String filename = getFileNameNoEx(file.getName());

			String hash = Fingerprint.getFingerprintPhash(filepath);

			pool.add(new ImgHashNode(hash, filename));
		}

		jedis = new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		
		
		
		for(ImgHashNode ni : pool)	{
//			jedis.lpush("test-pool", ni.getHash());
//			jedis.set(ni.getHash(), ni.getImgName());
		}
		
		// 连接本地的 Redis 服务
		// 存储数据到列表中

		List<String> templist = jedis.lrange("tutorial-list", 0, 5);
		
		for (int i = 0; i < templist.size(); i++) {
			System.out.println("Stored string in redis:: " + templist.get(i));
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

	public static List<File> readAllFile(String path) {
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

	public static String search(String hash) {
		for (ImgHashNode ni : pool) {
			if (Imghash.hammingDistance(hash,
					ni.getHash()) < 4)
				return ni.getImgName();
		}

		return null;
	}
}
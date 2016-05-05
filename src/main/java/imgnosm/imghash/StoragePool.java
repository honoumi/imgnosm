package imgnosm.imghash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoragePool {
	

	public static void readAllFile(String filePath, String hash, List<File> list) {
		File f = null;
		f = new File(filePath);
		File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
		for (File file : files) {
			if (file.isDirectory()) {
				// 如何当前路劲是文件夹，则循环读取这个文件夹下的所有文件
				readAllFile(file.getAbsolutePath(), hash, list);
			} else {
				list.add(file);
			}
		}
	}

	public static String searchAllFile(String filePath, String hash) {
		List<File> list = new ArrayList<File>();
		List<String> resultList = new ArrayList<String>();
		readAllFile(filePath, hash, list);

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
}
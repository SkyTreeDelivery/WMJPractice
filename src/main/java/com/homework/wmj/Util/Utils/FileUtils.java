package com.homework.wmj.Util.Utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class FileUtils {
	/**
	 * 根据url拿取file
	 * 
	 * @param url
	 * @param suffix
	 *            文件后缀名
	 */
	public static File createFileByUrl(String url, String suffix) {
		byte[] byteFile = getImageFromNetByUrl(url);
		if (byteFile != null) {
			File file = getFileFromBytes(byteFile, suffix);
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	private static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	// 创建临时文件
	private static File getFileFromBytes(byte[] b, String suffix) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = File.createTempFile("pattern", "." + suffix);
			System.out.println("临时文件位置：" + file.getCanonicalPath());
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static MultipartFile createImg(String url) {
		try {
			// File转换成MutipartFile
			File file = FileUtils.createFileByUrl(url, "jpg");
			FileInputStream inputStream = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
			return multipartFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static MultipartFile fileToMultipart(String filePath) {
		try {
			// File转换成MutipartFile
			File file = new File(filePath);
			if(!file.exists()){
				try {
					file.createNewFile();
				}catch (Exception e){
					return null;
				}
			}
			FileInputStream inputStream = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile(file.getName(), "png", "image/png", inputStream);
			return multipartFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  返回绝对路径
	 * @param multipartFile
	 * @param saveDir
	 * @return
	 * @throws IOException
	 */
	public static String saveFile(MultipartFile multipartFile, String saveDir) throws IOException {
		File saveFile = new File(saveDir);
		if(!saveFile.exists()){
			saveFile.mkdirs();
		}
		String oriFileName = multipartFile.getOriginalFilename();
		String savePath = saveDir + UUID.randomUUID() + "_" + oriFileName;
		multipartFile.transferTo(new File(savePath));
		return savePath;
	}

	public static String saveFile(InputStream inputStream, String saveDir, String oriFileName) throws IOException {
		File saveFile = new File(saveDir);
		if(!saveFile.exists()){
			saveFile.mkdir();
		}
		String savePath = saveDir + UUID.randomUUID() + "_" + oriFileName;
		byte[] bytes = inputStream2Bytes(inputStream);
		saveFile(bytes,savePath);
		return savePath;
	}

	public static byte[] inputStream2Bytes(InputStream inputStream) throws IOException {
		byte[] bytes = new byte[inputStream.available()];
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		int read = bufferedInputStream.read(bytes);
		if(read != inputStream.available()){
			throw new IOException("文件可读取，但读取结果不正确");
		}
		return bytes;
	}

	public static String saveFile(byte[] data, String savePath) throws IOException {
		File saveFile = new File(savePath);
		OutputStream outputStream = new FileOutputStream(saveFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		bufferedOutputStream.write(data,0,data.length);
		return savePath;
	}

	public static boolean deleteFile(String path){
		File file = new File(path);
		if(file.isFile()){
			return file.delete();
		}
		return false;
	}


}

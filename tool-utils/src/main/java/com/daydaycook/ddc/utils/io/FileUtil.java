package com.daydaycook.ddc.utils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 字符文件IO工具类
 * 
 * @author guannan.shang
 */
public abstract class FileUtil {

	/** 默认编码 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	protected String filePath;
	protected File file;
	protected BufferedWriter writer;
	protected BufferedReader reader;
	protected FileInputStream in;
	protected String charset;
	protected boolean append;

	/**
	 * 获取一个对指定文件操作的工具类对象,编码是UTF-8<br>
	 * 
	 * @param filePath
	 *            指定文件路径
	 * @return
	 */
	public static FileUtil getInstance(String filePath) {
		return getInstance(filePath, null, false);
	}

	/**
	 * 获取一个对指定文件操作的工具类对象<br>
	 * 
	 * @param filePath
	 *            指定文件路径
	 * @param charset
	 *            编码 默认为UTF-8
	 * @return
	 */
	public static FileUtil getInstance(String filePath, String charset) {
		return getInstance(filePath, charset, false);
	}

	/**
	 * 获取一个对指定文件操作的工具类对象<br>
	 * 
	 * @param filePath
	 *            指定文件路径
	 * @param charset
	 *            编码 默认为UTF-8
	 * @param append
	 *            是否想文件末尾追加
	 * @return
	 */
	public static FileUtil getInstance(String filePath, boolean append) {
		return getInstance(filePath, null, append);
	}

	/**
	 * 获取一个对指定文件操作的工具类对象<br>
	 * 
	 * @param filePath
	 *            指定文件路径
	 * @param charset
	 *            编码 默认为UTF-8
	 * @param append
	 *            是否想文件末尾追加
	 * @return
	 */
	public static FileUtil getInstance(String filePath, String charset, boolean append) {
		if (charset == null)
			charset = DEFAULT_CHARSET;
		try {
			return new FileUtilImpl(filePath, charset, append);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向文件写入字符串,默认不关闭流
	 * 
	 * @param data
	 */
	public abstract void write(String data) throws IOException;

	/**
	 * 向文件写入字符串
	 * 
	 * @param data
	 * @param close
	 *            关闭流
	 */
	public void write(String data, boolean close) throws IOException {
		write(data);
		if (close)
			close();
	}

	/**
	 * 将文件读取成为字符串
	 * 
	 * @return
	 */
	public abstract String readAll();

	/**
	 * 读取一行字符串
	 * 
	 * @return
	 */
	public abstract String readLine();

	public File getFile() {
		return file;
	}

	public String getFilePath() {
		return filePath;
	}

	FileUtil(){
	}

	FileUtil(String filePath, String charset, boolean append){
		this.filePath = filePath;
		this.file = new File(filePath);
		this.charset = charset;
		this.append = append;
	}

	/** 关闭输入流,输出流 */
	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void initWriter() throws IOException {
		createFile();
		if (writer == null) {
			FileOutputStream fos = new FileOutputStream(file, append);
			OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
			writer = new BufferedWriter(osw);
		}
	}

	protected void initReader() throws IOException {
		if (in == null) {
			in = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(in, charset);
			if (reader == null)
				reader = new BufferedReader(isr);
		}
	}

	private void createFile() throws IOException {
		if (!file.exists()) {
			new File(file.getParent()).mkdirs();
			if (!file.createNewFile())
				throw new RuntimeException("can't create file");
		}
	}
}

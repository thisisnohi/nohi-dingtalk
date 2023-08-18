package nohi.demo.mp.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileUtils {
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public static void clearPath(String path){
		if (StringUtils.isNotBlank( path )) {
			try {
				File file = new File( path );
				if (file.exists() && file.isDirectory()) {
					deleteDir(file);
				}
			}catch (Exception e){
				log.info( "清除目录失败!" );
				log.error( e.getMessage(),e );
			}
		}
	}

	/**
	 * 判断路径是否存在
	 * @param path
	 * @return
	 */
	public static boolean exists(String path){
		if (StringUtils.isBlank(path)) {

			return false;
		}
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 创建目录
	 * @param path
	 * @return
	 */
	public static void createDir(String path) throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()){
				throw new Exception("创建文件目录[" + path + "]失败!");
			}
		}
	}

	/**
	 * 保存输入流中的数据至文件，指定文件名
	 * 	   默认UTF-8
	 * @param docPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String storeMessage(InputStream is,String docPath,String fileName) throws Exception{
		//创建目录
		createDir(docPath);

		File file = new File(docPath + File.separator + fileName);
		if (file.exists()) {
			throw new Exception("文件已经存在");
		}
		if (null == is) {
			throw new Exception("输入流为空!");
		}
		FileOutputStream fos = null ;
		try {
            fos = new FileOutputStream(file); //直接写文件全路径

			byte[] bytes = new byte[2048];
			int length = -1;
			while ((length = is.read(bytes)) > -1){
                fos.write(bytes,0,length);
			}
            fos.flush();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		//返回文件路径和文件名
		return docPath + File.separator + fileName;
	}

	/**
	 * 保存信息至文件，指定文件名
	 * 	   默认UTF-8
	 * @param message
	 * @param docPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String storeMessage(String message,String docPath,String fileName) throws Exception {
		return storeMessage(message,"UTF-8", docPath, fileName,false);
	}
		/**
		 * 保存信息至文件，指定文件名
		 * 	   默认UTF-8
		 * @param message
		 * @param docPath
		 * @param fileName
		 * @return
		 * @throws Exception
		 */
	public static String storeMessage(String message,String docPath,String fileName,boolean checkExists) throws Exception{
		return storeMessage(message,"UTF-8", docPath, fileName,checkExists);
	}

	/**
	 * 保存信息至文件，指定文件名、字符集
	 * @param message
	 * @param encode
	 * @param docPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String storeMessage(String message,String encode,String docPath,String fileName,boolean checkExists) throws Exception{
		//创建目录
		createDir(docPath);

		File file = new File(docPath + File.separator + fileName);
		if (file.exists() && (file.isDirectory() || checkExists )) { //如果文件存在: 文件是目录/需要检查文件是否存在,则报错
			throw new Exception("文件已经存在");
		}

		if (null == message) {
			message = "";
		}
		OutputStreamWriter writer = null;
		FileOutputStream fos = null ;
		try {
			fos = new FileOutputStream(file); //直接写文件全路径
			writer = new OutputStreamWriter(fos,encode);
			writer.write(message);
			writer.flush();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				if (null != fos) {
					fos.close();
				}
				if (null != writer) {
					writer.close();
				}
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

		return docPath + File.separator + fileName;
	}

	/**
	 * 保存信息至文件，指定文件名、字符集
	 * @param message
	 * @param encode
	 * @param docPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String storeMessageAppend(String message,String encode,String docPath,String fileName) throws Exception{
		//创建目录
		createDir(docPath);

		File file = new File(docPath + File.separator + fileName);
		if (file.exists() && file.isDirectory()) { //如果文件存在: 文件是目录/需要检查文件是否存在,则报错
			throw new Exception("文件已经存在，且是文件目录");
		}

		if (null == message) {
			message = "";
		}
		OutputStreamWriter writer = null;
		FileOutputStream fos = null ;
		try {
			fos = new FileOutputStream(file,true); //直接写文件全路径
			writer = new OutputStreamWriter(fos,encode);
			writer.write(message);
			writer.flush();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				if (null != fos) {
					fos.close();
				}
				if (null != writer) {
					writer.close();
				}
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

		return docPath + File.separator + fileName;
	}

    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

	/**
	 * 从输入流中获取数据
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	public static String readStringfromStream(InputStream inStream) throws Exception{
		byte[] buffer = readStream(inStream);
		if (null != buffer) {
			return new String(buffer,"UTF-8");
		}
		return "";
	}

	/**
	 * 从输入流中获取数据
	 */
	public static String readStringfromPath(String path) throws Exception{
		return readStringfromStream(new FileInputStream( path ));
	}


	/**
	 * 复制文件到目标路径
	 * @param sourceFile
	 * @param targetFile
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(String sourcePath,String targetPath) throws Exception{
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourcePath));
			bos = new BufferedOutputStream(new FileOutputStream(targetPath));
			int len = 0;
			byte[] bs = new byte[1048567];
			while((len = bis.read(bs)) != -1){
				bos.write(bs, 0, len);
			}
			bos.flush();
			return true;
		} catch (Exception e) {
			throw e;
		}finally {
			try {
				bis.close();
			} catch (IOException e) {
				if (bis != null) {
					bis = null;
				}
			}
			try {
				bos.close();
			} catch (IOException e) {
				if (bos != null) {
					bos = null;
				}
			}
		}
	}
}

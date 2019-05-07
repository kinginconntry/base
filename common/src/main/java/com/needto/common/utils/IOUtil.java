/**   
 * @Title: IOUtil.java 
 * @Package: com.ewide.cloud.ewideframe.utils
 * @author Administrator  
 * @date 2017年3月16日 下午4:36:45 
 */
package com.needto.common.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Administrator
 * @Description
 */
public class IOUtil {

	public static void close(Closeable... ios) {
		for (Closeable io : ios) {
			if (null != io) {
				try {
					io.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			io = null;
		}
	}

}

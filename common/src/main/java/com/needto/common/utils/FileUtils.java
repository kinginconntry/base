package com.needto.common.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 文件处理工具
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 是否windows系统
     */
    public static boolean isWinOS() {
        boolean isWinOS = false;
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String sharpOsName = osName.replaceAll("windows", "{windows}").replaceAll("^win([^a-z])", "{windows}$1")
                    .replaceAll("([^a-z])win([^a-z])", "$1{windows}$2");
            isWinOS = sharpOsName.contains("{windows}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isWinOS;
    }

    /**
     * 是否为绝对地址
     * @param fileName
     * @return
     */
    public static boolean isAbsFile(String fileName) {
        if (isWinOS()) {
            // windows 操作系统时，绝对地址形如  c:\descktop
            return fileName.contains(":") || fileName.startsWith("\\");
        } else {
            // mac or linux
            return fileName.startsWith("/");
        }
    }

    /**
     * 将用户目录下地址~/xxx 转换为绝对地址
     * 只针对 linux 系统
     *
     * @param path
     * @return
     */
    public static String transferAbsPath(String path) {
        String homeDir = System.getProperties().getProperty("user.home");
        return StringUtils.replace(path, "~", homeDir);
    }

    /**
     * 获取文件流，支持网络链接，liunx，window文件
     * @param filepath
     * @return
     * @throws IOException
     */
    public static InputStream getFileStream(String filepath) throws IOException {
        if (filepath == null) {
            throw new IllegalArgumentException("fileName should not be null!");
        }

        if (ValidateUtils.isUrl(filepath)) {
            // 网络地址
            return HttpUtil.getFile(filepath);
        } else if (isAbsFile(filepath)) {
            // 绝对路径
            Path path = Paths.get(filepath);
            return Files.newInputStream(path);
        } else if (filepath.startsWith("~")) {
            // 用户目录下的绝对路径文件
            filepath = transferAbsPath(filepath);
            return Files.newInputStream(Paths.get(filepath));
        } else { // 相对路径
            return FileUtils.class.getClassLoader().getResourceAsStream(filepath);
        }
    }

    /**
     * 递归创建文件夹
     *
     * @param file 由目录创建的file对象
     * @throws FileNotFoundException
     */
    public static void mkDir(File file) throws FileNotFoundException {

        if (file.getParentFile() == null) {
            file = file.getAbsoluteFile();
        }

        if (file.getParentFile().exists()) {
            modifyFileAuth(file);
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        } else {
            mkDir(file.getParentFile());
            modifyFileAuth(file);
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        }
    }

    public static void mkDir(String src) throws FileNotFoundException {
        Assert.validateStringEmpty(src);
        File temp;
        int doc = src.lastIndexOf(".");
        int index = src.lastIndexOf(File.separator);
        if(doc > index){
            // 说明file为一个文件
            temp = new File(src.substring(0, index));
        }else{
            temp = new File(src);
        }
        mkDir(temp);
    }

    /**
     * 创建文件
     * @param file
     */
    public static boolean mkFile(File file) throws IOException {
        if(file.exists()){
            return true;
        }
        int i = file.getAbsolutePath().lastIndexOf(File.separator);
        File dir = new File(file.getAbsolutePath().substring(0, i));
        mkDir(dir);
        return file.createNewFile();
    }

    public static boolean mkFile(String path) throws IOException {
        return mkFile(new File(path));
    }

    /**
     * 改变文件权限
     * @param file
     */
    private static void modifyFileAuth(File file) {
        boolean ans = file.setExecutable(true, false);
        ans = file.setReadable(true, false) && ans;
        ans = file.setWritable(true, false) && ans;
        if (LOG.isDebugEnabled()) {
            LOG.debug("create file auth : {}", ans);
        }
    }

    /**
     * 写入网络文件或流
     * @param src
     * @param path
     * @param <T>
     * @throws Exception
     */
    public static <T> void write(T src, String path) throws Exception {
        if (src instanceof InputStream) {
            // 字节流
            write((InputStream) src, path);
        } else if (src instanceof URI) {
            // 网络资源文件时，需要下载到本地临时目录下
            write((URI) src, path);
        } else {
            throw new IllegalStateException("save file parameter only support String/URI/InputStream type! but input type is: " + (src == null ? null : src.getClass()));
        }
    }

    /**
     * 将网络文件写入到本地文件中
     * @param uri
     * @param path
     * @throws Exception
     */
    private static void write(URI uri, String path) throws Exception {
        Assert.validateStringEmpty(path);
        try {
            InputStream inputStream = HttpUtil.downFile(uri, null, null, null);
            write(inputStream, path);
        } catch (Exception e) {
            LOG.error("down file from url: {} error! e: {}", uri, e);
            throw e;
        }
    }

    /**
     * 将输入流数据写到输出流中
     * @param in
     * @param out
     * @param closeIn
     * @param closeOut
     */
    public static void write(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) {
        Assert.validateNull(in);
        Assert.validateNull(out);
        try {
            int len = in.available();
            //判断长度是否大于4k
            if (len <= 4096) {
                byte[] bytes = new byte[len];
                in.read(bytes);
                out.write(bytes);
            } else {
                int byteCount = 0;
                //1M逐个读取
                byte[] bytes = new byte[4096];
                while ((byteCount = in.read(bytes)) != -1) {
                    out.write(bytes, 0, byteCount);
                }
            }
        } catch (Exception e) {
            LOG.error("save stream into file error! e: {}", e);
        } finally {
            try {
                if(closeIn){
                    in.close();
                }
                if(closeOut){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                LOG.error("close stream error! e: {}", e);
            }
        }
    }

    public static void write(InputStream in, OutputStream out) throws FileNotFoundException {
        write(in, out, false, false);
    }

    /**
     * 将字节流保存到文件中
     *
     * @param in
     * @param path
     * @return
     */
    public static void write(InputStream in, String path) throws FileNotFoundException {
        Assert.validateStringEmpty(path);
        mkDir(new File(path));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
        InputStream inputStream = new BufferedInputStream(in);
        write(inputStream, outputStream, true, true);
        LOG.error("save stream into file error! filename: {}", path);
    }

    /**
     * 复制文件
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copy(String src,String dest) throws IOException {
        Assert.validateStringEmpty(src);
        Assert.validateStringEmpty(dest);
        File source = new File(src);
        if(!source.exists()){
            return;
        }
        File destFile = new File(dest);
        mkFile(destFile);


        FileInputStream in=new FileInputStream(source);
        FileOutputStream out=new FileOutputStream(destFile);
        int c;
        byte[] buffer = new byte[1024];
        while((c=in.read(buffer))!=-1){
            for(int i=0;i<c;i++){
                out.write(buffer[i]);
            }
        }
        in.close();
        out.close();
    }

    /**
     * 移动文件
     * @param src
     * @param dest
     * @return
     * @throws IOException
     */
    public static boolean move(String src, String dest) throws IOException {
        Assert.validateStringEmpty(src);
        Assert.validateStringEmpty(dest);
        File source = new File(src);
        if(!source.exists()){
            LOG.debug("file do not exists");
            return false;
        }
        mkFile(dest);
        return source.renameTo(new File(dest));
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean delete(String path){
        if(StringUtils.isEmpty(path)){
            return true;
        }
        File file = new File(path);
        if(!file.exists()){
            return true;
        }
        return file.delete();
    }

    /**
     * 连接多个路径
     * @param paths
     * @return
     */
    public static String join(String... paths){
        StringBuilder stringBuilder = new StringBuilder();
        for(String path : paths){
            if(path.endsWith(File.separator)){
                path = path.substring(0, path.length() - 1);
            }
            if(!path.startsWith(File.separator)){
                stringBuilder.append(File.separator);
            }
            stringBuilder.append(path);
        }
        return stringBuilder.toString();
    }

    /**
     * 分解路径
     * @param path
     * @return
     */
    public static List<String> resolve(String path){
        if(StringUtils.isEmpty(path)){
            return new ArrayList<>(0);
        }
        return Lists.newArrayList(path.split(File.separator));
    }

    /**
     * 转换window路径
     * @param path
     * @return
     */
    public static String transferWinPath(String path){
        if(StringUtils.isEmpty(path)){
            return path;
        }
        return path.replace("\\", File.separator);
    }

    /**
     * 转换linux路径
     * @param path
     * @return
     */
    public static String transferLinuxPath(String path){
        if(StringUtils.isEmpty(path)){
            return path;
        }
        return path.replace("/", File.separator);
    }

}

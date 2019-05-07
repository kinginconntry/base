package com.needto.common.utils;


import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * http导出工具类
 */
public class HttpExportUtils {

    /**
     * 导出格式支持
     */
    public enum ExportType {
        /**
         * xls excel97
         */
        XLS,
        /**
         * xls excel2017
         */
        XLSX,
        /**
         * commas excel
         */
        CSV,
        /**
         * txt 文本
         */
        TXT;
    }

    /**
     * 导出默认编码格式
     */
    private static final String DEFAULT_EXPORT_CHARSET = "GB2312";

    /**
     * 导出数据，使用默认的编码格式
     * @param response
     * @param fileType
     * @param fileName
     * @param headers
     * @param data
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String fileType, String fileName, List<ExportUtils.Header> headers, List<Map<String, Object>> data) throws IOException {
        export(response, fileType, fileName, null, headers, data);
    }

    /**
     * 导出数据
     * @param response 响应对象
     * @param fileType 导出文件格式
     * @param fileName 导出文件名
     * @param charset 响应编码
     * @param headers 数据头部信息
     * @param data 数据
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String fileType, String fileName, String charset, List<ExportUtils.Header> headers, List<Map<String, Object>> data) throws IOException {
        if(StringUtils.isEmpty(charset)){
            charset = DEFAULT_EXPORT_CHARSET;
        }
        String fileNameEncode = fileName;
        if(ExportType.XLSX.name().equalsIgnoreCase(fileType)){
            fileNameEncode += ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }else if(ExportType.CSV.name().equalsIgnoreCase(fileType)){
            fileNameEncode += ".csv";
            response.setContentType("application/octet-stream;charset=GB2312");
        }else if(ExportType.XLS.name().equalsIgnoreCase(fileType)){
            fileNameEncode += ".xls";
            response.setContentType("application/vnd.ms-excel");
        }else if(ExportType.TXT.name().equalsIgnoreCase(fileType)){
            fileNameEncode += ".txt";
            response.setContentType("application/octet-stream;charset=" + charset);
        }

        fileNameEncode = Encodes.urlEncode(fileNameEncode);

        response.setHeader("Location", fileNameEncode);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameEncode + "; filename*=utf-8''" + fileNameEncode);
        OutputStream outputStream = response.getOutputStream();
        try{
            if(ExportType.XLSX.name().equals(fileType)){
                ExportUtils.exportExcel107(fileName, headers, data, outputStream);
            }else if(ExportType.CSV.name().equals(fileType)){
                ExportUtils.exportCommas(headers, data, outputStream, charset);
            }else if(ExportType.XLS.name().equals(fileType)){
                ExportUtils.exportExcell97(fileName, headers, data, outputStream);
            }else if(ExportType.TXT.name().equals(fileType)){
                ExportUtils.exportTxt("\t", headers, data, outputStream, charset);
            }
        }catch (Error | Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.setHeader("Location", "");
            response.setHeader("Content-Disposition", "");
            String s = "导出失败，请换一种导出格式再试一次。";
            outputStream.write(s.getBytes());
        }
        outputStream.flush();
    }
}

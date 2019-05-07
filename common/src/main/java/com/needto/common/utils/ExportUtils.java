package com.needto.common.utils;

import com.csvreader.CsvWriter;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 导出工具
 */
public class ExportUtils {

    /**
     * 字符串默认编码格式
     */
    public static final String DEFAULT_CHARSET = "GB2312";

    /**
     * 默认可导出的最大行
     */
    public static final Integer DEFAULT_MAX_ROW = 1000;

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATEFORMAT = "yyyy-mm-dd hh:mm:ss";

    /**
     * 导出头部格式
     */
    public static class Header {
        /**
         * 导出字段key
         */
        public String key;
        /**
         * 字段对应的中文
         */
        public String name;
        /**
         * 字段在excel中的重排序，默认按照header添加的顺序
         */
        public int order;
        /**
         * 字段的格式信息
         */
        public String format;

        public Header(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public Header(String key, String name, int order) {
            this.key = key;
            this.name = name;
            this.order = order;
        }

        public Header(String key, String name, int order, String format) {
            this.key = key;
            this.name = name;
            this.order = order;
            this.format = format;
        }
    }

    /**
     * 获取日期时间格式
     * @param wb
     * @param format
     * @return
     */
    public static CellStyle getDateTimeStyle(Workbook wb, String format) {
        if(StringUtils.isEmpty(format)){
            format = DEFAULT_DATEFORMAT;
        }
        CellStyle dateTimeStyle = wb.createCellStyle();
        DataFormat dateTimeFormat= wb.createDataFormat();
        dateTimeStyle.setDataFormat(dateTimeFormat.getFormat(format));
        return dateTimeStyle;
    }

    /**
     * 格式化日期时间
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format){
        if(date == null){
            return null;
        }
        if(StringUtils.isEmpty(format)){
            format = DEFAULT_DATEFORMAT;
        }
        return DateFormatUtils.format(date, format);
    }

    /**
     * 导出excel107格式
     * @param sheetName
     * @param headers
     * @param data
     * @param out
     */
    public static void exportExcel107(String sheetName, List<Header> headers, List<Map<String, Object>> data, OutputStream out) throws IOException {
        if(CollectionUtils.isEmpty(headers) || CollectionUtils.isEmpty(data)){
            return;
        }
        if(data.size() > DEFAULT_MAX_ROW){
            data = data.subList(0, DEFAULT_MAX_ROW);
        }
        Workbook wb = new HSSFWorkbook();
        exportExcel(sheetName, headers, data, wb);
        wb.write(out);
    }

    /**
     * 导出Excel197格式
     * @param sheetName
     * @param headers
     * @param data
     * @param out
     */
    public static void exportExcell97(String sheetName, List<Header> headers, List<Map<String, Object>> data, OutputStream out) throws IOException {
        // 用SXSSFWorkbook处理大数据，需要poi3.8支持
        if(CollectionUtils.isEmpty(headers) || CollectionUtils.isEmpty(data)){
            return;
        }
        if(data.size() > DEFAULT_MAX_ROW){
            data = data.subList(0, DEFAULT_MAX_ROW);
        }
        Workbook wb=new SXSSFWorkbook(DEFAULT_MAX_ROW);
        exportExcel(sheetName, headers, data, wb);
        wb.write(out);
    }

    /**
     * 导出Exce格式
     * @param sheetName
     * @param headers
     * @param data
     * @param wb
     */
    public static void exportExcel(String sheetName, List<Header> headers, List<Map<String, Object>> data, Workbook wb){
        if(StringUtils.isEmpty(sheetName)){
            sheetName = "sheet";
        }
        headers.sort((o1, o2) -> {
            if(o1.order > o2.order){
                return -1;
            }else if(o1.order < o2.order){
                return 1;
            }else{
                return 0;
            }
        });
        Sheet sheet = wb.createSheet(sheetName);
        Row row = sheet.createRow(0);
        Cell cel;
        int o = 0;
        for(Header header : headers){
            cel = row.createCell(o);
            cel.setCellValue(header.name);
            o++;
        }

        CellStyle linkStyle = wb.createCellStyle();
        Font cellFont= wb.createFont();
        cellFont.setUnderline((byte) 1);
        cellFont.setColor(HSSFColor.BLUE.index);
        linkStyle.setFont(cellFont);

        int i=0;
        Object obj;
        for(Map<String, Object> temp : data){
            if(temp == null){
                continue;
            }
            Map<String, Object> transferTemp = Utils.transferDeepMapToHorizontal(temp, null);
            row = sheet.createRow(i+1);
            int j = 0;
            for (Header header : headers){
                cel = row.createCell(j);
                obj = transferTemp.get(header.key);
                if(obj == null){
                    cel.setCellValue("");
                }else if(obj instanceof Date){
                    cel.setCellStyle(getDateTimeStyle(wb, header.format));
                    cel.setCellValue((Date) obj);
                }else if(obj instanceof Double){
                    cel.setCellValue((Double)obj);
                }else if(obj instanceof Integer){
                    cel.setCellValue((Integer)obj);
                }else if(obj instanceof Long){
                    cel.setCellValue((Long)obj);
                }else{
                    cel.setCellValue(Utils.nullToString(obj));
                }
                j++;
            }
            i++;
        }

    }


    /**
     * 导出Commas格式
     * @param headers
     * @param data
     * @param out
     */
    public static void exportCommas(List<Header> headers, List<Map<String, Object>> data, OutputStream out, String charset) throws IOException {
        if(StringUtils.isEmpty(charset)){
            charset = DEFAULT_CHARSET;
        }
        CsvWriter wr =new CsvWriter(out, ',', Charset.forName(charset));
        for(Header header : headers){
            wr.write(header.name);
        }
        wr.endRecord();

        Object v;
        for(Map<String, Object> temp : data){
            if(temp == null){
                continue;
            }
            Map<String, Object> transferTemp = Utils.transferDeepMapToHorizontal(temp, null);
            for (Header header : headers){
                v = transferTemp.get(header.key);
                if(v == null){
                    // 不做处理
                    wr.write("");
                }else if(v instanceof Date){
                    wr.write(formatDate((Date) v, header.format));
                }else{
                    wr.write(Utils.nullToString(v));
                }
            }
            wr.endRecord();
        }
        wr.close();
    }

    /**
     * 导出文本格式
     * @param split
     * @param headers
     * @param data
     * @param out
     */
    public static void exportTxt(String split, List<Header> headers, List<Map<String, Object>> data, OutputStream out, String charset) throws IOException {
        if(StringUtils.isEmpty(charset)){
            charset = DEFAULT_CHARSET;
        }
        StringBuffer s = new StringBuffer();
        //head
        for(Header header : headers){
            s.append(header.name);
            s.append(split);
        }
        s.append("\r\n");

        Object v;
        for(Map<String, Object> temp : data){
            if(temp == null){
                continue;
            }
            Map<String, Object> transferTemp = Utils.transferDeepMapToHorizontal(temp, null);
            for (Header header : headers){
                v = transferTemp.get(header.key);
                if(v == null){
                    // 不做处理
                }else if(v instanceof Date){
                    s.append(formatDate((Date) v, header.format));
                }else{
                    s.append(Utils.nullToString(v));
                }
                s.append(split);
            }
            s.append("\r\n");
        }
        out.write(s.toString().getBytes(charset));
    }
}

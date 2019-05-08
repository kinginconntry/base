package com.needto.common.utils;

import com.google.common.collect.Lists;
import com.needto.common.entity.Dict;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * excel导入工具类
 */
public class ImportExcelUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ImportExcelUtils.class);

    public static class ExcelImportParams {
        /**
         * 解析器实现类
         */
        String processor;
        /**
         * excel临时文件
         */
        String file;
        /**
         * 参数
         */
        Dict target;
        /**
         * 映射表
         */
        Dict mapping;

        public String getProcessor() {
            return processor;
        }

        public void setProcessor(String processor) {
            this.processor = processor;
        }

        public Map<String, String> getTarget() {
            return target;
        }

        public void setTarget(Dict target) {
            this.target = target;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public Dict getMapping() {
            return mapping;
        }

        public void setMapping(Dict mapping) {
            this.mapping = mapping;
        }
    }

    /**
     * 导入出现的错误
     */
    public static class RowError {
        /**
         * 错误行
         */
        public int row;
        /**
         * 错误原因列表
         */
        public List<String> errors;

        public RowError() {
            this.errors = new ArrayList<>(0);
        }

        public RowError(int row, String error) {
            this.row = row;
            if(!StringUtils.isEmpty(error)){
                this.errors = Lists.newArrayList(error);
            }
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }

    /**
     * 导入结果
     */
    public static class ExcelImportResult {
        List<Dict> dataList;
        Dict extra;
        int totalRows;
        int importedRows;
        List<RowError> errors = new LinkedList<>();

        public List<Dict> getDataList() {
            return dataList;
        }

        public void setDataList(List<Dict> dataList) {
            this.dataList = dataList;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public int getImportedRows() {
            return importedRows;
        }

        public void setImportedRows(int importedRows) {
            this.importedRows = importedRows;
        }

        public List<RowError> getErrors() {
            return errors;
        }

        public void setErrors(List<RowError> errors) {
            this.errors = errors;
        }

        public void addError(int row, String message) {
            this.errors.add(new RowError(row, message));
        }

        public Dict getExtra() {
            return extra;
        }

        public void setExtra(Dict extra) {
            this.extra = extra;
        }
    }

    /**
     * 导入的头部信息
     */
    public static class Header {

        public enum Type {
            /**
             * 日期
             */
            DATE{
                @Override
                public Object transfer(Cell cell) {
                    if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                        if (DateUtil.isCellDateFormatted(cell) || DateUtil.isCellInternalDateFormatted(cell)) {
                            return cell.getDateCellValue();
                        } else {
                            Double dval = cell.getNumericCellValue();
                            return new Date(dval.longValue());
                        }
                    } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                        String str = cell.getStringCellValue();
                        if (str == null) {
                            str = "";
                        }
                        try {
                            return org.apache.commons.lang.time.DateUtils.parseDate(
                                    str.replace('/', '-'),
                                    Lists.newArrayList("yyyy-MM-dd", "yy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSSZ").toArray(new String[3]));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            },
            /**
             * 数字
             */
            NUMBER {
                @Override
                public Object transfer(Cell cell) {
                    if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                        return cell.getNumericCellValue();
                    } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                        String str = cell.getStringCellValue();
                        if (str != null) {
                            return NumberUtils.toDouble(str);
                        }
                    }
                    return null;
                }
            },
            /**
             * 地图
             */
            GPS {
                @Override
                public Object transfer(Cell cell) {
                    return Type.defaultTransfer(cell);
                }
            },
            /**
             * html
             */
            HTML {
                @Override
                public Object transfer(Cell cell) {
                    return Type.defaultTransfer(cell);
                }
            },
            /**
             * 文本
             */
            TEXT {
                @Override
                public Object transfer(Cell cell) {
                    return Type.defaultTransfer(cell);
                }
            },
            /**
             * 文本列表
             */
            TEXT_LIST {
                @Override
                public Object transfer(Cell cell) {
                    return Type.defaultTransfer(cell);
                }
            };
            public abstract Object transfer(Cell cell);

            public static Object defaultTransfer(Cell cell){
                try {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    LOG.error("importCellData error", e);
                }
                return null;
            }

        }

        /**
         * 列序号
         */
        public int column;
        /**
         * 列字段key
         */
        public String field;
        /**
         * 列字段类型
         * @see Type
         */
        public String type;

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * excel数据
     */
    public static class ExcelData {
        String message;
        int total;
        List<Header> header = new ArrayList<>(10);
        List<Map<String, Object>> dataList = new LinkedList<>();

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Header> getHeader() {
            return header;
        }

        public void setHeader(List<Header> header) {
            this.header = header;
        }

        public List<Map<String, Object>> getDataList() {
            return dataList;
        }

        public void setDataList(List<Map<String, Object>> dataList) {
            this.dataList = dataList;
        }

        void addHeader(Header h) {
            this.header.add(h);
        }

        Header header(int col) {
            return this.header.get(col);
        }

        void addRowData(Map<String, Object> data) {
            this.dataList.add(data);
        }
    }

    /**
     * 非预读模式
     * @param file
     * @param maxReadRows
     * @return
     * @throws IOException
     */
    public static ExcelData readExcel(File file, int maxReadRows) throws IOException {
        return readExcel(file, maxReadRows, false);
    }

    /**
     * 导入excel数据，有最大行限制
     * @param file
     * @param maxReadRows
     * @param preview 是否为预读模式
     * @return
     * @throws IOException
     */
    public static ExcelData readExcel(File file, int maxReadRows, boolean preview) throws IOException {
        ExcelData excelData = new ExcelData();
        Workbook workbook = loadExcel(file);
        if (workbook == null || workbook.getNumberOfSheets() <= 0) {
            excelData.setMessage("NO_DATA");
            return excelData;
        }
        if (workbook.getNumberOfSheets() > 1) {
            excelData.setMessage("MORE");
        }
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            excelData.setMessage("NO_DATA");
            return excelData;
        }
        Row headRow = sheet.getRow(0);
        if (headRow == null) {
            excelData.setMessage("NO_DATA");
            return excelData;
        }
        int lastColNum = headRow.getLastCellNum();
        for (int col = 0; col < lastColNum; col++) {
            excelData.addHeader(getHeader(headRow.getCell(col)));
        }

        // 是否已经排除第一行
        boolean exceptFirst = false;
        // 预览模式下只读10条数据
        int previeNum = 10;
        int total = 0;
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()){
            Row row = iterator.next();
            if(!exceptFirst){
                exceptFirst = true;
                continue;
            }
            total++;
            if(preview && total > previeNum){
                continue;
            }
            Map<String, Object> rowData = new LinkedHashMap<>();
            for (int col = 0; col < lastColNum; col++) {
                Header header = excelData.header(col);
                Cell cell = row.getCell(col);
                rowData.put(header.field, getCellData(cell, header));
            }
            excelData.addRowData(rowData);
            if (maxReadRows > 0 && total == maxReadRows) {
                break;
            }
        }
        excelData.setTotal(total);
        if (total == 0) {
            excelData.setMessage("NO_DATA");
        }
        return excelData;
    }

    /**
     * 根据对应关系将数据导入成表单数据
     *
     * @param file   要导入的文件
     * @param params 对应关系设置
     * @return 成表单数据
     */
    public static ExcelImportResult importExcelData(File file, ExcelImportParams params) throws IOException {
        ExcelImportResult result = new ExcelImportResult();
        Workbook workbook = loadExcel(file);
        if (workbook == null || workbook.getNumberOfSheets() <= 0) {
            return result;
        }
        Sheet sheet = workbook.getSheetAt(0);

        List<Dict> dataList = new LinkedList<>();

        Iterator<Row> iterator = sheet.iterator();
        // 是否已经排除第一行
        boolean exceptFirst = false;
        int total = 0;
        int importedRows = 0;
        while (iterator.hasNext()){
            Row row = iterator.next();
            if(!exceptFirst){
                exceptFirst = true;
                continue;
            }
            Dict rowData = new Dict();
            Set<String> errorFields = new LinkedHashSet<>();
            Dict mapping = params.getMapping();
            for (Object temp : mapping.keySet()) {
                Header header = mapping.getValue(temp.toString());
                Cell cell = row.getCell(header.getColumn());
                if (!importCellData(cell, header, rowData)) {
                    errorFields.add(header.field);
                }
            }
            if (errorFields.isEmpty()) {
                importedRows++;
                dataList.add(rowData);
            } else {
                result.addError(row.getRowNum(), "字段无法导入：" + String.join(",", errorFields));
            }
            total++;
        }
        result.setDataList(dataList);
        result.setImportedRows(importedRows);
        result.setTotalRows(total);
        return result;
    }

    private static Workbook loadExcel(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            Workbook workbook = null;
            if (file.getName().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (file.getName().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }
            return workbook;
        } finally {
            IOUtil.close(fis);
        }
    }

    private static Header getHeader(Cell cell) {
        Header h = new Header();
        h.field = cell.getStringCellValue();
        h.type = Header.Type.TEXT.name();
        h.column = cell.getColumnIndex();
        return h;
    }

    private static Object getCellData(Cell cell, Header header) {
        if (cell == null) {
            return null;
        }

        Object v = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell) || DateUtil.isCellInternalDateFormatted(cell)) {
                    v = cell.getDateCellValue();
                    header.type = Header.Type.DATE.name();
                } else {
                    v = cell.getNumericCellValue();
                    header.type = Header.Type.NUMBER.name();
                }
                break;
            case Cell.CELL_TYPE_STRING:
                v = cell.getStringCellValue();
                //含有yyyy-MM-dd， yyyy/MM/dd样子的字符串，姑且认为是日期
                String valString = StringUtils.defaultIfEmpty((String) v, "");
                if (valString.matches("\\d{4}-\\d{2}-\\d{2}") || valString.matches("\\d{4}/\\d{2}/\\d{2}")) {
                    v = cell.getDateCellValue();
                    header.type = Header.Type.DATE.name();
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                String formula = cell.getCellFormula();
                //超链接特殊处理
                if (formula.indexOf("HYPERLINK") == 0) {
                    v = cell.getHyperlink().getLabel() + "," + cell.getHyperlink().getAddress();
                    header.type = Header.Type.TEXT.name();
                } else {
                    v = cell.getNumericCellValue();
                    header.type = Header.Type.NUMBER.name();
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                v = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                v = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_ERROR:
                v = cell.getErrorCellValue() + "";
                break;
            default:
                v = cell.getStringCellValue();
        }
        return v;
    }

    /**
     * 获取一个单元数据
     * @param cell
     * @param header
     * @param rowData
     * @return
     */
    private static boolean importCellData(Cell cell, Header header, Dict rowData) {
        if (cell == null) {
            return true;
        }
        try {
            Header.Type typeEnum = Header.Type.valueOf(header.type);
            Object o = typeEnum.transfer(cell);
            if(o != null){
                rowData.put(header.field, cell.getStringCellValue());
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}

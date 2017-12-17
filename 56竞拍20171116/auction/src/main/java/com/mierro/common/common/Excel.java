package com.mierro.common.common;

import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 黄晓滨
 * @date 17/3/22
 * @Message java操作EXCEL工具集
 */
public class Excel {

    /**
     * 文件目录
     */
    private String Path="WEB-INF/resource/excel/";
    /**
     *  文档样式
     */
    private HSSFCellStyle style = null;
    /**
     *  返回的设定值
     */
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 字符编码
     */
    private String character_encoding;
    /**
     * 生成文件名
     */
    private String fileName;
    /**
     * 生成工作簿
     */
    private HSSFWorkbook workbook = null;
    /**
     * 生成工作表
     */
    private List<HSSFSheet> sheet = new ArrayList<>();
    /**
     * 设置使用的表
     */
    private Integer userSheet = 0;

    private List writeRows = new ArrayList();

    public Excel(){}

    public Excel(String fileName,String encoding){
        this.character_encoding = encoding;
        this.fileName = fileName;
        this.workbook = new HSSFWorkbook(); //产生工作簿对象
        this.style = workbook.createCellStyle();

    }

    public Excel(String fileName){
        this.fileName = fileName;
        this.character_encoding = "HSSFCell.ENCODING_UTF_16";
        this.workbook = new HSSFWorkbook(); //产生工作簿对象
        this.style = this.workbook.createCellStyle();
        HSSFFont font = this.workbook.getFontAt((short) 0);
        font.setCharSet(HSSFFont.DEFAULT_CHARSET);
        font.setFontHeightInPoints((short) 14);//更改默认字体大小
        font.setFontName("宋体");
        style.setFont(font);
    }

    public void addSheet(){
        HSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnStyle(this.sheet.size(),this.style);
        this.sheet.add(sheet);
    }

    public void setRow(List rows){
        if (this.sheet.size() == 0){
            addSheet();
        }
        Integer rowNumber = this.writeRows.size();
        this.writeRows.add(rows);
        HSSFRow row = this.sheet.get(userSheet).createRow(rowNumber);
        for (int i = 0; i < rows.size() ; i++) {
            HSSFCell cell = row.createCell((short) i);
            //判断传入类型,写入单元格
            Object temp = rows.get(i);
            if (temp instanceof Boolean){
                cell.setCellValue((Boolean) rows.get(i));
            }else if(temp instanceof Date){
                String dateTemp = format.format(rows.get(i));
                cell.setCellValue(dateTemp);
            }else if(temp instanceof Double){
                BigDecimal bigDecimal = new BigDecimal((Double) rows.get(i));
                cell.setCellValue(bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).toString());
            }else if(temp instanceof Integer){
                cell.setCellValue((Integer) rows.get(i));
            }else if(temp instanceof Float){
                cell.setCellValue((Float) rows.get(i));
            }else if(temp instanceof Calendar){
                cell.setCellValue((Calendar) rows.get(i));
            }else{
                cell.setCellValue(String.valueOf(rows.get(i)));
            }
        }
    }

     /**
       * @function:生成文件
       * @import:
      *  @return：相对路径
       */
    public String writeFile() throws IOException, URISyntaxException {
        //获取系统路径
        URI url = FileUtils.getRootURI();
        String path = url.getPath()+Path+this.fileName;
        //根据路径生成空文件
        Boolean b = FileUtils.createFile(path);
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        //进行列宽调整
        for (HSSFSheet aSheet : this.sheet) {
            Integer columns = aSheet.getRow(0).getPhysicalNumberOfCells();
            for (int j = 0; j < columns; j++) {
                aSheet.autoSizeColumn(j);
            }
        }
        this.workbook.write(fileOutputStream);
        return "/excel/"+this.fileName;
    }

    /**
     * 读取文件
     * @return true or false
     */
    public Boolean readFile(String src) {
        File excelFile = new File(src);
        FileInputStream is = null;
        try {
            is = new FileInputStream(excelFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //没有找到该文件
            return false;
        }
        try {
            this.workbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
            //文件读取失败
            return false;
        }
        return true;
    }

    /**
     * 更新excel表
     * @param sheetNumber
     * @param rowNumber
     * @param columnNumber
     * @param message 需要修改的数据
     * @return
     */
    public Boolean updateExcel(Integer sheetNumber,Integer rowNumber,Integer columnNumber,Object message){
        if (this.workbook == null){
            return false;
        }
        sheetNumber = sheetNumber-1;
        rowNumber = rowNumber-1;
        columnNumber = columnNumber-1;
        HSSFSheet sheet = this.workbook.getSheetAt(sheetNumber);
        int rowNum = sheet.getLastRowNum();
        if (rowNumber>rowNum){
            //输入的行数超过总行数
            return false;
        }
        HSSFRow row = sheet.getRow(rowNumber);
        HSSFCell cell = row.getCell(columnNumber);
        if (cell == null){
            //没有获取到该列
            return false;
        }
        if (message instanceof Boolean){
            cell.setCellValue((Boolean) message);
        }else if(message instanceof Date){
            String dateTemp = format.format(message);
            cell.setCellValue(dateTemp);
        }else if(message instanceof Double){
            BigDecimal bigDecimal = new BigDecimal((Double) message);
            cell.setCellValue(bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).toString());
        }else if(message instanceof Integer){
            cell.setCellValue((Integer) message);
        }else if(message instanceof Float){
            cell.setCellValue((Float) message);
        }else if(message instanceof Calendar){
            cell.setCellValue((Calendar) message);
        }else{
            cell.setCellValue(String.valueOf(message));
        }
        return true;
    }



    public String getCharacter_encoding() {
        return character_encoding;
    }

    public void setCharacter_encoding(String character_encoding) {
        this.character_encoding = character_encoding;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public Integer getUserSheet() {
        return userSheet;
    }

    public void setUserSheet(Integer userSheet) {
        this.userSheet = userSheet;
        this.writeRows = new ArrayList();
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public HSSFCellStyle getStyle() {
        return style;
    }

    public void setStyle(HSSFCellStyle style) {
        this.style = style;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }
}

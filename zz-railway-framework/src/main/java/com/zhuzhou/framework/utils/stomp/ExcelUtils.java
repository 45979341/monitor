package com.zhuzhou.framework.utils.stomp;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 读写Excel工具类
 */
@Slf4j
public class ExcelUtils {

	/**
	 * 读取Excel文件
	 * @param in excel文件流
	 * @param clazz 封装数据的对象类型
	 * @return 数据集合
	 */
	public static <T> List<T> read(InputStream in, Class<T> clazz) {
		return read(in, clazz, null, new String[]{});
	}

	/**
	 * 读取Excel文件
	 * @param in excel文件流
	 * @param clazz 封装数据的对象类型
	 * @param mapping Excel列与数据对象属性之间的映射关系（key为列序号 从1开始， value为属性名）
	 * @return 数据集合
	 */
	public static <T> List<T> read(InputStream in, Class<T> clazz, Map<Integer, String> mapping) {
		return read(in, clazz, mapping, null);
	}
	
    /**
     * 读取Excel文件
     * @param in excel文件流
     * @param clazz 封装数据的对象类型
     * @param mapping Excel列与数据对象属性之间的映射关系（key为列序号 从1开始， value为属性名）
     * @param sheetName 读取工作表的名称
     * @return 数据集合
     */
    public static <T> List<T> read(InputStream in, Class<T> clazz, Map<Integer, String> mapping, String[] sheetName) {
    	List<T> data = null;
		Workbook workbook = null;
    	try {
			workbook = WorkbookFactory.create(in);
			Sheet sheet;
			if(StringUtils.isEmpty(sheetName)){
				int count = workbook.getNumberOfSheets();
				for (int i = 0;i < count; i++) {
					sheet = workbook.getSheetAt(i);
					if (i == 0) {
						data = getTs(clazz, mapping, sheet);
					} else {
						data.addAll(getTs(clazz, mapping, sheet));
					}
				}
			} else {
				for (String sn : sheetName) {
					sheet = workbook.getSheet(sn);
					if (CollectionUtils.isEmpty(data)) {
						data = getTs(clazz, mapping, sheet);
					} else {
						data.addAll(getTs(clazz, mapping, sheet));
					}
				}
			}
		} catch (EncryptedDocumentException e) {
			throw new RuntimeException("Excel文件已被加密");
		} catch (InvalidFormatException e) {
			throw new RuntimeException("Excel文件格式不正确");
		} catch (IOException e) {
			throw new RuntimeException("Excel文件读取加密");
		} catch (SecurityException e) {
			throw new RuntimeException("Excel文件已被加密");
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Excel文件格式错误");
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch(IOException e){
					log.error(e.getMessage(), e);
				}
			}
		}
		return data;
    }

	private static <T> List<T> getTs(Class<T> clazz, Map<Integer, String> mapping, Sheet sheet) {
		List<T> data;
		if(sheet == null) {
            throw new RuntimeException("Excel文件找不到对应的表格");
        }
		data = new ArrayList<>(sheet.getLastRowNum());
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            data.add(readRow(row, clazz, mapping));
        }
		return data;
	}

	/**
	 * 导出Excel文件
	 * @param data
	 * @param mapping
	 * @param excelName
	 * @return
	 */
	public static <T> void write(List<T> data, Map<String, String> mapping, String excelName, OutputStream out){
		HSSFWorkbook wb =  new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(excelName);
		fillSheet(wb, sheet, data, mapping);
		try {
			wb.write(out);
			wb.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
	/**
     * 读取Excel一行数据
     * @param row
     * @param clazz
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException 
     */
    @SuppressWarnings("unchecked")
	private static <T> T readRow(Row row, Class<T> clazz, Map<Integer, String> fieldMapping){
		if(fieldMapping == null){
			return (T) getCellData(row.getCell(0));
		}
		T t ;
		try {
			t = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("实例化数据对象失败: " + clazz.getName(), e);
		}
		for (Entry<Integer, String> entry : fieldMapping.entrySet()) {
			Cell cell = row.getCell(entry.getKey());
			Object value;
//			if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
//				DecimalFormat formatter  = new DecimalFormat("###########");
//				value = formatter.format(cell.getNumericCellValue());
//			} else {
				value = getCellData(cell);
//			}
			setFieldValue(t, entry.getValue(), value);
		}
    	return t;
    }
 
    /**
     * 获取单元格中的值
     * @param cell
     * @return
     */
	private static Object getCellData(Cell cell) {
		if(cell == null){
			return null;
		}
		switch (cell.getCellTypeEnum()) {
            //空白
            case BLANK :
				return null;
            // 数值型 0----日期类型也是数值型的一种
            case NUMERIC:
				if(DateUtil.isCellDateFormatted(cell)){
					return cell.getDateCellValue();
				}
				return cell.getNumericCellValue();
            // 字符串型 1
            case STRING:
				return cell.getStringCellValue().trim();
            // 公式型 2
            case FORMULA:
				cell.setCellType(CellType.STRING);
				return cell.getStringCellValue();
            // 布尔型 4
            case BOOLEAN:
				return cell.getBooleanCellValue();
            // 错误 5
            case ERROR:
				return null;
		default :
			return null;
		}
	}
	
	/**
	 * 为数据对象属性赋值
	 * @param target
	 * @param fieldName
	 * @param value
	 */
	private static void setFieldValue(Object target, String fieldName, Object value){
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			if(value instanceof Number){
				Class<?> type = field.getType();
				Number number = (Number) value;
				if(type == Long.class || long.class == type){
					value = number.longValue();
				}else if(type == Integer.class || int.class == type){
					value = number.intValue();
				}else if(type == Short.class || short.class == type){
					value = number.byteValue();
				}else if(type == Float.class || float.class == type){
					value = number.floatValue();
				}else if(type == Double.class || double.class == type){
					value = number.doubleValue();
				}else if (type == String.class) {
					value = value.toString();
				}
			}else if (value instanceof Date) {
				value = DateUtils.sdf.format(value);
			} else if (value instanceof String) {
				String v = (String)value;
				try {
					value = DateUtils.sdf.parse(v);
				} catch (Exception e) {
				}
			}
			if (!ObjectUtils.isEmpty(value)) {
				field.set(target, value);
			}
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("数据对象中未找到属性【" + fieldName + "】",e);
		} catch (SecurityException e) {
			throw new RuntimeException("数据对象中属性【" + fieldName + "】不可访问", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("数据对象中属性【" + fieldName + "】不可访问", e);
		} catch (IllegalArgumentException e){
			throw new RuntimeException("数据对象中属性【" + fieldName + "】类型不匹配", e);
		}
	}
	
	/**
	 * 填充工作表
	 * @param sheet
	 * @param list
	 * @param mapping
	 * @throws Exception
	 */
	private static <T> void fillSheet(HSSFWorkbook wb, HSSFSheet sheet, List<T> list, Map<String,String> mapping){
		String[] fieldNames = new String[mapping.size()];
        String[] headNames = new String[mapping.size()];
        //填充数组 
        int count=0; 
        for(Entry<String, String> entry:mapping.entrySet()){
			headNames[count]=entry.getKey();
            fieldNames[count]=entry.getValue();
            count++; 
        }
        //填充表头 
        HSSFRow firstRow = sheet.createRow(0);
        HSSFCellStyle headStyle = createStyle(wb, 1);
        for(int i=0; i<headNames.length; i++){
            //设置自动列宽
            sheet.setColumnWidth(i, headNames[i].length()*4*256);
            HSSFCell cell = firstRow.createCell(i);
            //设置样式
            cell.setCellStyle(headStyle);
            cell.setCellValue(headNames[i]);
        } 
        //填充内容
        int rowNo = 1;
        HSSFCellStyle contentStyle = createStyle(wb, 2);
        for(int index = 0; index < list.size(); index++){
            T item = list.get(index); 
            HSSFRow row = sheet.createRow(rowNo);
            for(int i=0; i< fieldNames.length; i++){ 
            	Object value = ReflectionUtils.getFieldValue(item, fieldNames[i]);
				HSSFCell cell2 = row.createCell(i);
				if (value instanceof Date) {
					Date date = (Date) value;
					cell2.setCellValue(DateUtils.format(DateUtils.sdf, date));
				} else if (value instanceof Number) {
					Class<?> type = value.getClass();
					Number number = (Number) value;
					if(type == Long.class || long.class == type){
						cell2.setCellValue(number.longValue());
					}else if(type == Integer.class || int.class == type){
						cell2.setCellValue(number.intValue());
					}else if(type == Short.class || short.class == type){
						cell2.setCellValue(number.byteValue());
					}else if(type == Float.class || float.class == type){
						cell2.setCellValue(number.floatValue());
					}else if(type == Double.class || double.class == type){
						cell2.setCellValue(number.doubleValue());
					}
					cell2.setCellType(CellType.NUMERIC);
				} else {
					cell2.setCellValue(value == null ? "" : value.toString());
					cell2.setCellType(CellType.STRING);
				}
            	cell2.setCellStyle(contentStyle);
            } 
            rowNo ++;
        } 
	}
	
	/**
	 * 设置单元格样式
	 * @param hssfWorkbook
	 * @param layer
	 * @return
	 * @throws Exception
	 */
	private static HSSFCellStyle createStyle(HSSFWorkbook hssfWorkbook, int layer){
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        if (layer == 1) {
        	// 设置背景颜色
        	style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
            // 填充单元格
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 居中显示
            style.setAlignment(HorizontalAlignment.CENTER);
	        // 设置字体
	        HSSFFont hssfFont = hssfWorkbook.createFont();
            // 字体
            hssfFont.setFontName("宋体");
            // 字体颜色
            hssfFont.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            // 字高
            hssfFont.setFontHeight((short) 300);
            // 字体加粗
            hssfFont.setBold(true);
	        style.setFont(hssfFont);
        } else if (layer == 2) {
            // 背景
            style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            // 填充单元格
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 居中显示
            style.setAlignment(HorizontalAlignment.CENTER);
	        HSSFFont font = hssfWorkbook.createFont();
	        font.setFontName("宋体");
            // 字体加粗
//            font.setBold(true);
	        style.setFont(font);
        } else if (layer == 3) {
			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
            // 填充单元格
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return style;
     }

}
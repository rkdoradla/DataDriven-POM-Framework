package com.base.lib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import com.base.configuration.ConfigUtil;
import com.base.logging.LoggerManager;

/**
 * Class has methods to interact with the excel files. Methods to create
 * connection, run queries and return responses and close connections
 *
 * @author Ramakrishna Doradla Venkatesh
 * @version 
 * @since 
 */

public class Excel {

	private static Connection dbConnection = null;
	private static ResultSet resultSet = null;
	public static final String excelFilePath = null;
	public static LoggerManager loggerManger;
	
	
	/**
	 * Close all.
	 *
	 * @name closeAll
	 * @description 
	 * @author Ramakrishna Doradla Venkatesh
	 * @return void	||description:
	 * @throws SQLException 
	 * @jiraId 
	 */
	public void closeAll() throws SQLException {
		try {
			if (resultSet != null)
				resultSet.close();
			if (dbConnection != null)
				dbConnection.close();
		} catch (SQLException e) {
			throw e;
		}
	}


	/**
	 * Read data.
	 *
	 * @name readData
	 * @description Creates connection with the Excel sheet name passed and runs the query , returns the result set
	 * @author   Ramakrishna Doradla Venkatesh
	 * @param filename ||description: String Name of the Excel sheet
	 * ||allowedRange:
	 * @param query ||description: String query that needs to be run on the excel sheet
	 * ||allowedRange:
	 * @return ResultSet	||description: Result set of records returned by the Query
	 * @throws Exception 
	 * @jiraId 
	 */
	public ResultSet readData(String filename, String query){
		if (dbConnection == null)
			dbConnection = getExcelConnect(filename);
		try {
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			resultSet = stmt.executeQuery(query);
			// c.close();
			
		} catch (Exception e) {
			loggerManger.logger.info("Execption at readData(String filename, String query) in Excel.java:\n"+e.getMessage());
		}
		
		return resultSet;
	}

	/**
	 * Get excel connect.
	 *
	 * @name getExcelConnect
	 * @description Creates a connection to run queries on excel
	 * @author   Ramakrishna Doradla Venkatesh
	 * @param filename ||description: String Name/Path of the excel file to connect
	 * ||allowedRange:
	 * @return Connection	||description: Connection object with the connection to the file
	 * @throws Exception 
	 * @jiraId 
	 */
	public Connection getExcelConnect(String filename) {
		try {
			Class.forName("com.googlecode.sqlsheet.Driver");
			filename = filename.replace("\\", "/");
			dbConnection = DriverManager.getConnection("jdbc:xls:file:" + filename);
		} catch (Exception e) {
			loggerManger.logger.info("Execption at getExcelConnect(String filename) in Excel.java:\n"+e.getMessage());
		}
		return dbConnection;
	}

	
	/**
	 * Excel read.
	 *
	 * @name excelRead
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as List<Object[]> 
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sExcelPath ||description: String Name/Path of the excel file to read
	 * ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file to work
	 * ||allowedRange:
	 * @return List	||description: List<Object[]>, Object[] contains LinkedHashMap<String,String>
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	public static List<Object[]> excelRead(String sExcelPath, String sSheetName) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<Object[]> DataList = new ArrayList<>();

		try 
		{
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;
			if (sExcelPath.contains(".xlsx"))
			{
				workbook = new XSSFWorkbook(oFis);
			}
			else
			{
				workbook  = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) 
			{
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) 
				{
					bHeaderRow = true;
				}
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) 
				{
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK))
					{
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					}
					else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) 
					{
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK)
						{
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} 
						else
						{
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if ((sHeaderKey.length != 0) && (sValue.length != 0))
				{
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++)
					{
						if (i < sValue.length)
						{
							RowData.put(sHeaderKey[i], sValue[i]);
						}
						else
						{
							RowData.put(sHeaderKey[i], null);
						}
					}
					DataList.add( new Object[]{RowData} );
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
			loggerManger.logger.info("Execption at excelRead(String sExcelPath, String sSheetName) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}

	
	/**F
	 * Excel read hash map.
	 *
	 * @name excelReadHashMap
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as List<LinkedHashMap<"ColumnHeader", "RowValue">> 
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sExcelPath ||description: String Name/Path of the excel file to read
	 * ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file to work
	 * ||allowedRange:
	 * @return List	||description: List of LinkedHashMap<"ColumnHeader", "RowValue">
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	public static List<LinkedHashMap<String,String>> excelReadHashMap(String sExcelPath, String sSheetName) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<LinkedHashMap<String,String>> DataList = new ArrayList<>();
		try 
		{
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;
			
			if (sExcelPath.contains(".xlsx"))
			{
				workbook = new XSSFWorkbook(oFis);
			}
			else
			{
				workbook  = new HSSFWorkbook(oFis);
			}
		
			
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else{
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if ((sHeaderKey.length != 0) && (sValue.length != 0)) 
				{
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) 
					{
						if (i < sValue.length)
						{
							RowData.put(sHeaderKey[i], sValue[i]);
						}
						else
						{
							RowData.put(sHeaderKey[i], null);
						}
					}
					DataList.add( RowData );
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
			loggerManger.logger.info("Execption at excelReadHashMap(String sExcelPath, String sSheetName) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}
	
	
	 /**
 	 * Excel read.
 	 *
 	 * @name excelRead
 	 * @description Reads data from an excel(.xlsx) sheet and returns the result as List<Obejct[]> based on the condition column & its value
 	 * @author 
 	 * @param sExcelPath ||description: String Name/Path of the excel file to connect
 	 * ||allowedRange:
 	 * @param sSheetName ||description: String Name of the sheet in the excel file to work
 	 * ||allowedRange:
 	 * @param sCondCol ||description: String Name of the column in the excel file to apply condition
 	 * ||allowedRange:
 	 * @param sCondVal ||description: String Condition value to apply for valid test data
 	 * ||allowedRange:
 	 * @return List	||description: List of Object[], Object[] contains HashMap<Column Header, Value>
 	 * @throws Exception 	||type: exception
 	 * @jiraId 
 	 */
	public static List<Object[]> excelRead(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<Object[]> DataList = new ArrayList<>();
		FileInputStream oFis = null;
		Workbook workbook = null;

		try 
		{
			 oFis = new FileInputStream(sExcelPath);
			// Using XSSF for xlsx format, for xls use HSSF
			//workbook = new XSSFWorkbook(oFis);
			if (sExcelPath.contains(".xlsx"))
			{
				workbook = new XSSFWorkbook(oFis);
			}
			else
			{
				workbook  = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) 
			{
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) 
				{
					bHeaderRow = true;
				}
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) 
				{
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) 
					{
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} 
					else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) 
					{
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK)
						{
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} 
						else
						{
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				
				if(bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol))
				{
					workbook.close();
					throw new InvalidParameterException("Condition column:" +sCondCol +"doesn't exist in the sheet:" +sSheetName);
				}

				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					RowData = new LinkedHashMap<String, String>();

					for (int i = 0; i < sHeaderKey.length; i++)
					{
					//	RowData.put(sHeaderKey[i], sValue[i]);
						if (i < sValue.length)
						{
							RowData.put(sHeaderKey[i], sValue[i]);
						}
						else
						{
							RowData.put(sHeaderKey[i], null);
						}
					}
					if (RowData.get(sCondCol)!=null && RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) {
						DataList.add(new Object[] { RowData });
					}					
				}
			}
			workbook.close();
			oFis.close();
		} 
		catch (Exception e)
		{
			loggerManger.logger.info("Execption at excelRead(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}
	
	
	/**
	 * Excel read hash map.
	 *
	 * @name excelReadHashMap
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as List<LinkedHashMap<String,String>> based on the condition column & its value
	 * @author Ramakrishna Doradla Venkatesh
	* @param sExcelPath ||description: String Name/Path of the excel file to connect
 	 * ||allowedRange:
 	 * @param sSheetName ||description: String Name of the sheet in the excel file to work
 	 * ||allowedRange:
 	 * @param sCondCol ||description: String Name of the column in the excel file to apply condition
 	 * ||allowedRange:
 	 * @param sCondVal ||description: String Condition value to apply for valid test data
 	 * ||allowedRange:
 	 * @return List	||description: List of LinkedHashMap<"ColumnHeader", "Value">
	 * @throws 
	 * @jiraId 
	 */
	public static List<LinkedHashMap<String,String>> excelReadHashMap(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) {
		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<LinkedHashMap<String,String>> DataList = new ArrayList<>();
		try 
		{
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;
			// Using XSSF for xlsx format, for xls use HSSF
			//workbook = new XSSFWorkbook(oFis);
		
			if (sExcelPath.contains(".xlsx"))
			{
				workbook = new XSSFWorkbook(oFis);
			}
			else
			{
				workbook  = new HSSFWorkbook(oFis);
			}
			
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);

			while (rowIterator.hasNext()) 
			{
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) 
				{
					bHeaderRow = true;
				}

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) 
				{
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK))
					{
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} 
					else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) 
					{
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK)
						{
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} 
						else
						{
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if(bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol))
				{
					workbook.close();
					throw new InvalidParameterException("Condition column:" +sCondCol +"doesn't exist in the sheet:" +sSheetName);
				}
				
				if ((sHeaderKey.length != 0) && (sValue.length != 0)) 
				{
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) 
					{
						if (i < sValue.length)
						{
							RowData.put(sHeaderKey[i], sValue[i]);
						}
						else
						{
							RowData.put(sHeaderKey[i], null);
						}						
					}

					if (RowData.get(sCondCol)!=null && RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) 
					{
						DataList.add( RowData );
					}
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
			loggerManger.logger.info("Execption at excelReadHashMap(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}

	
	/**
	 * Update excel.
	 *
	 * @name updateExcel
	 * @description Updates the data to excel(.xlsx) sheet based on the condition column & its value
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sExcelPath ||description: String Name/Path of the excel file to connect
	 * ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file to work
	 * ||allowedRange:
	 * @param sCondCol ||description: String Name of the column in the excel file to apply condition
	 * ||allowedRange:
	 * @param sCondVal ||description: String Condition value to apply for valid test data
	 * ||allowedRange:
	 * @param DataPair ||description: LinkedHashMap<String,String> value to be updated in the sheet
	 * ||allowedRange:
	 * @return void	||description:
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	public static void updateExcel(String sExcelPath, String sSheetName, String sCondCol, String sCondVal,
			LinkedHashMap<String, String> DataPair) {

		String[] sHeaderKey = new String[0];
		try 
		{
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;
			
			if (sExcelPath.contains(".xlsx"))
			{
				workbook = new XSSFWorkbook(oFis);
			}
			else
			{
				workbook  = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			boolean bFlag= false;
			while (rowIterator.hasNext()) 
			{
				Boolean bHeaderRow = false;
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) 
				{
					bHeaderRow = true;
				}

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) 
				{
					if (bFlag==true)
					{
						break;
					}
					
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) 
					{
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} 
					else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null))
					{						
						String sCellVal;
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK)
						{
							sCellVal = formatter.formatCellValue(cell);
						} 
						else
						{
							sCellVal = "";
						}
						
						if ((sHeaderKey[cell.getColumnIndex()].equals(sCondCol))
								&& (sCellVal.equals(sCondVal)))
						{

							//Iterator<Cell> targetCellIterator = row.cellIterator();
							for (int i = 0; i < DataPair.size(); i++) 
							{
								/*while (targetCellIterator.hasNext()) 
								{
									Cell targetCell = targetCellIterator.next();
									if (DataPair.get(sHeaderKey[targetCell.getColumnIndex()]) != null) 
									{
										targetCell.setCellValue(DataPair.get(sHeaderKey[targetCell.getColumnIndex()]));
										break;
									}
								}*/
								int j=0;
								while (j< sHeaderKey.length ) 
								{
									//Cell targetCell = targetCellIterator.next();
									Cell cell1 = row.getCell(j, Row.RETURN_NULL_AND_BLANK);
									if (DataPair.get(sHeaderKey[j]) != null) 
									{
										if (cell1 == null)
										{
											 row.createCell(j, Cell.CELL_TYPE_STRING);
											 cell1 = row.getCell(j, Row.RETURN_NULL_AND_BLANK);
											 bFlag=true;
										}
										cell1.setCellValue(DataPair.get(sHeaderKey[j]));
										break;
									}
									j++;
								}

							}
						}

					}
				}

			}
			oFis.close();
			FileOutputStream oFos = new FileOutputStream(sExcelPath);
			workbook.write(oFos);
			workbook.close();
		} 
		catch (Exception e) 
		{
			loggerManger.logger.info("Execption at updateExcel(String sExcelPath, String sSheetName, String sCondCol, String sCondVal, LinkedHashMap<String, String> DataPair) in Excel.java:\n"+e.getMessage());
		}
	}

	
	/**
	 * Excel read multiple sheets.
	 *
	 * @name excelReadMultipleSheets
	 * @description Reads the data from multiple sheets in XLSX excel and return them as a List based on the condition column & primary column reference,
	 * primary column should be configured in the Config.properties file using the property "env.data.primarycolumn".
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sExcelPath ||description: String Name/Path of the excel file to connect
	 * ||allowedRange:
	 * @param sSheetName ||description: Array of String, Excel Sheet Names
	 * ||allowedRange:
	 * @param sCondCol ||description: String Name of the column in the excel file to apply condition
	 * ||allowedRange:
	 * @param sCondVal ||description: String Condition value to apply for valid test data
	 * ||allowedRange:
	 * @return List	||description: List of Object[], Object[] contains LinkedHashMap<ColumHeader, RowValue>
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	@SuppressWarnings("resource")
	public static List<Object[]> excelReadMultipleSheets(String sExcelPath, String sSheetName[], String sCondCol, String sCondVal) {
		
	String[] sHeaderKey = new String[0];
	String[] sValue = new String[0];
	ArrayList<Object[]> DataList;
	String Col_Name = null;
	Col_Name = ConfigUtil.config.get("env.data.primarycolumn");
	//Col_Name = "SR #";
			
	LinkedHashMap<String, String> RowData = null;
	Row DataRow = null;
	int primaryColumnIndex = 0;
	DataList = new ArrayList<Object[]>();
	FileInputStream oFis = null;
	Workbook workbook = null;
	
	try {
		oFis = new FileInputStream(sExcelPath);
	//	workbook = new XSSFWorkbook(oFis);
		
		if (sExcelPath.contains(".xlsx"))
		{
			workbook = new XSSFWorkbook(oFis);
		}
		else
		{
			workbook  = new HSSFWorkbook(oFis);
		}
	
		
		
		Sheet sheet = workbook.getSheet(sSheetName[0]);
		Iterator<Row> rowIterator = sheet.iterator();
		DataFormatter formatter = new DataFormatter(Locale.US);

			while (rowIterator.hasNext()){
				Boolean bHeaderRow = false;
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				sValue = new String[0];

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} 
					else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else{
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				
				if(bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol)){
					throw new InvalidParameterException("Condition column:" +sCondCol +"doesn't exist in the sheet:" +sSheetName[0]);
				}
				
				if (sValue.length == sHeaderKey.length && row.getRowNum()!=0) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++)
					{
						if (i < sValue.length)
						{
							RowData.putIfAbsent(sHeaderKey[i], sValue[i]);
						}
						else
						{
							RowData.putIfAbsent(sHeaderKey[i], null);
						}
					}
				
					if (RowData.get(sCondCol)!=null && RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) {
						if(sSheetName.length==1){
							DataList.add(new Object[] { RowData });
						}else if(Col_Name.isEmpty()){
							throw new InvalidParameterException("Config parameter env.data.primarycolumn can't be empty");
						}
									
						//Check child sheets and add data to RowData
						int sheetCount;
						for (sheetCount = 1; sheetCount <sSheetName.length; sheetCount++) {
							Sheet sheet1 = workbook.getSheet(sSheetName[sheetCount]);
									
							// Get the primary Column Index in the child sheet
							Iterator<Row> rowIteratorHeader = sheet1.iterator();
							if(rowIteratorHeader.hasNext())	{
								Row childSheetRow = rowIteratorHeader.next();
								Iterator<Cell> childSheetCellIterator = childSheetRow.cellIterator();
								primaryColumnIndex = 0;
								
								while (childSheetCellIterator.hasNext())
								{
									Cell cell = childSheetCellIterator.next();
									cell.setCellType(Cell.CELL_TYPE_STRING);
										if(cell.getStringCellValue().toString().equals(Col_Name)){
											primaryColumnIndex =  cell.getColumnIndex() + 1;
											break;
										}
								}
							} else{
								System.out.println("No valid data found in sheet:" +sheet1.getSheetName());
								//Adding the data from multiple sheets into DataList 
								if (sheetCount == sSheetName.length-1){
									DataList.add(new Object[] { RowData });
								}
								continue;
							}

							
							if (primaryColumnIndex != 0 ){
								//Get the row number of the data
								int rowCount = sheet1.getLastRowNum();
								DataRow = null;
								for (int i = 0; i <= rowCount; i++) 
								{
									Cell cell;
									cell = sheet1.getRow(i).getCell(primaryColumnIndex-1);
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String CellData = cell.getStringCellValue().toString();
									if (CellData.equalsIgnoreCase(RowData.get(Col_Name))) {
										DataRow = sheet1.getRow(i);
										break;
									}
								}
								
								
								if(DataRow!=null){
									//Read the child sheet row data
									String[] sChildHeaderKey = new String[0];
									String[] sChildValue = new String[0];
									Cell cell;
									Row childRow = sheet1.getRow(0);
									Iterator<Cell> headerRow = childRow.iterator();
									Iterator<Cell> dataRowCellIterator = DataRow.cellIterator();
									
									while(headerRow.hasNext()){
										cell = headerRow.next();
										if ((cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
											sChildHeaderKey = Arrays.copyOf(sChildHeaderKey, sChildHeaderKey.length + 1);
											if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
												sChildHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
											} else{
												sChildHeaderKey[cell.getColumnIndex()] = "";
											}
										}
									}
									
									while (dataRowCellIterator.hasNext()) {
										cell = dataRowCellIterator.next();
										if ((sChildHeaderKey[cell.getColumnIndex()] != null)) {
											sChildValue = Arrays.copyOf(sChildValue, sChildValue.length + 1);
											if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
												sChildValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
											} else{
												sChildValue[cell.getColumnIndex()] = "";
											}
										}
									}
									if (sChildValue.length == sChildHeaderKey.length)
									{
										for (int i = 0; i < sChildHeaderKey.length; i++) 
										{
											if(i < sChildValue.length)
											{
												RowData.putIfAbsent(sChildHeaderKey[i], sChildValue[i]);
											}
											else
											{
												RowData.putIfAbsent(sChildHeaderKey[i], null);
											}
										}
									}
									else
									{
										System.out.println("Mismatch found between no. of row values and header values in Sheet:" +sheet1.getSheetName() +", for the iteration:"+RowData.get(Col_Name) + ", No. of Headers: "+sChildHeaderKey.length + ", No. of Values:" + sChildValue.length);
									}
								
									
								}else{
									System.out.println("Unable to find a valid data row in sheet:"+sheet1.getSheetName() +", for the iteration:"+RowData.get(Col_Name));
								}
									
								
							}else{
								System.out.println("Unable to find primary column in sheet:"+sheet1.getSheetName());
							}
							
							//Adding the data from multiple sheets into DataList 
							if (sheetCount == sSheetName.length-1){
								DataList.add(new Object[] { RowData });
							}
						}
						
					}
									
				}else if (row.getRowNum()!=0){
					System.out.println("Mismatch found between no. of row values and header values in Sheet:" +sSheetName[0] +", for the row:"+row.getRowNum()  + ", No. of Headers: "+sHeaderKey.length + ", No. of Values:" + sValue.length);
				}
			}	

			workbook.close();
			oFis.close();
	} catch(Exception e) {
		
		loggerManger.logger.info("Execption at excelReadMultipleSheets(String sExcelPath, String sSheetName[], String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
	}
		
		return DataList;
	}
	

	/**
	 * Excel read multi sheets hash map.
	 *
	 * @name excelReadMultiSheetsHashMap
	 * @description Reads the data from multiple sheets in XLSX excel and return them as a List<LinkedHashMap<String,String>> based on the condition column & primary column reference,
	 * primary column should be configured in the Config.properties file using the property "env.data.primarycolumn".
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sExcelPath ||description: String Name/Path of the excel file to connect
	 * ||allowedRange:
	 * @param sSheetName ||description: Array of String, Excel Sheet Names
	 * ||allowedRange:
	 * @param sCondCol ||description: String Name of the column in the excel file to apply condition
	 * ||allowedRange:
	 * @param sCondVal ||description: String Condition value to apply for valid test data
	 * ||allowedRange:
	 * @return List	||description: List of LinkedHashMap<String,String>, HashMap contains <ColumHeader, RowValue>
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	@SuppressWarnings("resource")
	public static List<LinkedHashMap<String,String>> excelReadMultiSheetsHashMap(String sExcelPath, String sSheetName[], String sCondCol,
			String sCondVal) {
		
	String[] sHeaderKey = new String[0];
	String[] sValue = new String[0];
	ArrayList<LinkedHashMap<String,String>> DataList;
	String Col_Name = null;
	Col_Name = ConfigUtil.config.get("env.data.primarycolumn");
	//Col_Name = "SR #";
			
	LinkedHashMap<String, String> RowData = null;
	Row DataRow = null;
	int primaryColumnIndex = 0;
	DataList = new ArrayList<LinkedHashMap<String,String>>();
	FileInputStream oFis = null;
	Workbook workbook = null;
	
	try {
		oFis = new FileInputStream(sExcelPath);
		//workbook = new XSSFWorkbook(oFis);
		
		if (sExcelPath.contains(".xlsx"))
		{
			workbook = new XSSFWorkbook(oFis);
		}
		else
		{
			workbook  = new HSSFWorkbook(oFis);
		}
		Sheet sheet = workbook.getSheet(sSheetName[0]);
		Iterator<Row> rowIterator = sheet.iterator();
		DataFormatter formatter = new DataFormatter(Locale.US);

			while (rowIterator.hasNext()){
				Boolean bHeaderRow = false;
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				sValue = new String[0];

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} 
					else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else{
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				
				if(bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol)){
					throw new InvalidParameterException("Condition column:" +sCondCol +"doesn't exist in the sheet:" +sSheetName[0]);
				}
				
				if (sValue.length == sHeaderKey.length && row.getRowNum()!=0) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) 
					{
						if (i < sValue.length)
						{
							RowData.putIfAbsent(sHeaderKey[i], sValue[i]);
						}
						else
						{
							RowData.putIfAbsent(sHeaderKey[i], null);							
						}
					}
				
					if (RowData.get(sCondCol)!=null && RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase()))
					{
						if(sSheetName.length==1){
							DataList.add( RowData );
						}else if(Col_Name.isEmpty()){
							throw new InvalidParameterException("Config parameter env.data.primarycolumn can't be empty");
						}
									
						//Check child sheets and add data to RowData
						int sheetCount;
						for (sheetCount = 1; sheetCount <sSheetName.length; sheetCount++) {
							Sheet sheet1 = workbook.getSheet(sSheetName[sheetCount]);
									
							// Get the primary Column Index in the child sheet
							Iterator<Row> rowIteratorHeader = sheet1.iterator();
							if(rowIteratorHeader.hasNext())	{
								Row childSheetRow = rowIteratorHeader.next();
								Iterator<Cell> childSheetCellIterator = childSheetRow.cellIterator();
								primaryColumnIndex = 0;
								
								while (childSheetCellIterator.hasNext())
								{
									Cell cell = childSheetCellIterator.next();
									cell.setCellType(Cell.CELL_TYPE_STRING);
										if(cell.getStringCellValue().toString().equals(Col_Name)){
											primaryColumnIndex =  cell.getColumnIndex() + 1;
											break;
										}
								}
							} else{
								System.out.println("No valid data found in sheet:" +sheet1.getSheetName());
								//Adding the data from multiple sheets into DataList 
								if (sheetCount == sSheetName.length-1){
									DataList.add( RowData );
								}
								continue;
							}

							
							if (primaryColumnIndex != 0 ){
								//Get the row number of the data
								int rowCount = sheet1.getLastRowNum();
								DataRow = null;
								for (int i = 0; i <= rowCount; i++) 
								{
									Cell cell;
									cell = sheet1.getRow(i).getCell(primaryColumnIndex-1);
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String CellData = cell.getStringCellValue().toString();
									if (CellData.equalsIgnoreCase(RowData.get(Col_Name))) {
										DataRow = sheet1.getRow(i);
										break;
									}
								}
								
								
								if(DataRow!=null){
									//Read the child sheet row data
									String[] sChildHeaderKey = new String[0];
									String[] sChildValue = new String[0];
									Cell cell;
									Row childRow = sheet1.getRow(0);
									Iterator<Cell> headerRow = childRow.iterator();
									Iterator<Cell> dataRowCellIterator = DataRow.cellIterator();
									
									while(headerRow.hasNext()){
										cell = headerRow.next();
										if ((cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
											sChildHeaderKey = Arrays.copyOf(sChildHeaderKey, sChildHeaderKey.length + 1);
											if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
												sChildHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
											} else{
												sChildHeaderKey[cell.getColumnIndex()] = "";
											}
										}
									}
									
									while (dataRowCellIterator.hasNext()) {
										cell = dataRowCellIterator.next();
										if ((sChildHeaderKey[cell.getColumnIndex()] != null)) {
											sChildValue = Arrays.copyOf(sChildValue, sChildValue.length + 1);
											if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
												sChildValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
											} else{
												sChildValue[cell.getColumnIndex()] = "";
											}
										}
									}
									
									if (sChildValue.length == sChildHeaderKey.length)
									{
										for (int i = 0; i < sChildHeaderKey.length; i++) 
										{
											if( i < sChildValue.length)
											{
												RowData.putIfAbsent(sChildHeaderKey[i], sChildValue[i]);
											}
											else
											{
												RowData.putIfAbsent(sChildHeaderKey[i], null);
											}
										}
									}else{
										System.out.println("Mismatch found between no. of row values and header values in Sheet:" +sheet1.getSheetName() +", for the iteration:"+RowData.get(Col_Name) + ", No. of Headers: "+sChildHeaderKey.length + ", No. of Values:" + sChildValue.length);
									}
								
									
								}else{
									System.out.println("Unable to find a valid data row in sheet:"+sheet1.getSheetName() +", for the iteration:"+RowData.get(Col_Name));
								}
									
								
							}else{
								System.out.println("Unable to find primary column in sheet:"+sheet1.getSheetName());
							}
							
							//Adding the data from multiple sheets into DataList 
							if (sheetCount == sSheetName.length-1){
								DataList.add(RowData );
							}
						}
						
					}
									
				}else if (row.getRowNum()!=0){
					System.out.println("Mismatch found between no. of row values and header values in Sheet:" +sSheetName[0] +", for the row:"+row.getRowNum()  + ", No. of Headers: "+sHeaderKey.length + ", No. of Values:" + sValue.length);
				}
			}	

			workbook.close();
			oFis.close();
	} catch (Exception e) {
		loggerManger.logger.info("Execption at excelReadMultiSheetsHashMap(String sExcelPath, String sSheetName[], String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
		
	}
		
		return DataList;
	}
	
	public static Object[][] getData(Method method,Class<Test> className, String filePath, String sheetName){
		   List<Object[]>  DataList = null;
		   Object[][] dataArr = {} ;

		   String methodName = method.getAnnotation(className).testName();
		  
		   List<Object[]>  DataListCases = Excel.excelRead(filePath, "TestRun");
		   for(Object[] data : DataListCases) {
			   @SuppressWarnings("unchecked")
			LinkedHashMap<String, String> DataSet = (LinkedHashMap<String,String>) data[0];
	 		   if( DataSet.get("TestCaseName").equalsIgnoreCase(methodName) && DataSet.get("RunMode").equalsIgnoreCase("YES")) {
	 			  DataList = Excel.excelRead(filePath,sheetName);
	 			
		 			for(int i=0;i<DataList.size();i++) {
		 				@SuppressWarnings("unchecked")
						LinkedHashMap<String, String> tempMap = (LinkedHashMap<String,String>) DataList.get(i)[0];
		 	 			String testCaseName = tempMap.get("TestCaseName");
		 	 			if(!testCaseName.equalsIgnoreCase(methodName)) {
		 	 				DataList.remove(i);
		 	 			}
		 			}

	 			 dataArr = new Object[DataList.size()][];
	 			 dataArr=DataList.toArray(dataArr);
	 		   }
//	 	       }else {
//	 			   System.out.println("Skipping the test case as RunMode is NO or Test case" +DataSet.get("TestCaseName")+"is not found in the data sheet");
//	 		   }
		   } 
		   return dataArr;
	   }
	
	public static void writeData(String filePath, String sheetName,String value) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		
		XSSFWorkbook wb= new XSSFWorkbook(fis);  
	    XSSFSheet sheet = wb.getSheet(sheetName);
	    int columnNum = 0;
	    int rowNum = 0;
	    String columnName = null;
	    for(int i=0;i<sheet.getLastRowNum()+1;i++) {
	    	
	    	XSSFRow roww = sheet.getRow(i);
	    	for(int j=0;j<roww.getLastCellNum();j++) {
	    		columnName = roww.getCell(j).getStringCellValue();
	    		if(columnName.equalsIgnoreCase("ContentCrationFlowStatus")) {
	    			columnNum = j;
	    			rowNum = i;
	    			break;
	    		}
	    	}
	    	if(columnName.equalsIgnoreCase("ContentCrationFlowStatus")) {
    	   			break;
    		}
	    	
	    }
		FileOutputStream fout =new FileOutputStream(filePath);
	   	sheet.getRow(rowNum+1).createCell(columnNum).setCellValue(value);
		wb.write(fout);		 
		fout.close();
	}
	
}
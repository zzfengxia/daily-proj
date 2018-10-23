package com.card.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbExportUtil {
	private final static Logger logger = LoggerFactory.getLogger(DbExportUtil.class);

	private final static String DB_EXPORT_CHARSET = "UTF-8";

	public static boolean oracleExportData(String username, String password, String netname, String dbFilePath,
			String table, String condition, String dbExportLogPath, String dbExportLogName) {
		BufferedWriter bw = null;
		BufferedReader br = null;
		boolean flag = false;
		try {
			StringBuffer exp = new StringBuffer("exp ");
			exp.append(username);
			exp.append("/");
			exp.append(password);
			exp.append("@");
			exp.append(netname);
			exp.append(" file=");
			exp.append(dbFilePath + table);
			if (table != null && !"".equals(table.trim())) {
				exp.append(" TABLES=" + table);
			}
			if (condition != null && !"".equals(condition.trim())) {
				exp.append(" query=\" where " + condition + "\" ");
			}
			logger.info("exp cmd_String:" + exp.toString());

			File outFile = new File(dbExportLogPath + dbExportLogName + ".log");
			outFile.createNewFile();

			Process p = Runtime.getRuntime().exec(exp.toString());
			InputStreamReader isr = new InputStreamReader(p.getErrorStream(), DB_EXPORT_CHARSET);
			bw = new BufferedWriter(new FileWriter(outFile, true));
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
			p.destroy();
			flag = true;
		} catch (FileNotFoundException e) {
			logger.error("找不到指定的文件!", e);
		} catch (IOException e) {
			logger.error("读取文件失败!", e);
		} catch (Exception e) {
			logger.error("Error:", e);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				logger.error("数据库备份关闭流异常!", ex);
			}
		}
		return flag;
	}
	/***
	 * 
	 * @return
	 */
	private static String getExportCommand(String username, String password,String jdbcUrl,String dbFilePath,
			String table, String condition) {
		StringBuffer command = new StringBuffer();
		String[] arr = jdbcUrl.split("/");
		String host = arr[2].split(":")[0];// 导入的目标数据库所在的主机
		String port = arr[2].split(":")[1];// 使用的端口号
		String exportDatabaseName = arr[3];// 导出的目标数据库的名称
		String exportPath = dbFilePath+table+".sql";// 导出路径
		//mysqldump -uroot -padmin  vfc_card --extended-insert=true --no-create-info --table TBL_BSE_ORDER   >/tmp/TBL_BSE_ORDER.sql
		// 注意哪些地方要空格，哪些不要空格
		command.append("mysqldump -u").append(username).append(" -p").append(password)// 密码是用的小p，而端口是用的大P。
				.append(" -h").append(host).append(" -P").append(port).append(" ").append(exportDatabaseName)
				.append(" --extended-insert=true --no-create-info ").append(table);
		if(!(condition == null || "".equals(condition)))
			command.append(" -w\"").append(condition).append("\"");
		command.append(" -r ").append(exportPath);
		return command.toString();
	}


	public static boolean mysqlExportData(String username, String password,String jdbcUrl,String dbFilePath,
			String table, String condition, String dbExportLogPath, String dbExportLogName) {

		String sql = getExportCommand(username,password,jdbcUrl,dbFilePath,table,condition);
		logger.info("==========back sql :"+sql);
		Runtime runtime = Runtime.getRuntime();
		BufferedWriter bw = null;
		BufferedReader br = null;
		boolean flag = false;
		try {
			Process process = runtime.exec(sql);
			InputStreamReader isr = new InputStreamReader(process.getErrorStream(), DB_EXPORT_CHARSET);
			File outFile = new File(dbExportLogPath + dbExportLogName + ".log");
			outFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(outFile, true));
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				logger.info("dbback log :" +line);
				bw.newLine();
				bw.flush();
			}
			process.destroy();
			flag = true;
		} catch (FileNotFoundException e) {
			logger.error("找不到指定的文件!", e);
		} catch (IOException e) {
			logger.error("读取文件失败!", e);
		} catch (Exception e) {
			logger.error("Error:", e);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				logger.error("数据库备份关闭流异常!", ex);
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		String str = "jdbc:mysql://120.76.102.251:3306/vfc_cardoperate";
		String[] arr = str.split("/");
		System.out.println(arr[2].split(":")[0]);
		System.out.println(arr[2].split(":")[1]);
		System.out.println(arr[3]);
		boolean flag = mysqlExportData("WFC_KONGQUAN","111111","jdbc:mysql://120.76.102.251:3306/vfc_cardoperate","/tmp/","TBL_BSE_ORDER",
				"","/tmp/","TBL_BSE_ORDER");
		System.out.println(flag);
	}
}

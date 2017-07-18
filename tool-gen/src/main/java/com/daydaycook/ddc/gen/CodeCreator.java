package com.daydaycook.ddc.gen;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daydaycook.ddc.gen.model.GenEntity;
import com.daydaycook.ddc.gen.utils.GenUtils;
import com.daydaycook.ddc.gen.utils.VelocityUtils;
import com.daydaycook.ddc.gen.utils.XmlUtils;
import com.daydaycook.ddc.gen.xml.Config;
import com.daydaycook.ddc.gen.xml.JaxbMapper;
import com.daydaycook.ddc.gen.xml.Template;
import com.daydaycook.ddc.jdbc.Jdbc;
import com.daydaycook.ddc.jdbc.JdbcFactory;
import com.daydaycook.ddc.utils.io.FileUtil;
import com.daydaycook.ddc.utils.str.StringUtils;

/**
 * Hello world!
 *
 */
public class CodeCreator {
	static Jdbc jdbc = JdbcFactory.getJdbc("jdbc");
	static String DB_SQL = "select schema_name from information_schema.schemata where schema_name != 'information_schema' and schema_name != 'test'";

	static Logger logger = LoggerFactory.getLogger(CodeCreator.class);
	static Config config = XmlUtils.fileToObject("config.xml", Config.class);

	public static void main(String[] args) {
		String tableSchema = config.getTableSchema();
		List<String> tables = config.getTables();
		if (tables != null && tables.size() > 0) {
			for (String table : tables) {
				execute(tableSchema, table);
			}
		} else {
			List<String> list = GenUtils.getTableList(tableSchema);
			for (String table : list) {
				execute(tableSchema, table);
			}
		}
	}

	public static void execute(String tableSchema, String tableName) {
		GenEntity entity = GenUtils.getEntity(tableSchema, tableName);
		entity.setModule(config.getModule());
		entity.setPackageName(config.getPackageName());
		entity.setAuthor(config.getAuthor());
		// 表名去掉前缀
		String prefix = config.getPrefix();
		if (prefix != null && !"".equals(prefix)) {
			if (tableName.startsWith(prefix)) {
				String tableNameNew = tableName.substring(prefix.length());
				tableNameNew = StringUtils.parse2Camel(tableNameNew);
				entity.setName(tableNameNew);
			}
		}
		List<String> list = config.getTemplates();
		for (String tpl : list) {
			String s = VelocityUtils.getString(tpl, entity);
			Template template = JaxbMapper.fromXml(s, Template.class);
			String filePath = template.getFilePath();
			String fileName = template.getFileName();
			fileName = config.getRootPath() + filePath + fileName;
			FileUtil fileUtil = FileUtil.getInstance(fileName);
			try {
				fileUtil.write(template.getContent());
			} catch (IOException e) {
				logger.error("", e);
			} finally {
				fileUtil.close();
			}
		}
	}
}

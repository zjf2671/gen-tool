package com.daydaycook.ddc.gen.utils;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daydaycook.ddc.gen.model.GenEntity;
import com.daydaycook.ddc.utils.date.DateUtils;

public class VelocityUtils {
	private static Logger logger = LoggerFactory.getLogger(VelocityUtils.class);

	public static String getString(String template, GenEntity entity) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

		ve.init();
		Template actionTpt = ve.getTemplate(template, "UTF-8");
		VelocityContext ctx = new VelocityContext();
		ctx.put("className", entity.getClassName());
		ctx.put("name", entity.getName());
		ctx.put("comment", entity.getComment());
		ctx.put("fields", entity.getFields());
		ctx.put("ref", entity.getRef());
		ctx.put("module", entity.getModule());
		ctx.put("author", entity.getAuthor());
		ctx.put("packageName", entity.getPackageName());
		ctx.put("filePath", entity.getFilePath());
		ctx.put("today", DateUtils.getDefaultDateTimeNow());
		ctx.put("table", entity.getTable());
		ctx.put("pk", entity.getPk());
		StringWriter writer = new StringWriter();
		try {
			actionTpt.merge(ctx, writer);
			writer.flush();
			String str = writer.toString();
			return str;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return null;
	}
}

package com.gokhan.goeuro.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class ConfigurationUtil
{
	private static ConfigurationUtil instance;
	private Properties properties;
	private ResourceBundle bundle;

	public static ConfigurationUtil getInstance()
	{
		if (instance == null)
		{
			instance = new ConfigurationUtil();
		}
		return instance;
	}

	private ConfigurationUtil()
	{
		InputStream is = this.getClass().getResourceAsStream("/config.properties");
		properties = new Properties();
		try
		{
			properties.load(is);

			Locale locale = new Locale("EN");

			String language = properties.getProperty("default.language");
			if (language != null && !"".equals(language))
			{
				locale = new Locale(language);
			}
			bundle = ResourceBundle.getBundle("Keywords", locale);

			is.close();
		}
		catch (IOException e)
		{
		}
	}

	public String getKeyword(String key)
	{
		if (key == null)
		{
			return null;
		}

		return bundle.getString(key);
	}

	public String getProperty(String key)
	{
		return properties.getProperty(key, null);
	}
}

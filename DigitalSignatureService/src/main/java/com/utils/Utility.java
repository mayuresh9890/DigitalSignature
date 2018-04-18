package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Utility {
	private static Logger logger = Logger.getLogger(Utility.class);
	public Utility(){		
		if (loadProperty4m_SecificPath() == null) {
			logger.error("Error While Loading EmailProperties File");	
			return;
		}
	}
	
	public void loadLogPropertFile() {		
		PropertyConfigurator.configure(getClass().getResource("/com/utils/log4j.properties"));
		logger.info("log4j Intialized");
	}
	
	private static Properties loadProperty4m_SecificPath() {
		Properties prop = new Properties();
		try (InputStream inputStream = Utility.class.getClassLoader()
				.getResourceAsStream("/com/utils/DSproperties.txt")) {
			prop = new Properties();
			prop.load(inputStream);
		} catch (IOException e) {
			logger.error("Issues while reading property file", e);		
		}
		return prop;
	}
	
	public String getProperty(final String key) {
		return loadProperty4m_SecificPath().getProperty(key);
	}	
}

package com.mm.core;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertyConf
{

	static Properties properties = null;
	static
	{
		properties = new Properties();
		try
		{
			properties.load(new FileInputStream("F:\\code\\PropertySet.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String getProperty(String key)
	{
		return properties.getProperty(key);
	}	
	
}
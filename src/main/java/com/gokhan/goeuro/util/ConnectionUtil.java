package com.gokhan.goeuro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class ConnectionUtil
{
	public static String get(String urlString)
	{
		String result = "";
		try
		{
			URL obj = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			result = readUrlConnection(con);
		}
		catch (Exception e)
		{
			System.out.println("No Connection to the Server");
		}

		return result;
	}

	private static String readUrlConnection(HttpURLConnection con)
	{
		String result = "";
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(),
					Charset.forName("UTF-8")));

			String out;
			while ((out = reader.readLine()) != null)
			{
				result += out;
			}
			con.getInputStream().close();
		}
		catch (IOException e)
		{
		}

		return result;
	}
}

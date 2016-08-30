package com.gokhan.goeuro;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gokhan.goeuro.model.GeoPosition;
import com.gokhan.goeuro.model.GoEuroResponse;
import com.gokhan.goeuro.util.CSVUtils;
import com.gokhan.goeuro.util.ConfigurationUtil;
import com.gokhan.goeuro.util.ConnectionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author Gokhan Celikkaya
 *
 */
public class MainClass
{
	private ConfigurationUtil configurationUtil;
	private final String EXPORT_FILENAME = "Details.csv";

	public static void main(String[] args)
	{
		MainClass m = new MainClass();
		m.collectAndSaveLocationDetails(args);
	}

	/**
	 * Constructor method
	 * 
	 * @param args
	 */
	public MainClass()
	{
		configurationUtil = ConfigurationUtil.getInstance();
	}

	private String validateInput(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println(configurationUtil.getKeyword("location.empty"));
			return null;
		}
		else if (args.length > 1)
		{
			System.out.println(configurationUtil.getKeyword("location.multiple"));
			return null;
		}

		try
		{
			String location = URLEncoder.encode(args[0], "UTF-8");
			return location;
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println(configurationUtil.getKeyword("error.unexpected"));
			return null;
		}
	}

	/**
	 * Main method of the project. It gets and saves location details
	 * 
	 * @param location
	 */
	public void collectAndSaveLocationDetails(String[] args)
	{
		String location = validateInput(args);
		if (location == null)
		{
			return;
		}

		GoEuroResponse[] goEuroResponses = collectLocationDetails(location);
		if (goEuroResponses == null)
		{
			System.out.println(configurationUtil.getKeyword("error.get.location"));
			return;
		}

		String exportFileName = location + "_" + EXPORT_FILENAME;
		exportResultsToCSV(goEuroResponses, exportFileName);
	}

	/**
	 * Method that gets location details from server API
	 * 
	 * @param location
	 *            to be searched
	 * @return location details
	 */
	private GoEuroResponse[] collectLocationDetails(String location)
	{
		String baseUrl = configurationUtil.getProperty("server.url");
		if (baseUrl != null)
		{
			String responseString = ConnectionUtil.get(baseUrl + location);
			if (responseString == null || "".equals(responseString))
			{
				return null;
			}

			Gson gson = new Gson();
			try
			{
				return gson.fromJson(responseString, GoEuroResponse[].class);
			}
			catch (JsonSyntaxException e)
			{
				return null;
			}
		}

		return null;
	}

	/**
	 * Method exports given location list to a excel file
	 * 
	 * @param goEuroResponses
	 */
	private void exportResultsToCSV(GoEuroResponse[] goEuroResponses, String exportFileName)
	{
		try
		{
			FileWriter writer = new FileWriter(exportFileName);
			CSVUtils.writeLine(writer, Arrays.asList("_id", "name", "type", "latitude", "longitude"));

			for (GoEuroResponse goEuroResponse : goEuroResponses)
			{
				List<String> list = new ArrayList<>();
				list.add("" + goEuroResponse.getId());
				list.add(goEuroResponse.getName());
				list.add(goEuroResponse.getType());

				final GeoPosition geoPosition = goEuroResponse.getGeoPosition();
				if (geoPosition != null)
				{
					list.add("" + geoPosition.getLatitude());
					list.add("" + geoPosition.getLongitude());
				}
				else
				{
					list.add("");
					list.add("");
				}

				CSVUtils.writeLine(writer, list);
			}
			writer.flush();
			writer.close();

			System.out.println(configurationUtil.getKeyword("success.export") + exportFileName);
		}
		catch (IOException e)
		{
			System.out.println(configurationUtil.getKeyword("error.export.location"));
		}
	}
}

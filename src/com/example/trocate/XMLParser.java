package com.example.trocate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public class XMLParser implements FeedParser{

	private static final String GET_CLOSE_BINS	= "getCloseBinsResponse";
	private static final String SEND_REPORT		= "sendReportResponse";
	private static final String ADD_BIN			= "addBinResponse";
	private static final String REMOVE_BIN		= "removeBinResponse";

	private static final String RETURN_VALUE	= "return";

	private URL feedUrl;

	protected XMLParser(String url){
		try {
			this.feedUrl = new URL(url);
			//Log.d("feedURL", feedUrl.toString());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream(){
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			Log.d("INPUT STREAM", "===========================\n"+feedUrl.toString());
			Log.d("INPUT STREAM", "URL could not open an input stream", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] parse() {
		List<String> temp = null;
		XmlPullParser parser = Xml.newPullParser();
		boolean validResponse = false;
		try{
			//Log.d("PARSER", "getting the input stream");
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;
			//Log.d("PARSER", "entering the parser loop");
			while (eventType != XmlPullParser.END_DOCUMENT && !done){
				String name = null;
				//Log.d("TAG", name);
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					temp = new ArrayList<String>();
					break;
				case XmlPullParser.START_TAG:
					name = parser.getName();
					//Log.d("XML parser", "Tag: "+name);

					if(name.equalsIgnoreCase(GET_CLOSE_BINS))
						validResponse = true;
					else if(name.equalsIgnoreCase(ADD_BIN))
						validResponse = true;
					else if(name.equalsIgnoreCase(REMOVE_BIN))
						validResponse = true;
					else if(name.equalsIgnoreCase(SEND_REPORT))
						validResponse = true;

					if(name.equalsIgnoreCase(RETURN_VALUE)){
						String nextText = parser.nextText();
						temp.add(nextText);
						//Log.d("VALUE", nextText);
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("AndroidNews::XMLParser", e.getMessage(), e);
			throw new RuntimeException(e);
		}

		int pos = 0;
		String[] returnValue = new String[temp.size()];
		for(String str: temp)
			returnValue[pos++] = str;

		//Log.d("PARSER", returnValue[0]);
		if(validResponse)	{/*Log.d("PARSER", "isValid");*/return returnValue;}
		else				{/*Log.d("PARSER", "invalid");*/return null;}
	}
}

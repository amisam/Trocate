package com.example.trocate;

public class URLFactory {

	public static final int SEARCH = 0;
	public static final int ADD_BIN = 1;
	public static final int REMOVE_BIN = 2;
	public static final int REPORT_BIN = 3;

	private static final String httpURL = "http://";
	private static final String ipURL = "101.98.140.96:8081";
	private static final String baseURL = "/axis2/services/ServerFacade/";

	private static final String search1 = "getCloseBins?lat=";
	private static final String search2 = "&lng=";
	private static final String search3 = "&num=";
	private static final String search4 = "&filter=";

	private static final String report1 = "sendReport?subject=";
	private static final String report2 = "&message=";

	private static final String add1 = "addBin?lat=";
	private static final String add2 = "&lng=";
	private static final String add3 = "&type=";

	private static final String remove1 = "removeBin?lat=";
	private static final String remove2 = "&lng=";
	private static final String remove3 = "&type=";


	protected static String createURL(int type, String[] args) {
		String url;
		switch(type) {
		case SEARCH:
			url = httpURL+ipURL+baseURL+search1+args[0]+search2+args[1]+search3+args[2]+search4+args[3];
			break;
		case ADD_BIN:
			url = httpURL+ipURL+baseURL+add1 + args[0] + add2 + args[1] + add3 + args[2];
			break;
		case REMOVE_BIN:
			url = httpURL+ipURL+baseURL+remove1 + args[0] + remove2 + args[1] + remove3 + args[2];
			break;
		case REPORT_BIN:
			url = httpURL+ipURL+baseURL+report1 + args[0] + report2 + args[1];
			break;
		default:
			url = null;
			break;
		}
		return url;
	}

}
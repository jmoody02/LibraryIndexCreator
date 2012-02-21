package com.playaway.web.library;

/*Generate a java date compatible string from a DB2 Date string.
 * i.e. 2006-11-26 21:00:00:0 -> 20061126
 * @author Jonathan Moody 	jmoody@playaway.com
 */
public class TwoWayDB2DateStringBridge implements
		org.hibernate.search.bridge.TwoWayStringBridge {
	
	@Override
	public Object stringToObject(String arg0) {
		// 20061126 -> 2006-11-26 21:00:00:0
		String DB2DateString = "";
		if (arg0.length() >= 8) {
			DB2DateString = arg0.substring(0, 4) + "-" + arg0.substring(4, 6)
					+ "-" + arg0.substring(6);
			//DB2DateString = DB2DateString + "21:00:00:0";
		} else {
			DB2DateString = arg0;
		}
		return (String)DB2DateString;
	}

	@Override
	public String objectToString(Object arg0) {
		if (arg0 != null) {
			String db2Date = ((String) arg0).toString();
			if (db2Date.length() > 10) {
				String javaDateString = db2Date.substring(0, 4);
				javaDateString = javaDateString.concat(db2Date.substring(5, 7));
				javaDateString = javaDateString
						.concat(db2Date.substring(8, 10));
				return javaDateString;
			} else
				return "";
		} else {
			return "";
		}
	}

}

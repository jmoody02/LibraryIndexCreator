package com.playaway.web.library;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WCSUtil {
  public class ZeroResultsException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2314130707307184640L;

	ZeroResultsException(String s) {super(s);}
  }

  private static Logger logger = LoggerFactory.getLogger(WCSUtil.class);
  private Connection conn;
  private PreparedStatement pStatement;

  public WCSUtil(String url, String uname, String passwd) throws Exception {
    Class.forName("com.ibm.db2.jcc.DB2Driver");
    conn = DriverManager.getConnection(url, uname, passwd);
    pStatement = conn.prepareStatement(
       "select catentry_id from catentry where mfpartnumber = ?");
  }
    
  public int getCatEntryID(String libItem) throws Exception {
    pStatement.setString(1, libItem);
    logger.debug("libItem = " +  libItem);
    ResultSet res = pStatement.executeQuery();
    if (! res.next()) {
      throw new ZeroResultsException("zero Results for id=" + libItem);
    }
    int retval = res.getInt("catentry_id");
    res.close();
    return retval;
  }

  public int getTestViewCatEntryID(String libItem) throws Exception {
		//return 30003;
	  return 21601;
  }
  
  public void finalize() {
    try {
      pStatement.close();
      conn.close();
    } catch(Exception e) {
      logger.error("Error closing statement or connection", e);
    }
  }
}

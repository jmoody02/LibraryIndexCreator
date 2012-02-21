package com.view.web.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.search.Search;
import org.hibernate.search.FullTextSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playaway.web.library.WCSUtil;

//import com.playaway.web.library.WCSUtil;

import java.util.*;
import java.io.*;
import java.net.URL;

public class ViewSearchIndex {
	private Session session = null;
	private static Logger logger = LoggerFactory
			.getLogger(ViewSearchIndex.class);

	public ViewSearchIndex() throws Exception {
		try {
			logger.debug("Constructor");
			ClassLoader loader = this.getClass().getClassLoader();
			// ClassLoader loader = ClassLoader.getSystemClassLoader();
			URL configURL = loader.getResource("viewhibernate.cfg.xml");
			logger.info("hibernate resource path:" + configURL);
			init(configURL);
		} catch (Exception e) {
			logger.error("Exception in constructor:" + e.getMessage());
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	/**
	 * Create our session object by initilizing the hibernate lib based on a url
	 * to the hibernate.cfg.xml file.
	 */
	protected synchronized void init(URL configURL) {
		logger.info("configURL: " + configURL);
		if (session == null) {
			try {
				SessionFactory sessionFactory = new AnnotationConfiguration()
						.configure(configURL).buildSessionFactory();
				logger.debug("sessionFactory:" + sessionFactory);
				session = sessionFactory.openSession();
				//sessionFactory.close();
			} catch (Exception e) {
				logger.error("Exception in init:" + e.getMessage());
				System.out.println(e.getMessage());
				System.out.print("Stack:");
				e.printStackTrace(System.out);
			}
		}
		logger.info("Done loading session");
	}
	
	protected void cleanUp() {
		//session.close();
		session = null;
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Builds the lucene indexes in the directory specified by {@code
	 * hibernate.search.default.indexBase} property in the hibernate.cfg.xml
	 * file. Only used when we need to recreate our index. Not used by normal
	 * clients of this object.
	 */
	public void createIndex(int limit) throws Exception {
		logger.info("creating index");
		/*
		 * first create a WCSUtil object with access info from our properties
		 * file. At the moment this is the only part of the code that uses the
		 * properties file, if its needed elsewhere we should move this out of
		 * this method.
		 */
		int count = 0;
		try {
			Properties props = new Properties();
			ClassLoader loader = this.getClass().getClassLoader();
			URL propURL = loader.getResource("combined_search.properties");
			logger.info("combined_search.properties path:" + propURL);
			props.load(new FileInputStream(new File(propURL.getFile()))); 
			WCSUtil wcs = new WCSUtil(props.getProperty("db2url"), props
					.getProperty("db2user"), props.getProperty("db2password"));

			FullTextSession fullTextSession = Search
					.getFullTextSession(session);
			Transaction tx = fullTextSession.beginTransaction();
			Criteria crit = session.createCriteria(ViewBundle.class);

			// only index library items
			/*
			 * edit 2010-01-26: no need to restrict this now because the view
			 * vwMTR_library takes care of this for us
			 */

			// only create an index of the first {@code limit} records
			crit.setMaxResults(limit);
			@SuppressWarnings("unchecked")
			List<ViewBundle> viewList = crit.list();
			logger.info("create index, found " + viewList.size() + " records");
			for (ViewBundle view : viewList) {
				/*if (mt.getTitleSeries() != null) {
					System.out.println(mt);
				}*/
				count++;
				// fill in the catentry id from db2
				try {
					view.setCatEntryID(wcs.getCatEntryID(new Integer(view.getPv_library_item()).toString()));
					fullTextSession.index(view);
				} catch (WCSUtil.ZeroResultsException e) {
					logger.error("Library Item: " + view.getPv_library_item()
							+ " not in DB2!" + e);
				}
				if (count % 100 == 0) {
					logger.info("indexed " + count + " records");
					tx.commit(); // index is written at commit time
					tx = fullTextSession.beginTransaction();
				}
			}
			// commit remaining records
			tx.commit();
			logger.info("indexed " + count + " records");
			
			fullTextSession.close();
			
		} catch (Exception e) {
			logger.error("Exception in createIndex:" + e.getMessage());
			System.out.println(e.getMessage());
			throw (e);
		}
		cleanUp();
	}

	public static void main(String[] args) {
		int records = 8000; 
		if (args.length > 0) {
			try {
				records = Integer.parseInt(args[0]);
			} catch(Exception e) {
				System.out.println("Usage: LibrarySearchIndex [recordNum]");
				System.out.println("Defaulting to 8000 records...");
			}
		}
		else {
			System.out.println("Usage: LibrarySearchIndex [recordNum]");
			System.out.println("Defaulting to 8000 records...");
		}
		try {
			ViewSearchIndex testObj = new ViewSearchIndex();
			testObj.createIndex(records);
		} catch (Exception e) {
			System.out.println("Test failed on exception: " + e.getMessage());
		}
		System.exit(0);
	}
	public void testMe(int limit) throws Exception {
		try {
			Properties props = new Properties();
			ClassLoader loader = this.getClass().getClassLoader();
			URL propURL = loader.getResource("combined_search.properties");
			logger.info("combined_search.properties path:" + propURL);
			props.load(new FileInputStream(new File(propURL.getFile())));
			//WCSUtil wcs = new WCSUtil(props.getProperty("db2url"), props
			//		.getProperty("db2user"), props.getProperty("db2password"));
	
			FullTextSession fullTextSession = Search
					.getFullTextSession(session);
			Criteria crit = session.createCriteria(ViewBundle.class);
	
			// only create an index of the first {@code limit} records
			crit.setMaxResults(limit);
			@SuppressWarnings("unchecked")
			List<ViewBundle> viewList = crit.list();
			logger.info("create index, found " + viewList.size() + " records");
			for (ViewBundle view : viewList) {
				if (view.getPv_bundle_title() != null) {
					System.out.println(view);
				}
			}
			fullTextSession.close();
		} catch (Exception e) {
			logger.error("Exception in createIndex:" + e.getMessage());
			System.out.println(e.getMessage());
			throw (e);
		}
	}
}

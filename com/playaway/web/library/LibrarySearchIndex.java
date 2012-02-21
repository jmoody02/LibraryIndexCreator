package com.playaway.web.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.search.Search;
import org.hibernate.search.FullTextSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * The Library Search Index class provides functionality for creating the Lucene
 * index for future searching. Pathing configured in hibernate.cfg.xml.
 * 
 * Sample code for creating the index:
 * 
 * <pre>
 * LibrarySearchIndex indexCreator = new LibrarySearchIndex();
 * indexCreator.createIndex(310);
 * </pre>
 * 
 * @author Rob Tandy rtandy@playaway.com 
 * @author Jonathan Moody: Pulled into a stand
 *         alone class 03/22/2010
 */

public class LibrarySearchIndex {
	private Session session = null;
	private static Logger logger = LoggerFactory
			.getLogger(LibrarySearchIndex.class);

	public LibrarySearchIndex() throws Exception {
		try {
			logger.debug("Constructor");
			ClassLoader loader = this.getClass().getClassLoader();
			// ClassLoader loader = ClassLoader.getSystemClassLoader();
			URL configURL = loader.getResource("hibernate.cfg.xml");
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
	@SuppressWarnings("unchecked")
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
			URL propURL = loader.getResource("library_search.properties");
			logger.info("library_search.properties path:" + propURL);
			props.load(new FileInputStream(new File(propURL.getFile())));
			WCSUtil wcs = new WCSUtil(props.getProperty("db2url"), props
					.getProperty("db2user"), props.getProperty("db2password"));

			FullTextSession fullTextSession = Search
					.getFullTextSession(session);
			Transaction tx = fullTextSession.beginTransaction();
			Criteria crit = session.createCriteria(MasterTitle.class);

			// only index library items
			/*
			 * edit 2010-01-26: no need to restrict this now because the view
			 * vwMTR_library takes care of this for us
			 */

			// only create an index of the first {@code limit} records
			crit.setMaxResults(limit);
			List<MasterTitle> mts = crit.list();
			logger.info("create index, found " + mts.size() + " records");
			for (MasterTitle mt : mts) {
				count++;
				// fill in the catentry id from db2
				try {
					mt.setCatEntryID(wcs.getCatEntryID(mt.getLibraryItem()));
					fullTextSession.index(mt);
				} catch (WCSUtil.ZeroResultsException e) {
					logger.error("Library Item: " + mt.getLibraryItem()
							+ " not in DB2!" + e);
				}
				if (count % 100 == 0) {
					logger.info("indexed " + count + " records");
					tx.commit(); // index is written at commit time
					tx = fullTextSession.beginTransaction();
				}
			}
			// commit remaining records
			logger.info("indexed " + count + " records");
			tx.commit();
			
			//Lookups
			logger.info("creating lookup indexes...");
			count = 0;
			session.enableFilter("active");
			session.enableFilter("activeAges");
			session.enableFilter("activePublishers");
			session.enableFilter("activeAwards");
			session.enableFilter("activeVersions");
			tx = fullTextSession.beginTransaction();
			logger.info("genres...");
			crit = session.createCriteria(Genre.class);
			List<Genre> genrelist = crit.list();
			for (Genre gt : genrelist) {
				count++;
				fullTextSession.index(gt);
			}
			//tx.commit();
			logger.info("age levels...");
			crit = session.createCriteria(AgeLevel.class);
			List<AgeLevel> agelist = crit.list();
			for (AgeLevel at : agelist) {
				count++;
				fullTextSession.index(at);
			}
			//tx.commit();
			logger.info("publishers...");
			crit = session.createCriteria(Publisher.class);
			List<Publisher> publisherlist = crit.list();
			for (Publisher pt : publisherlist) {
				count++;
				fullTextSession.index(pt);
			}
			//tx.commit();
			logger.info("awards...");
			crit = session.createCriteria(Award.class);
			List<Award> awardlist = crit.list();
			for (Award awt : awardlist) {
				count++;
				fullTextSession.index(awt);
			}
			logger.info("abridgements...");
			crit = session.createCriteria(Abridgement.class);
			List<Abridgement> abridgelist = crit.list();
			for (Abridgement abt : abridgelist) {
				count++;
				fullTextSession.index(abt);
			}
			logger.info("release categories...");
			crit = session.createCriteria(ReleaseCategory.class);
			List<ReleaseCategory> categorylist = crit.list();
			for (ReleaseCategory rlt : categorylist) {
				count++;
				fullTextSession.index(rlt);
			}
			logger.info("release accessories...");
			crit = session.createCriteria(Accessory.class);
			List<Accessory> accessorylist = crit.list();
			for (Accessory access : accessorylist) {
				count++;
				access.setCatEntryID(wcs.getCatEntryID(new Integer(access.getIdAccessory()).toString()));
				fullTextSession.index(access);
			}
			
			logger.info("release services...");
			crit = session.createCriteria(Service.class);
			List<Service> servicelist = crit.list();
			for (Service service : servicelist) {
				count++;
				service.setCatEntryID(wcs.getCatEntryID(new Integer(service.getIdService()).toString()));
				fullTextSession.index(service);
			}
			
			tx.commit();
			
			fullTextSession.close();
			
		} catch (Exception e) {
			logger.error("Exception in createIndex:" + e.getMessage());
			System.out.println(e.getMessage());
			throw (e);
		}
		cleanUp();
	}

	public static void main(String[] args) {
		int records = 10000; 
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
			LibrarySearchIndex testObj = new LibrarySearchIndex();
			testObj.createIndex(records);
		} catch (Exception e) {
			System.out.println("Test failed on exception: " + e.getMessage());
		}
		System.exit(0);
	}
	
	@SuppressWarnings("unchecked")
	public void testMe(int limit) throws Exception {
		try {
			Properties props = new Properties();
			ClassLoader loader = this.getClass().getClassLoader();
			URL propURL = loader.getResource("library_search.properties");
			logger.info("library_search.properties path:" + propURL);
			props.load(new FileInputStream(new File(propURL.getFile())));
	
			FullTextSession fullTextSession = Search
					.getFullTextSession(session);
			Criteria crit = session.createCriteria(MasterTitle.class);
	
			// only create an index of the first {@code limit} records
			crit.setMaxResults(limit);
			List<MasterTitle> mts = crit.list();
			logger.info("create index, found " + mts.size() + " records");
			for (MasterTitle mt : mts) {
				if (mt.getTitleSeries() != null) {
					System.out.println(mt);
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

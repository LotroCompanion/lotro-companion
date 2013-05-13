package delta.games.lotro.tools.sourceforge;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import delta.downloads.Downloader;

/**
 * Load download statistics from sourceforge.
 * @author DAM
 */
public class MainLoadStats {

	private class Results
	{
		private HashMap<String,Integer> _countByCountries;
		private HashMap<String,Integer> _countByOSes;
		
		public Results() {
			_countByCountries = new HashMap<String,Integer>();
			_countByOSes = new HashMap<String,Integer>();
		}
	
		public void merge(Results results) {
			for(Map.Entry<String, Integer> entry : results._countByCountries.entrySet()) {
				String countryName=entry.getKey();
				Integer nb=entry.getValue();
				addForCountry(countryName, nb.intValue());
			}
			for(Map.Entry<String, Integer> entry : results._countByOSes.entrySet()) {
				String osName=entry.getKey();
				Integer nb=entry.getValue();
				addForOS(osName, nb.intValue());
			}
		}

		public void addForOS(String name, int nb) {
			Integer counter = _countByOSes.get(name);
			if (counter==null) {
				counter = Integer.valueOf(nb);
				_countByOSes.put(name, counter);
			}
			else {
				_countByOSes.put(name, Integer.valueOf(counter.intValue()+nb));
			}
		}
		
		public void addForCountry(String name, int nb) {
			Integer counter = _countByCountries.get(name);
			if (counter==null) {
				counter = Integer.valueOf(nb);
				_countByCountries.put(name, counter);
			}
			else {
				_countByCountries.put(name, Integer.valueOf(counter.intValue()+nb));
			}
		}
		
		public int getNbDownloads() {
			int count=0;
			for(Integer countForOS : _countByOSes.values()) {
				count+=countForOS.intValue();
			}
			int count2=0;
			for(Integer countForCountry : _countByCountries.values()) {
				count2+=countForCountry.intValue();
			}
			if (count!=count2) {
				System.err.println("Error: count="+count+", count2="+count2);
			}
			return count;
		}

		public String toString() {
			return "("+getNbDownloads()+") Countries: "+_countByCountries+"\nOS: "+_countByOSes;
		}
	}

	private Date buildDate(int year, int month, int day) {
		Calendar c=new GregorianCalendar(year,month-1,day,12,0);
		Date d=c.getTime();
		return d;
	}

	private Date today() {
    Calendar c=new GregorianCalendar();
    c.setTimeInMillis(System.currentTimeMillis());
    c.set(Calendar.HOUR_OF_DAY,12);
    c.set(Calendar.MINUTE,0);
    c.set(Calendar.SECOND,0);
    c.set(Calendar.MILLISECOND,0);
    Date d=c.getTime();
    return d;
	}

	private void doIt() {
		Date start=buildDate(2012,9,1);
		Date end=today();
		doIt(start, end, "lotro-companion-3.0 with JRE.zip");
		doIt(start, end, "lotro-companion-3.0.zip");
		doIt(start, end, "lotro-companion-2.0.zip");
		doIt(start, end, "lotro-companion-1.0.zip");
	}

	private Results doIt(Date start, Date end, String file) {
		Date myStart;
		Date myEnd=end;
		Results results=new Results();
    Calendar c=GregorianCalendar.getInstance();
		while (true) {
		  c.setTime(myEnd);
		  int year=c.get(Calendar.YEAR);
			int month=c.get(Calendar.MONTH)+1;
			myStart=buildDate(year,month,1);
			if (myStart.getTime()>=start.getTime()) {
				Results r=doItForOneMonthMax(myStart, myEnd, file);
				results.merge(r);
				myEnd=new Date(myStart.getTime()-24*3600*1000);
			}
			else {
				break;
			}
		}
		System.out.println("Results: "+results);
		return results;
	}
	
	private Results doItForOneMonthMax(Date start, Date end, String file) {
		Results results = new Results();
		String url=buildURL("lotrocompanion", file, start, end);
		//System.out.println("URL: "+url);
		Downloader d=new Downloader();
		String s=null;
		try {
			s=d.downloadString(url);
			JSONObject o=new JSONObject(s);
			// OSes
			{
				JSONArray oses=o.getJSONArray("oses");
				int nbOses=oses.length();
				for(int i=0;i<nbOses;i++) {
					JSONArray oneOS=(JSONArray)oses.get(i);
					String osName=(String)oneOS.get(0);
					Integer osNumber=(Integer)oneOS.get(1);
					results.addForOS(osName, osNumber.intValue());
				}
			}
			// Countries
			{
				JSONArray countries=o.getJSONArray("countries");
				int nbCountries=countries.length();
				for(int i=0;i<nbCountries;i++) {
					JSONArray oneOS=(JSONArray)countries.get(i);
					String countryName=(String)oneOS.get(0);
					Integer number=(Integer)oneOS.get(1);
					results.addForCountry(countryName, number.intValue());
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//System.out.println("Results: "+results);
		return results;
	}

	private String buildURL(String project, String file, Date start, Date end) {
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr=sdf.format(start);
		String endDateStr=sdf.format(end);
		file=file.replace(" ", "%20");
		String url="http://sourceforge.net/projects/"+project+"/files/"+file+"/stats/json?start_date="+startDateStr+"&end_date="+endDateStr;
		//System.out.println("URL: "+url);
		return url;
	}

	/**
	 * Main method of tool.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		new MainLoadStats().doIt();
	}
}

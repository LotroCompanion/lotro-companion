package delta.games.lotro.quests.io.web;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.LotroLoggers;

/**
 * Converts internal LOTRO resource URLs to quest identifiers.
 * @author DAM
 */
public class MyLotroURL2Identifier
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String QUEST_URL_SEED="/wiki/Quest:";
  private static final String DEED_URL_SEED="/wiki/Deed:";

  /**
   * Find the identifier for the given URL.
   * @param urlStr URL to use.
   * @return An identifier or <code>null</code> if not found.
   */
  public String findIdentifier(String urlStr)
  {
    String identifier=null;
    if ((urlStr!=null) && (urlStr.length()>0))
    {
      HttpURLConnection conn=null;
      try
      {
        URL url=new URL(urlStr);
        conn=(HttpURLConnection)url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        int responseCode=conn.getResponseCode();
        if (responseCode==302)
        {
          String loc=conn.getHeaderField("Location");
          if (loc!=null)
          {
            byte[] b=loc.getBytes("ISO8859-1");
            loc=new String(b,"UTF-8");
            if (loc.startsWith(QUEST_URL_SEED))
            {
              identifier=loc.substring(QUEST_URL_SEED.length());
            }
            else if (loc.startsWith(DEED_URL_SEED))
            {
              identifier=loc.substring(DEED_URL_SEED.length());
            }
          }
        }
      }
      catch(Exception e)
      {
        _logger.error("",e);
      }
      finally
      {
        if (conn!=null)
        {
          conn.disconnect();
        }
        conn=null;
      }
    }
    return identifier;
  }

  /*
  public static void main(String[] args)
  {
    MyLotroURL2Identifier r=new MyLotroURL2Identifier();
    //String urlStr="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879202545";
    String urlStr="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879099041";
    String questId=r.findIdentifier(urlStr);
    System.out.println(questId);
  }
  */
}

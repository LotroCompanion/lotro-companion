package delta.games.lotro.lore.quests.io.web;

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

  private static final String WIKI_URL_SEED="/wiki/";
  private static final String WIKI_URL_SEPERATOR=":";

  /**
   * Find the identifier for the given URL.
   * @param urlStr URL to use.
   * @return An identifier or <code>null</code> if not found.
   */
  public String findIdentifier(String urlStr)
  {
    return findIdentifier(urlStr,false);
  }

  /**
   * Find the identifier for the given URL.
   * @param urlStr URL to use.
   * @param full Return full identifier (including URL type).
   * @return An identifier or <code>null</code> if not found.
   */
  public String findIdentifier(String urlStr, boolean full)
  {
    // TODO use downloader to centralize web I/O and allow proxy usage
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
            if (loc.startsWith(WIKI_URL_SEED))
            {
              int separatorIndex=loc.indexOf(WIKI_URL_SEPERATOR,WIKI_URL_SEED.length());
              if (separatorIndex!=-1)
              {
                if (full)
                {
                  identifier=loc.substring(WIKI_URL_SEED.length());
                }
                else
                {
                  identifier=loc.substring(separatorIndex+1);
                }
              }
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

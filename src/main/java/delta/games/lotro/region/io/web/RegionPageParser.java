package delta.games.lotro.region.io.web;

import java.net.URL;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com quest page.
 * @author DAM
 */
public class RegionPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private void parseQuestsTable(Element table)
  {
    // nothing yet!
  }

  /**
   * Parse the region page at the given URL.
   * @param url URL of region page.
   */
  public void parseRegionPage(String url)
  {
    try
    {
      Source source=new Source(new URL(url));
  
      //<div id="questsAndDeeds_wrapper" class="dataTables_wrapper">
      Element wrapper=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","questsAndDeeds_wrapper");
      if (wrapper!=null)
      {
        //<table id="questsAndDeeds" style="width: 902px;">
        Element table=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(wrapper,HTMLElementName.TABLE,"id","questsAndDeeds");
        if (table!=null)
        {
          parseQuestsTable(table);
        }
      }
    }
    catch(Exception e)
    {
      _logger.error("Cannot parse quest page ["+url+"]",e);
    }
  }
}

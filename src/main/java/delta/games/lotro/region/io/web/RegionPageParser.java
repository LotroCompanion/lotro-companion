package delta.games.lotro.region.io.web;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;

import delta.games.lotro.region.Area;
import delta.games.lotro.region.Region;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com region page.
 * @author DAM
 */
public class RegionPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String AREA_URL_SEED="/wiki/Area:";
  private static final String TITLE_SEED="Region:";

  private Region _region;

  private void parseArea(Element area)
  {
    /*
<div>
<img id="1879064022" class="territoryMarker" src="http://content.turbine.com/sites/google_map/search_result_icon.gif" title="Highlight on Map">
<a href="/wiki/Area:Bindbole_Wood">Bindbole Wood</a>
</div>
     */

    String areaName=JerichoHtmlUtils.getTagContents(area,HTMLElementName.A);
    Element firstA=area.getFirstElement(HTMLElementName.A);
    if (firstA!=null)
    {
      String url=firstA.getAttributeValue("href");
      if ((url!=null) && (url.startsWith(AREA_URL_SEED)))
      {
        String areaIdentifier=url.substring(AREA_URL_SEED.length()).trim();
        Area a=new Area(areaIdentifier,areaName);
        _region.addArea(a);
      }
      //System.out.println("Area: ["+areaName+"], URL=["+url+"]");
    }
  }

  private void parseAreas(Element regionAreas)
  {
    Element div=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(regionAreas,HTMLElementName.DIV,"style","padding: 10px;");
    if (div!=null)
    {
      List<Element> areas=div.getAllElements(HTMLElementName.DIV);
      for(Element area : areas)
      {
        parseArea(area);
      }
    }
  }

  /**
   * Parse a region page.
   * @param identifier Identifier of the region.
   * @return A region or <code>null</code> if an error occurred.
   */
  public Region parseRegionPage(String identifier)
  {
    Region ret=null;
    String url="http://lorebook.lotro.com/wiki/Region:"+identifier;
    try
    {
      Source source=new Source(new URL(url));
  
      //<div class="lorebooktitle">Region: The Shire</div>
      Element titleTag=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"class","lorebooktitle");
      String name="";
      if (titleTag!=null)
      {
        name=CharacterReference.decodeCollapseWhiteSpace(titleTag.getContent());
        if (name.startsWith(TITLE_SEED))
        {
          name=name.substring(TITLE_SEED.length()).trim();
        }
      }
      _region=new Region(identifier,name);
      //<div class="regionAreas widget ui-corner-all">
      Element regionAreas=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"class","regionAreas widget ui-corner-all");
      if (regionAreas!=null)
      {
        parseAreas(regionAreas);
      }
      ret=_region;
      _region=null;
    }
    catch(Exception e)
    {
      _logger.error("Cannot parse region page ["+url+"]",e);
    }
    return ret;
  }
}

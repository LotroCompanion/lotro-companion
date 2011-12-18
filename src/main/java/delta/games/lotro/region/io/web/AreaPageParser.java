package delta.games.lotro.region.io.web;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;

import delta.games.lotro.region.Area;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com area page.
 * @author DAM
 */
public class AreaPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String QUEST_URL_SEED="/wiki/Quest:";
  private static final String TITLE_SEED="Area:";

  private Area _area;

  private void parseQuestRow(Element questRow)
  {
    /*
    <tr>
    <td style="text-align: left;">
    <a href="/wiki/Quest:Bundle_for_Bywater">Bundle for Bywater</a>
    </td>
    <td>9</td>
    </tr>
     */

    //String questName=JerichoHtmlUtils.getTagContents(questRow,HTMLElementName.A);
    Element firstA=questRow.getFirstElement(HTMLElementName.A);
    if (firstA!=null)
    {
      String url=firstA.getAttributeValue("href");
      if ((url!=null) && (url.startsWith(QUEST_URL_SEED)))
      {
        String questIdentifier=url.substring(QUEST_URL_SEED.length()).trim();
        _area.addQuest(questIdentifier);
      }
      //System.out.println("Quest: ["+questName+"], URL=["+url+"]");
    }
  }

  private void parseQuests(Element questsTable)
  {
    List<Element> questRows=questsTable.getAllElements(HTMLElementName.TR);
    for(Element questRow : questRows)
    {
      parseQuestRow(questRow);
    }
  }

  /**
   * Parse the region page at the given URL.
   * @param identifier Identifier of the area.
   * @return An area or <code>null</code> if an error occurred.
   */
  public Area parseAreaPage(String identifier)
  {
    Area ret=null;
    String url="http://lorebook.lotro.com/wiki/Area:"+identifier;
    try
    {
      Source source=new Source(new URL(url));
  
      //<div class="lorebooktitle">Area: Bindbole Wood</div>
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
      _area=new Area(identifier,name);

      //<div id="regionQuests" 
      Element questsTable=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.TABLE,"id","region_quests_table");
      if (questsTable!=null)
      {
        parseQuests(questsTable);
      }
      ret=_area;
      _area=null;
    }
    catch(Exception e)
    {
      _logger.error("Cannot parse region page ["+url+"]",e);
    }
    return ret;
  }
}

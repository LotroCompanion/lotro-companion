package delta.games.lotro.gui.quests;

import java.io.File;

import delta.common.utils.files.TextFileWriter;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Tool to dump all quests and deeds as a HTML file.
 * @author DAM
 */
public class MainDumpHtml
{
  /**
   * Main method for this tool.
   * @param args Not used.
   * @throws Exception if an error occurs.
   */
  public static void main(String[] args) throws Exception
  {
    dumpQuests();
    dumpDeeds();
  }

  private static void dumpQuests()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    QuestsManager questsMgr=QuestsManager.getInstance();
    for(QuestDescription quest : questsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(quest);
      sb.append("<h3>").append(quest.getIdentifier()+" - "+quest.getName()).append("</h3>");
      sb.append("<b>Description</b><p>");
      sb.append(quest.getDescription());
      ObjectivesHtmlBuilder.buildHtml(sb,quest);
    }
    sb.append("</body></html>");
    TextFileWriter w=new TextFileWriter(new File("quests.html"));
    w.start();
    w.writeSomeText(sb.toString());
    w.terminate();
  }

  private static void dumpDeeds()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    DeedsManager deedsMgr=DeedsManager.getInstance();
    for(DeedDescription deed : deedsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(deed);
      sb.append("<h3>").append(deed.getIdentifier()+" - "+deed.getName()).append("</h3>");
      sb.append("<b>Description</b><p>");
      sb.append(deed.getDescription());
      ObjectivesHtmlBuilder.buildHtml(sb,deed);
    }
    sb.append("</body></html>");
    TextFileWriter w=new TextFileWriter(new File("deeds.html"));
    w.start();
    w.writeSomeText(sb.toString());
    w.terminate();
  }
}

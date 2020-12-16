package delta.games.lotro.gui.quests;

import java.io.File;

import delta.common.utils.files.TextFileWriter;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.utils.gui.HtmlUtils;

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
    System.out.println(ObjectivesDisplayBuilder._counters);
  }

  private static void dumpQuests()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    QuestsManager questsMgr=QuestsManager.getInstance();
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(true);
    for(QuestDescription quest : questsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(quest);
      sb.append("<h3>").append(quest.getIdentifier()+" - "+quest.getName()).append("</h3>");
      sb.append("<b>Description</b><p>");
      sb.append(HtmlUtils.toHtml(quest.getDescription()));
      sb.append(EndOfLine.NATIVE_EOL);
      builder.build(sb,quest);
      sb.append(EndOfLine.NATIVE_EOL);
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
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(true);
    for(DeedDescription deed : deedsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(deed);
      sb.append("<h3>").append(deed.getIdentifier()+" - "+deed.getName()).append("</h3>");
      sb.append("<b>Description</b><p>");
      sb.append(HtmlUtils.toHtml(deed.getDescription()));
      sb.append(EndOfLine.NATIVE_EOL);
      builder.build(sb,deed);
      sb.append(EndOfLine.NATIVE_EOL);
    }
    sb.append("</body></html>");
    TextFileWriter w=new TextFileWriter(new File("deeds.html"));
    w.start();
    w.writeSomeText(sb.toString());
    w.terminate();
  }
}

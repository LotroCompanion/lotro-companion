package delta.games.lotro.gui.lore.quests;

import java.io.File;
import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.common.utils.files.TextFileWriter;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.objectives.Objective;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesManager;
import delta.games.lotro.utils.gui.HtmlUtils;
import delta.games.lotro.utils.gui.TextSanitizer;
import delta.games.lotro.utils.strings.ContextRendering;

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
    dumpDeedsAsText();
    System.out.println(ObjectivesDisplayBuilder._counters);
  }

  private static void dumpQuests()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    QuestsManager questsMgr=QuestsManager.getInstance();
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(null,true);
    for(QuestDescription quest : questsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(quest);
      sb.append("<h3>").append(quest.getIdentifier()+" - "+quest.getName()).append("</h3>");
      sb.append("<b>Description</b><p>");
      String description=quest.getDescription();
      description=ContextRendering.render((AreaController)null,description);
      sb.append(HtmlUtils.toHtml(description));
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

  private static void dumpDeedsAsText()
  {
    StringBuilder sb=new StringBuilder();
    DeedsManager deedsMgr=DeedsManager.getInstance();
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(null,false);
    for(DeedDescription deed : deedsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(deed);
      sb.append(deed.getIdentifier()+" - "+deed.getName()).append(EndOfLine.NATIVE_EOL);
      //sb.append("Description: ").append(deed.getDescription()).append(EndOfLine.NATIVE_EOL);
      ObjectivesManager objectivesMgr=deed.getObjectives();
      List<Objective> objectives=objectivesMgr.getObjectives();
      for(Objective objective : objectives)
      {
        int index=objective.getIndex();
        sb.append("Objective #").append(index).append(": ");
        String text=objective.getDescription();
        text=ContextRendering.render((AreaController)null,text);
        if (text.length()>0)
        {
          sb.append(TextSanitizer.removeColorHints(text));
        }
        sb.append(EndOfLine.NATIVE_EOL);
        // Conditions
        int conditionIndex=1;
        for(ObjectiveCondition condition : objective.getConditions())
        {
          sb.append("\tCondition #").append(conditionIndex).append(": ");
          sb.append(builder.getConditionDisplay(condition,true));
          sb.append(EndOfLine.NATIVE_EOL);
          conditionIndex++;
        }
      }
    }
    TextFileWriter w=new TextFileWriter(new File("deeds.txt"));
    w.start();
    w.writeSomeText(sb.toString());
    w.terminate();
  }

  private static void dumpDeeds()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    DeedsManager deedsMgr=DeedsManager.getInstance();
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(null,true);
    for(DeedDescription deed : deedsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(deed);
      sb.append("<h3>").append(deed.getIdentifier()+" - "+deed.getName()).append("</h3>");
      sb.append("<b>Description</b><p>");
      String description=deed.getDescription();
      description=ContextRendering.render((AreaController)null,description);
      sb.append(HtmlUtils.toHtml(description));
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

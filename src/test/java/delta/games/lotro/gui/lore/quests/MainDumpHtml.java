package delta.games.lotro.gui.lore.quests;

import java.io.File;
import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.common.utils.files.TextFileWriter;
import delta.common.utils.text.EndOfLine;
import delta.common.utils.variables.VariablesResolver;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.objectives.Objective;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesDisplayBuilder;
import delta.games.lotro.lore.quests.objectives.ObjectivesManager;
import delta.games.lotro.utils.html.HtmlOutput;
import delta.games.lotro.utils.html.NavigatorLinkGenerator;
import delta.games.lotro.utils.strings.ContextRendering;
import delta.games.lotro.utils.strings.GenericOutput;
import delta.games.lotro.utils.strings.StringRendering;
import delta.games.lotro.utils.strings.TextOutput;
import delta.games.lotro.utils.strings.TextSanitizer;

/**
 * Tool to dump all quests and deeds as a HTML file.
 * @author DAM
 */
public class MainDumpHtml
{
  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    dumpQuests();
    dumpDeedsAsText();
  }

  private static void dumpQuests()
  {
    StringBuilder sb=new StringBuilder();
    GenericOutput output=new HtmlOutput(new NavigatorLinkGenerator());
    output.startDocument(sb);
    output.startBody(sb);
    QuestsManager questsMgr=QuestsManager.getInstance();
    VariablesResolver resolver=ContextRendering.buildRenderer(null);
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(resolver,output);
    for(QuestDescription quest : questsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(quest);
      output.startTitle(sb,3);
      output.printText(sb,quest.getIdentifier()+" - "+quest.getName());
      output.endTitle(sb,3);
      output.startBold(sb);
      output.printText(sb,"Description");
      output.endBold(sb);
      output.startParagraph(sb);
      String description=quest.getDescription();
      description=StringRendering.render(resolver,description);
      output.printText(sb,description);
      sb.append(EndOfLine.NATIVE_EOL);
      builder.build(sb,quest);
      sb.append(EndOfLine.NATIVE_EOL);
    }
    output.endBody(sb);
    output.endDocument(sb);
    TextFileWriter w=new TextFileWriter(new File("quests.html"));
    w.start();
    w.writeSomeText(sb.toString());
    w.terminate();
  }

  private static void dumpDeedsAsText()
  {
    StringBuilder sb=new StringBuilder();
    DeedsManager deedsMgr=DeedsManager.getInstance();
    VariablesResolver resolver=ContextRendering.buildRenderer(null);
    ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(resolver,new TextOutput());
    for(DeedDescription deed : deedsMgr.getAll())
    {
      AchievableProxiesResolver.resolve(deed);
      sb.append(deed.getIdentifier()+" - "+deed.getName()).append(EndOfLine.NATIVE_EOL);
      ObjectivesManager objectivesMgr=deed.getObjectives();
      List<Objective> objectives=objectivesMgr.getObjectives();
      for(Objective objective : objectives)
      {
        int index=objective.getIndex();
        sb.append("Objective #").append(index).append(": ");
        String text=objective.getDescription();
        text=ContextRendering.render((AreaController)null,text);
        if (!text.isEmpty())
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
}

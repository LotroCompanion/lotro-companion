package delta.games.lotro.utils.strings;

import java.io.File;

import delta.common.utils.i18n.SingleLocaleLabelsManager;
import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.dat.data.strings.renderer.StringRenderer;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.i18n.I18nFacade;

/**
 * Test class for string rendering.
 * @author DAM
 */
public class MainTestStringRendering
{
  private StringRenderer buildRenderer()
  {
    ContextVariableValueProvider p=new ContextVariableValueProvider();
    CharacterFile toon=new LotroTestUtils().getToonByName("Meva");
    CharacterSummary summary=toon.getSummary();
    p.setup(summary);
    StringRenderer r=new StringRenderer(p);
    return r;
  }

  private void doIt()
  {
    StringRenderer r= buildRenderer();
    //renderTitles(r);
    //renderVocations(r);
    //renderDeeds(r);
    //renderQuests(r);
    doAllLabels(r);
  }

  void renderTitles(StringRenderer r)
  {
    for(TitleDescription title : TitlesManager.getInstance().getAll())
    {
      String name=title.getRawName();
      String renderedTitle=r.render(name);
      System.out.println("["+renderedTitle+"]");
    }
  }

  void renderVocations(StringRenderer r)
  {
    for(Vocation vocation : CraftingSystem.getInstance().getData().getVocationsRegistry().getAll())
    {
      String name=vocation.getName();
      String rendered=r.render(name);
      System.out.println("["+rendered+"]");
    }
  }

  void renderDeeds(StringRenderer r)
  {
    for(DeedDescription deed : DeedsManager.getInstance().getAll())
    {
      String name=deed.getRawName();
      String rendered=r.render(name);
      System.out.println("["+rendered+"]");
    }
  }

  void renderQuests(StringRenderer r)
  {
    for(QuestDescription quest : QuestsManager.getInstance().getAll())
    {
      String name=quest.getRawName();
      String rendered=r.render(name);
      System.out.println("["+rendered+"]");
    }
  }

  private void doAllLabels(StringRenderer r)
  {
    File root=new File("../lotro-data/labels/fr");
    for(String labelsFile : root.list())
    {
      String group=labelsFile.substring(0,labelsFile.length()-4);
      System.out.println("Doing: "+group);
      SingleLocaleLabelsManager mgr=I18nFacade.getLabelsMgr(group);
      for(String key : mgr.getKeys())
      {
        String value=mgr.getLabel(key);
        if (value.contains("${"))
        {
          render(r,value);
        }
      }
    }
  }

  private void render(StringRenderer r, String value)
  {
    String rendered=r.render(value);
    if (rendered.indexOf('{')!=-1)
    {
      System.out.println("Warning: format="+value);
      System.out.println("Warning: rendered="+rendered);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestStringRendering().doIt();
  }
}

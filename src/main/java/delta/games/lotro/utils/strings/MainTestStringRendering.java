package delta.games.lotro.utils.strings;

import java.io.File;

import delta.common.utils.i18n.SingleLocaleLabelsManager;
import delta.games.lotro.dat.data.strings.renderer.StringRenderer;
import delta.games.lotro.dat.data.strings.renderer.VariableValueProvider;
import delta.games.lotro.utils.i18n.I18nFacade;

/**
 * Test class for string rendering.
 * @author DAM
 */
public class MainTestStringRendering
{
  private void doIt()
  {
    VariableValueProvider p=new VariableValueProvider()
    {
      @Override
      public String getVariable(String variableName)
      {
        if ("NAME".equals(variableName)) return "Meva[f]";
        if ("RANK".equals(variableName)) return "";
        if ("SURNAME".equals(variableName)) return "Funnyone";
        if ("CLASS".equals(variableName)) return "Minstrel";
        if ("RACE".equals(variableName)) return "Hobbit";
        if ("PLAYER".equals(variableName)) return "Meva";
        if ("PLAYER_NAME".equals(variableName)) return "Meva";
        if ("NUMBER".equals(variableName)) return "n";
        if ("TOTAL".equals(variableName)) return "t";
        if ("MAX".equals(variableName)) return "m";
        if ("CURRENT".equals(variableName)) return "c";
        if ("VALUE".equals(variableName)) return "v";
        if ("NOS".equals(variableName)) return "?";
        System.out.println("Unsupported variable: "+variableName);
        return variableName;
      }
    };
    StringRenderer r=new StringRenderer(p);
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
          r.render(value);
        }
      }
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

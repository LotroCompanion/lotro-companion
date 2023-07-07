package delta.games.lotro.utils.strings;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.area.AreaUtils;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.context.Context;
import delta.common.utils.context.ContextUtils;
import delta.games.lotro.Config;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.Genders;
import delta.games.lotro.dat.data.strings.renderer.StringRenderer;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * String rendering using character context.
 * @author DAM
 */
public class ContextRendering
{
  private static final BaseCharacterSummary DEFAULT_SUMMARY=buildDefaultSummary();

  private static BaseCharacterSummary buildDefaultSummary()
  {
    BaseCharacterSummary ret=new BaseCharacterSummary();
    // Class
    ClassDescription characterClass=ClassesManager.getInstance().getCharacterClassByKey(WellKnownCharacterClassKeys.CHAMPION);
    ret.setCharacterClass(characterClass);
    // Race
    RaceDescription race=RacesManager.getInstance().getByKey("man");
    ret.setRace(race);
    // Gender
    ret.setCharacterSex(Genders.MALE);
    // Level
    int maxLevel=Config.getInstance().getMaxCharacterLevel();
    ret.setLevel(maxLevel);
    // Name
    ret.setName("(character name)"); // I18n
    return ret;
  }

  /**
   * Render a given string using the given character summary.
   * @param areaController Current window controller.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  public static String render(AreaController areaController, String rawFormat)
  {
    BaseCharacterSummary summary=getSummaryFromContext(areaController);
    return render(summary,rawFormat);
  }

  /**
   * Render a given string using the given context.
   * @param context Context to use.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  public static String render(Context context, String rawFormat)
  {
    BaseCharacterSummary summary=getSummaryFromContext(context);
    return render(summary,rawFormat);
  }

  private static BaseCharacterSummary getSummaryFromContext(AreaController areaController)
  {
    Context context=null;
    WindowController parentController=AreaUtils.findParentWindowController(areaController);
    if (parentController!=null)
    {
      context=parentController.getContext();
    }
    return getSummaryFromContext(context);
  }
  
  private static BaseCharacterSummary getSummaryFromContext(Context context)
  {
    BaseCharacterSummary ret=DEFAULT_SUMMARY;
    if (context!=null)
    {
      BaseCharacterSummary summary=ContextUtils.getValue(context,ContextPropertyNames.BASE_CHARACTER_SUMMARY,BaseCharacterSummary.class);
      if (summary!=null)
      {
        ret=summary;
      }
    }
    return ret;
  }

  /**
   * Render a given string using the given character summary.
   * @param summary Character summary.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  public static String render(BaseCharacterSummary summary, String rawFormat)
  {
    if (rawFormat==null)
    {
      return null;
    }
    if (rawFormat.indexOf("${")==-1)
    {
      return rawFormat;
    }
    ContextVariableValueProvider provider=new ContextVariableValueProvider();
    provider.setup(summary);
    StringRenderer renderer=new StringRenderer(provider);
    String ret=renderer.render(rawFormat);
    ret=ret.replace(" ,",",");
    ret=ret.replace("  "," ");
    ret=ret.trim();
    return ret;
  }
}

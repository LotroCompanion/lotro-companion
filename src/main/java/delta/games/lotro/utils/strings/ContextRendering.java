package delta.games.lotro.utils.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.area.AreaUtils;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.context.Context;
import delta.common.utils.context.ContextUtils;
import delta.common.utils.variables.VariablesResolver;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.dat.data.strings.renderer.StringRenderer;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * String rendering using character context.
 * @author DAM
 */
public class ContextRendering
{
  private static final BaseCharacterSummary DEFAULT_SUMMARY=RenderingUtils.buildDefaultSummary();

  /**
   * Render a given string using the given character summary.
   * @param areaController Current window controller.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  public static String render(AreaController areaController, String rawFormat)
  {
    Context context=getContext(areaController);
    return render(context,rawFormat);
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

  /**
   * Get the context from the given area controller.
   * @param areaController Area controller.
   * @return A context or <code>null</code> if not found.
   */
  public static Context getContext(AreaController areaController)
  {
    Context context=null;
    WindowController parentController=AreaUtils.findWindowController(areaController);
    if (parentController!=null)
    {
      context=parentController.getContext();
    }
    return context;
  }

  /**
   * Initialize a rendering context using the current UI controller.
   * @param areaController UI controller.
   * @return A rendering context.
   */
  public static Map<String,String> initContext(AreaController areaController)
  {
    Context context=getContext(areaController);
    BaseCharacterSummary summary=getSummaryFromContext(context);
    return RenderingUtils.setupContext(summary);
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
    Map<String,String> map=RenderingUtils.setupContext(summary);
    return renderCustomContext(map,rawFormat);
  }

  /**
   * Build a renderer using the given context.
   * @param areaController Context.
   * @return A renderer.
   */
  public static VariablesResolver buildRenderer(AreaController areaController)
  {
    Map<String,String> newContext=initContext(areaController);
    ContextVariableValueProvider provider=new ContextVariableValueProvider(newContext);
    StringRenderer renderer=new StringRenderer(provider);
    return renderer.getResolver();
  }

  /**
   * Render a given string using the given context.
   * @param context Context to use.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  public static String renderCustomContext(Map<String,String> context, String rawFormat)
  {
    if (rawFormat==null)
    {
      return null;
    }
    if (rawFormat.indexOf("${")==-1)
    {
      return rawFormat;
    }
    // Render context elements (for RANK)
    List<String> keys=new ArrayList<String>(context.keySet());
    for(String key : keys)
    {
      String value=context.get(key);
      value=unsafeRenderCustomContext(context,value);
      context.put(key,value);
    }
    ContextVariableValueProvider provider=new ContextVariableValueProvider(context);
    StringRenderer renderer=new StringRenderer(provider);
    String ret=renderer.render(rawFormat);
    ret=ret.replace(" ,",",");
    ret=ret.replace("  "," ");
    ret=ret.trim();
    return ret;
  }

  /**
   * Render a given string using the given context.
   * @param context Context to use.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  private static String unsafeRenderCustomContext(Map<String,String> context, String rawFormat)
  {
    if (rawFormat==null)
    {
      return null;
    }
    if (rawFormat.indexOf("${")==-1)
    {
      return rawFormat;
    }
    ContextVariableValueProvider provider=new ContextVariableValueProvider(context);
    StringRenderer renderer=new StringRenderer(provider);
    String ret=renderer.render(rawFormat);
    ret=ret.replace(" ,",",");
    ret=ret.replace("  "," ");
    ret=ret.trim();
    return ret;
  }
}

package delta.games.lotro.gui.lore.titles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.context.Context;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.common.rewards.RewardsUiUtils;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.strings.ContextRendering;
import delta.games.lotro.utils.strings.RenderingUtils;

/**
 * Utility methods for title-related UIs.
 * @author DAM
 */
public class TitleUiUtils
{
  /**
   * Build a combo-box controller to choose a title category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=TitlesManager.getInstance().getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a title link controller.
   * @param parent Parent window.
   * @param title Title to use.
   * @param label Label to use.
   * @return a new controller.
   */
  public static HyperLinkController buildTitleLink(final WindowController parent, final TitleDescription title, JLabel label)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showTitleWindow(parent,title.getIdentifier());
      }
    };
    String text="???";
    if (title!=null)
    {
      text=renderTitle(parent,title,TitleRenderingFormat.MINIMAL);
    }
    text=RewardsUiUtils.getDisplayedTitle(text);
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action,label);
    return controller;
  }

  /**
   * Show a title display window.
   * @param parent Parent window.
   * @param titleID Title identifier.
   */
  public static void showTitleWindow(WindowController parent, int titleID)
  {
    PageIdentifier ref=ReferenceConstants.getTitleReference(titleID);
    NavigationUtils.navigateTo(ref,parent);
  }

  /**
   * Available formats to render titles.
   * @author DAM
   */
  public enum TitleRenderingFormat
  {
    /**
     * Includes rank, surname and handles gender.
     */
    FULL,
    /**
     * No rank, no surname. Handles gender.
     */
    SHORT,
    /**
     * Minimal (no player name). Handles gender.
     */
    MINIMAL
  }

  /**
   * Render a title.
   * @param areaController Context.
   * @param title Title to use.
   * @param format Format to use.
   * @return the rendered title.
   */
  public static String renderTitle(AreaController areaController, TitleDescription title, TitleRenderingFormat format)
  {
    Map<String,String> context=ContextRendering.initContext(areaController);
    return renderTitle(context,title,format);
  }

  /**
   * Render a title.
   * @param summary Character summary.
   * @param title Title to use.
   * @param format Format to use.
   * @return the rendered title.
   */
  public static String renderTitle(BaseCharacterSummary summary, TitleDescription title, TitleRenderingFormat format)
  {
    Map<String,String> context=RenderingUtils.setupContext(summary);
    return renderTitle(context,title,format);
  }

  /**
   * Render a title.
   * @param uiContext Context.
   * @param title Title to use.
   * @param format Format to use.
   * @return the rendered title.
   */
  public static String renderTitle(Context uiContext, TitleDescription title, TitleRenderingFormat format)
  {
    Map<String,String> context=ContextRendering.initContext(uiContext);
    return renderTitle(context,title,format);
  }

  private static String renderTitle(Map<String,String> context, TitleDescription title, TitleRenderingFormat format)
  {
    if (format!=TitleRenderingFormat.FULL)
    {
      context.put("RANK","");
      context.put("SURNAME","");
      if (format==TitleRenderingFormat.MINIMAL)
      {
        String name=context.get("NAME");
        context.put("NAME",name.substring(name.indexOf('[')));
      }
    }
    String renderedTitle=ContextRendering.renderCustomContext(context,title.getRawName()).trim();
    renderedTitle=renderedTitle.replace('*',' ').trim();
    if (renderedTitle.startsWith("+"))
    {
      renderedTitle=renderedTitle.substring(1).trim();
    }
    if (renderedTitle.startsWith(","))
    {
      renderedTitle=renderedTitle.substring(1).trim();
    }
    if (!renderedTitle.isEmpty())
    {
      renderedTitle=renderedTitle.substring(0,1).toUpperCase()+renderedTitle.substring(1);
    }
    return renderedTitle;
  }
}

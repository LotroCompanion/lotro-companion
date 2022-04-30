package delta.games.lotro.gui.lore.titles;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.titles.TitlesManager;

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
   * Show a title display window.
   * @param parent Parent window.
   * @param titleID Title identifier.
   */
  public static void showTitleWindow(WindowController parent, int titleID)
  {
    WindowsManager windowsMgr=parent.getWindowsManager();
    int id=windowsMgr.getAll().size();
    NavigatorWindowController window=NavigatorFactory.buildNavigator(parent,id);
    PageIdentifier ref=ReferenceConstants.getTitleReference(titleID);
    window.navigateTo(ref);
    window.show(false);
    windowsMgr.registerWindow(window);
  }
}

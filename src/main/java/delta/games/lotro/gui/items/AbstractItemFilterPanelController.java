package delta.games.lotro.gui.items;

import javax.swing.JPanel;

/**
 * Base class for item filter panel UI controller.
 * @author DAM
 */
public abstract class AbstractItemFilterPanelController
{
  private ItemChoicePanelController _choiceController;

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public abstract JPanel getPanel();

  /**
   * Set the associated item choice controller.
   * @param choiceController Associated item choice controller.
   */
  public void setChoicePanel(ItemChoicePanelController choiceController)
  {
    _choiceController=choiceController;
  }

  protected void updateFilter()
  {
    _choiceController.updateFilter();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _choiceController=null;
  }
}

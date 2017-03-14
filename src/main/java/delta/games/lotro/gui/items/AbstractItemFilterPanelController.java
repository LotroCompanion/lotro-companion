package delta.games.lotro.gui.items;

import javax.swing.JPanel;

/**
 * Base class for item filter panel UI controller.
 * @author DAM
 */
public abstract class AbstractItemFilterPanelController
{
  private FilterUpdateListener _filterListener;

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public abstract JPanel getPanel();

  /**
   * Set the filter update listener.
   * @param filterListener Associated filter listener.
   */
  public void setFilterUpdateListener(FilterUpdateListener filterListener)
  {
    _filterListener=filterListener;
  }

  protected void filterUpdated()
  {
    if (_filterListener!=null)
    {
      _filterListener.filterUpdated();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _filterListener=null;
  }
}

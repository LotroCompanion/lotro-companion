package delta.games.lotro.utils.gui.filter;

import javax.swing.JPanel;

import delta.common.ui.swing.tables.panel.FilterUpdateListener;

/**
 * Base class for item filter panel UI controller.
 * @author DAM
 */
public abstract class ObjectFilterPanelController
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

  /**
   * Invoked when the managed filter has been updated.
   */
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

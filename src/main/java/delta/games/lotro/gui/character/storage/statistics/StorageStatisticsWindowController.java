package delta.games.lotro.gui.character.storage.statistics;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.statistics.StorageStatistics;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller for a "tasks statistics" window.
 * @author DAM
 */
public class StorageStatisticsWindowController extends DefaultDialogController implements FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="STORAGE_STATISTICS";

  // Data
  private StorageStatistics _statistics;
  private StorageFilter _filter;
  private List<StoredItem> _storedItems;
  // Controllers
  private StorageStatisticsPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param filter Filter to use.
   * @param storedItems Stored items.
   */
  public StorageStatisticsWindowController(WindowController parent, StorageFilter filter, List<StoredItem> storedItems)
  {
    super(parent);
    _statistics=new StorageStatistics();
    _filter=filter;
    _storedItems=storedItems;
    _panelController=new StorageStatisticsPanelController(this,_statistics);
    updateStats(storedItems);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel summaryPanel=_panelController.getPanel();
    return summaryPanel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(700,700);
    dialog.setResizable(true);
    dialog.setTitle("Storage Statistics");
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void filterUpdated()
  {
    List<StoredItem> toUse=new ArrayList<StoredItem>();
    for(StoredItem item : _storedItems)
    {
      if ((_filter!=null) && (_filter.accept(item)))
      {
        toUse.add(item);
      }
    }
    updateStats(toUse);
  }

  /**
   * Update statistics.
   * @param storedItems Items to use.
   */
  private void updateStats(List<StoredItem> storedItems)
  {
    _panelController.updateStats(storedItems);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Data
    _statistics=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    super.dispose();
  }
}

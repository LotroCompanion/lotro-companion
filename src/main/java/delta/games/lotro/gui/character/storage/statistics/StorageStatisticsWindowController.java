package delta.games.lotro.gui.character.storage.statistics;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.statistics.StorageStatistics;

/**
 * Controller for a "tasks statistics" window.
 * @author DAM
 */
public class StorageStatisticsWindowController extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="STORAGE_STATISTICS";

  // Data
  private StorageStatistics _statistics;
  // Controllers
  private StorageStatisticsPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param storedItems Stored items.
   */
  public StorageStatisticsWindowController(WindowController parent, List<StoredItem> storedItems)
  {
    super(parent);
    _statistics=new StorageStatistics();
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

  /**
   * Update statistics.
   * @param storedItems Items to use.
   */
  public void updateStats(List<StoredItem> storedItems)
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

package delta.games.lotro.gui.character.storage.own;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.gui.character.storage.StorageDisplayPanelController;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.character.storage.StorageFilterController;
import delta.games.lotro.gui.character.storage.StorageUtils;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "character storage" window.
 * @author DAM
 */
public class CharacterStorageDisplayWindowController extends DefaultDialogController implements GenericEventsListener<CharacterEvent>
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="CHARACTER_STORAGE";

  // Data
  private CharacterFile _toon;
  private StorageFilter _filter;
  // Controllers
  private StorageSummaryPanelController _summaryController;
  private StorageDisplayPanelController _panelController;
  private DetailedStorageAccessPanelController _detailedAccessController;
  private StorageFilterController _filterController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Character file.
   */
  public CharacterStorageDisplayWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    _toon=toon;
    _filter=new StorageFilter();
    updateContents();
    EventsManager.addListener(CharacterEvent.class,this);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary
    _summaryController=new StorageSummaryPanelController();
    // Display
    _panelController=new StorageDisplayPanelController(this,_filter);
    // Details
    _detailedAccessController=new DetailedStorageAccessPanelController(this,_toon);
    // Table
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new StorageFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_summaryController.getPanel(),c);
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(0,2,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    c=new GridBagConstraints(2,0,1,3,0,1,GridBagConstraints.NORTHWEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    panel.add(_detailedAccessController.getPanel(),c);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(700,350));
    dialog.setSize(750,700);
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_STORAGE_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      if (toon==_toon)
      {
        updateContents();
      }
    }
  }

  /**
   * Update contents.
   */
  private void updateContents()
  {
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Storage for "+name+" @ "+serverName;
    getDialog().setTitle(title);
    // Update storage
    CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(_toon);
    _summaryController.update(characterStorage);
    List<StoredItem> items=StorageUtils.buildCharacterItems(_toon,characterStorage);
    _panelController.update(items);
    _filterController.update();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    EventsManager.removeListener(CharacterEvent.class,this);
    // Data
    _toon=null;
    _filter=null;
    // Controllers
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_detailedAccessController!=null)
    {
      _detailedAccessController.dispose();
      _detailedAccessController=null;
    }
    super.dispose();
  }
}

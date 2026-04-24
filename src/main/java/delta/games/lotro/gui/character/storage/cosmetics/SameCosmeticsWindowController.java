package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;
import delta.games.lotro.character.storage.cosmetics.SameCosmeticsFinder;
import delta.games.lotro.gui.character.storage.StorageFilterController;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a window that shows a collection of 'same cosmetics' groups.
 * @author DAM
 */
public class SameCosmeticsWindowController extends DefaultDialogController implements FilterUpdateListener
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="SAME_COSMETICS_WINDOW";

  // Controllers
  private StorageFilterController _filterController;
  private SameCosmeticsPanelController _groupsPanelCtrl;
  // Data
  private SameCosmeticsTableRowFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SameCosmeticsWindowController(WindowController parent)
  {
    super(parent);
    _filter=new SameCosmeticsTableRowFilter();
    _groupsPanelCtrl=new SameCosmeticsPanelController(this,_filter);
    _filterController=new StorageFilterController(_filter.getStorageFilter(),this);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle(Labels.getLabel("sameCosmetics.window.title"));
    dialog.pack();
    int width=dialog.getWidth()+20;
    dialog.setSize(width,INITIAL_HEIGHT);
    dialog.setMinimumSize(new Dimension(width,MIN_HEIGHT));
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Update display.
   * @param storedItems Items to use.
   */
  public void updateDisplay(List<StoredItem> storedItems)
  {
    SameCosmeticsFinder finder=new SameCosmeticsFinder();
    List<CosmeticItemsGroup> groups=finder.findGroups(storedItems);
    _groupsPanelCtrl.updateDisplay(groups);
    List<SameCosmeticsTableRow> rows=_groupsPanelCtrl.getRows();
    List<StoredItem> sameCosmeticsItems=_groupsPanelCtrl.getStoredItems();
    _filterController.update(sameCosmeticsItems);
    _filter.setRows(rows);
    filterUpdated();
  }

  @Override
  public void filterUpdated()
  {
    _filter.updateFilter();
    _groupsPanelCtrl.filterUpdated();
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    JPanel groupsPanel=_groupsPanelCtrl.getPanel();
    // Filter
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder(Labels.getLabel("shared.title.filter"));
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(groupsPanel,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Data
    _filter=null;
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_groupsPanelCtrl!=null)
    {
      _groupsPanelCtrl.dispose();
      _groupsPanelCtrl=null;
    }
  }
}

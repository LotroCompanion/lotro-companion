package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;
import delta.games.lotro.character.storage.cosmetics.SameCosmeticsFinder;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

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

  // Data
  private StorageFilter _filter;
  private List<StoredItem> _storedItems;
  // Controllers
  private SameCosmeticsPanelController _groupsPanelCtrl;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param filter Filter to use.
   * @param storedItems Stored items.
   */
  public SameCosmeticsWindowController(WindowController parent, StorageFilter filter, List<StoredItem> storedItems)
  {
    super(parent);
    _filter=filter;
    _storedItems=storedItems;
    _groupsPanelCtrl=new SameCosmeticsPanelController(this);
    filterUpdated();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Same cosmetics");
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

  @Override
  public void filterUpdated()
  {
    List<StoredItem> toUse=new ArrayList<StoredItem>();
    for(StoredItem item : _storedItems)
    {
      if ((_filter==null) || (_filter.accept(item)))
      {
        toUse.add(item);
      }
    }
    SameCosmeticsFinder finder=new SameCosmeticsFinder();
    List<CosmeticItemsGroup> groups=finder.findGroups(toUse);
    _groupsPanelCtrl.updateDisplay(groups);
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel groupsPanel=_groupsPanelCtrl.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(groupsPanel);
    panel.add(scroll,BorderLayout.CENTER);
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
    // Controllers
    if (_groupsPanelCtrl!=null)
    {
      _groupsPanelCtrl.dispose();
      _groupsPanelCtrl=null;
    }
  }
}

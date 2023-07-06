package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

/**
 * Controller for a window that shows a collection of 'same cosmetics' groups.
 * @author DAM
 */
public class SameCosmeticsWindowController extends DefaultDialogController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="SAME_COSMETICS_WINDOW";

  // Controllers
  private SameCosmeticsPanelController _groupsPanelCtrl;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SameCosmeticsWindowController(WindowController parent)
  {
    super(parent);
    _groupsPanelCtrl=new SameCosmeticsPanelController(this);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Same cosmetics"); // I18n
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

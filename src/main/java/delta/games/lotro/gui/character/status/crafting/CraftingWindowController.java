package delta.games.lotro.gui.character.status.crafting;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a "crafting stats" window.
 * @author DAM
 */
public class CraftingWindowController extends DefaultFormDialogController<CraftingStatus>
{
  // Data
  private CharacterFile _toon;
  // Controllers
  private CraftingEditionPanelController _editionController;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public CraftingWindowController(WindowController parentController, CharacterFile toon)
  {
    super(parentController,toon.getCraftingMgr().getCraftingStatus());
    _toon=toon;
    _editionController=new CraftingEditionPanelController(this,_data);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Professions edition panel
    JPanel editionPanel=_editionController.getPanel();

    // Assembly
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(editionPanel,c2);

    // Update vocation edition panel
    _editionController.updateUiFromData();

    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Crafting history editor for "+name+" @ "+serverName; // I18n
    dialog.setTitle(title);
    // Minimum size
    dialog.setMinimumSize(new Dimension(500,380));
    return dialog;
  }

  @Override
  protected void okImpl()
  {
    _editionController.updateDataFromUi();
    _toon.getCraftingMgr().saveCrafting();
    // Broadcast crafting update event...
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_CRAFTING_UPDATED,_toon,null);
    EventsManager.invokeEvent(event);
  }

  @Override
  protected void cancelImpl()
  {
    _toon.getCraftingMgr().revertCrafting();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_editionController!=null)
    {
      _editionController.dispose();
      _editionController=null;
    }
    _toon=null;
  }
}

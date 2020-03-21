package delta.games.lotro.gui.stats.crafting;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.crafting.VocationComparator;
import delta.games.lotro.lore.crafting.Vocations;
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
  private ComboBoxController<Vocation> _vocation;
  private VocationEditionPanelController _vocationController;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public CraftingWindowController(WindowController parentController, CharacterFile toon)
  {
    super(parentController,toon.getCraftingMgr().getCraftingStatus());
    _toon=toon;
    _vocationController=new VocationEditionPanelController(_data);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Vocation panel
    JPanel vocationPanel=GuiFactory.buildPanel(new FlowLayout());
    {
      _vocation=buildVocationCombo();
      JLabel vocationLabel=GuiFactory.buildLabel("Vocation:");
      vocationPanel.add(vocationLabel);
      vocationPanel.add(_vocation.getComboBox());
    }

    // Vocation panel
    JPanel vocationEditionPanel=_vocationController.getPanel();

    // Assembly
    JPanel topPanel=GuiFactory.buildPanel(new FlowLayout());
    topPanel.add(vocationPanel);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,10,0,10),0,0);
    panel.add(topPanel,c);
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(vocationEditionPanel,c2);

    // Update vocation edition panel
    _vocationController.updateUiFromData();
    Vocation vocation=_data.getVocation();
    // Init vocation combo
    _vocation.selectItem(vocation);
    ItemSelectionListener<Vocation> vocationListener=new ItemSelectionListener<Vocation>()
    {
      @Override
      public void itemSelected(Vocation selectedVocation)
      {
        handleVocationUpdate(selectedVocation);
      }
    };
    _vocation.addListener(vocationListener);

    return panel;
  }

  private void handleVocationUpdate(Vocation selectedVocation)
  {
   boolean changed=updateVocation(selectedVocation);
    if (changed)
    {
      updateVocationPanel(selectedVocation);
    }
  }

  private boolean updateVocation(Vocation selectedVocation)
  {
    boolean changed=false;
    Vocation currentVocation=_data.getVocation();
    if (currentVocation!=selectedVocation)
    {
      long now=System.currentTimeMillis();
      _data.changeVocation(selectedVocation,now);
      changed=true;
    }
    return changed;
  }

  private void updateVocationPanel(Vocation selectedVocation)
  {
    _vocationController.updateDataFromUi();
    _vocationController.updateUiFromData();
    getWindow().pack();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Crafting history editor for "+name+" @ "+serverName;
    dialog.setTitle(title);
    // Minimum size
    dialog.setMinimumSize(new Dimension(500,380));
    return dialog;
  }

  @Override
  protected void okImpl()
  {
    _vocationController.updateDataFromUi();
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

  private ComboBoxController<Vocation> buildVocationCombo()
  {
    ComboBoxController<Vocation> ret=new ComboBoxController<Vocation>();
    ret.addEmptyItem("");
    CraftingData crafting=CraftingSystem.getInstance().getData();
    Vocations vocationsRegistry=crafting.getVocationsRegistry();
    List<Vocation> vocations=vocationsRegistry.getAll();
    Collections.sort(vocations,new VocationComparator());
    for(Vocation vocation : vocations)
    {
      ret.addItem(vocation,vocation.getName());
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_vocationController!=null)
    {
      _vocationController.dispose();
      _vocationController=null;
    }
    if (_vocation!=null)
    {
      _vocation.dispose();
      _vocation=null;
    }
    _toon=null;
  }
}

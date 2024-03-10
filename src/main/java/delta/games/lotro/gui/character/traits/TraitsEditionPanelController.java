package delta.games.lotro.gui.character.traits;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.character.status.traits.TraitsStatus;
import delta.games.lotro.character.status.traits.shared.TraitSlotsStatus;
import delta.games.lotro.gui.character.status.traits.racial.RacialTraitsEditionDialogController;
import delta.games.lotro.gui.character.traitTree.TraitTreeEditionDialog;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a panel to edit traits.
 * @author DAM
 */
public class TraitsEditionPanelController
{
  // Data
  private CharacterData _toon;
  // Controllers
  private WindowController _parentWindow;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param character Targeted character.
   */
  public TraitsEditionPanelController(WindowController parent, CharacterData character)
  {
    _parentWindow=parent;
    _toon=character;
    build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void build()
  {
    _panel=GuiFactory.buildPanel(new FlowLayout());
    // Trait tree
    JButton traitTreeButton=GuiFactory.buildButton("Trait tree..."); // I18n
    _panel.add(traitTreeButton);
    ActionListener alTraitTree=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editTraitTree();
      }
    };
    traitTreeButton.addActionListener(alTraitTree);
    // Racial traits
    JButton racialTraitsButton=GuiFactory.buildButton("Racial..."); // I18n
    _panel.add(racialTraitsButton);
    ActionListener alRacial=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editRacialTraits();
      }
    };
    racialTraitsButton.addActionListener(alRacial);
  }

  private void editTraitTree()
  {
    TraitsStatus traitsStatus=_toon.getTraits();
    TraitTreeStatus status=traitsStatus.getTraitTreeStatus();
    TraitTreeStatus toEdit=new TraitTreeStatus(status);
    TraitTreeEditionDialog dialog=new TraitTreeEditionDialog(_parentWindow,_toon,toEdit);
    TraitTreeStatus result=dialog.editModal();
    if (result!=null)
    {
      TraitTreeStatus newStatus=new TraitTreeStatus(result);
      traitsStatus.setTraitTreeStatus(newStatus);
      // Broadcast toon update event...
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
      EventsManager.invokeEvent(event);
    }
  }

  private void editRacialTraits()
  {
    TraitsStatus traitsStatus=_toon.getTraits();
    TraitSlotsStatus status=traitsStatus.getRacialTraitsStatus();
    TraitSlotsStatus toEdit=new TraitSlotsStatus(status);
    RacialTraitsEditionDialogController dialog=new RacialTraitsEditionDialogController(_parentWindow,toEdit,_toon.getSummary());
    TraitSlotsStatus result=dialog.editModal();
    if (result!=null)
    {
      TraitSlotsStatus newStatus=new TraitSlotsStatus(result);
      traitsStatus.setRacialTraitsStatus(newStatus);
      // Broadcast toon update event...
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
      EventsManager.invokeEvent(event);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _parentWindow=null;
    _toon=null;
  }
}

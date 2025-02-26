package delta.games.lotro.gui.character.status.effects;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.status.effects.CharacterEffectsManager;
import delta.games.lotro.utils.events.EventsManager;
/**
 * Controller for a panel to access to the character effects.
 * @author DAM
 */
public class CharacterEffectsPanelController extends AbstractPanelController
{
  // Data
  private CharacterData _toon;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param character Targeted character.
   */
  public CharacterEffectsPanelController(WindowController parent, CharacterData character)
  {
    super(parent);
    _toon=character;
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());

    JButton effectsButton=GuiFactory.buildButton("Effects...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doEffects();
      }
    };
    effectsButton.addActionListener(al);
    panel.add(effectsButton);
    return panel;
  }

  private void doEffects()
  {
    CharacterEffectsManager mgr=_toon.getEffects();
    WindowController parent=getWindowController();
    CharacterEffectsManager result=CharacterEffectsEditionDialogController.editEffects(parent,mgr);
    if (result!=null)
    {
      // Broadcast effects update event...
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
      EventsManager.invokeEvent(event);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    _toon=null;
  }
}

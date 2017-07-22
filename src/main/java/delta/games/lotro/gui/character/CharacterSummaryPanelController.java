package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.summary.CharacterSummaryDialogController;

/**
 * Controller for character summary panel.
 * @author DAM
 */
public class CharacterSummaryPanelController implements CharacterEventListener
{
  // Data
  private CharacterFile _toon;
  private CharacterSummary _summary;
  // Controllers
  private WindowController _parent;
  // UI
  private JPanel _panel;
  private JLabel _nameLabel;
  private JLabel _levelLabel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Toon to display.
   */
  public CharacterSummaryPanelController(WindowController parent, CharacterFile toon)
  {
    _parent=parent;
    _toon=toon;
    _summary=toon.getSummary();
    CharacterEventsManager.addListener(this);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);

    // Grab data
    CharacterClass cClass=null;
    Race race=null;
    if (_summary!=null)
    {
      cClass=_summary.getCharacterClass();
      race=_summary.getRace();
    }
    // Class
    ImageIcon classIcon=LotroIconsManager.getClassIcon(cClass,LotroIconsManager.MEDIUM_SIZE);
    panel.add(GuiFactory.buildIconLabel(classIcon),c);
    // Race
    ImageIcon raceIcon=LotroIconsManager.getRaceIcon(race);
    c.gridx=1;
    panel.add(GuiFactory.buildIconLabel(raceIcon),c);
    // Name
    _nameLabel=GuiFactory.buildLabel("",28.0f);
    c.gridx=2;c.anchor=GridBagConstraints.CENTER;
    c.weightx=1.0;c.fill=GridBagConstraints.BOTH;
    panel.add(_nameLabel,c);
    // Level
    _levelLabel=GuiFactory.buildLabel("",32.0f);
    c.gridx=3;c.weightx=0.0;c.fill=GridBagConstraints.NONE;c.anchor=GridBagConstraints.EAST;
    panel.add(_levelLabel,c);
    c.gridx=4;
    JButton edit=GuiFactory.buildButton("Edit...");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editSummary();
      }
    };
    edit.addActionListener(al);
    panel.add(edit,c);
    update();
    return panel;
  }

  private void editSummary()
  {
    CharacterSummaryDialogController dialog=new CharacterSummaryDialogController(_parent,_summary);
    dialog.show(true);
    CharacterSummary summary=dialog.getSummary();
    if (summary!=null)
    {
      _toon.saveSummary(_summary);
      CharacterEvent event=new CharacterEvent(_toon,null);
      CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_SUMMARY_UPDATED,event);
    }
  }

  /**
   * Handle character events.
   */
  public void eventOccured(CharacterEventType type, CharacterEvent event)
  {
    if (type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      if (toon==_toon)
      {
        update();
      }
    }
  }

  /**
   * Update contents.
   */
  public void update()
  {
    if (_summary!=null)
    {
      String name=_summary.getName();
      String region=_summary.getRegion();
      String text=name;
      if ((region!=null) && (region.length()>0))
      {
        text=text+" of "+region;
      }
      _nameLabel.setText(text);
      int level=_summary.getLevel();
      _levelLabel.setText(String.valueOf(level));
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Listeners
    CharacterEventsManager.removeListener(this);
    // Controllers
    _parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Data
    _toon=null;
    _summary=null;
  }
}

package delta.games.lotro.gui.character.main;

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
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.summary.CharacterSummaryDialogController;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for character header panel.
 * @author DAM
 */
public class MainCharacterHeaderPanelController extends AbstractPanelController implements GenericEventsListener<CharacterEvent>
{
  // Data
  private CharacterFile _toon;
  private CharacterSummary _summary;
  // UI
  private JLabel _nameLabel;
  private JLabel _levelLabel;
  private JLabel _characterIconLabel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Toon to display.
   */
  public MainCharacterHeaderPanelController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    _toon=toon;
    _summary=toon.getSummary();
    setPanel(buildPanel());
    EventsManager.addListener(CharacterEvent.class,this);
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);

    // Grab data
    ClassDescription characterClass=null;
    RaceDescription race=null;
    CharacterSex sex=null;
    if (_summary!=null)
    {
      characterClass=_summary.getCharacterClass();
      race=_summary.getRace();
      sex=_summary.getCharacterSex();
    }
    // Class
    ImageIcon classIcon=null;
    if (characterClass!=null)
    {
      classIcon=LotroIconsManager.getClassIcon(characterClass.getIconId());
    }
    panel.add(GuiFactory.buildIconLabel(classIcon),c);
    // Character icon
    ImageIcon characterIcon=LotroIconsManager.getCharacterIcon(race,sex);
    c.gridx=1;
    _characterIconLabel=GuiFactory.buildIconLabel(characterIcon);
    panel.add(_characterIconLabel,c);
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
    JButton edit=GuiFactory.buildButton("Edit..."); // I18n
    ActionListener al=new ActionListener()
    {
      @Override
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
    CharacterSummaryDialogController dialog=new CharacterSummaryDialogController(getWindowController(),_summary);
    CharacterSummary summary=dialog.editModal();
    if (summary!=null)
    {
      _toon.saveSummary(_summary);
      _toon.summaryUpdated();
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_SUMMARY_UPDATED,_toon,null);
      EventsManager.invokeEvent(event);
    }
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
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
    if (_summary==null)
    {
      return;
    }
    // Name/region
    String name=_summary.getName();
    String region=_summary.getRegion();
    String text=name;
    if ((region!=null) && (!region.isEmpty()))
    {
      text=text+" of "+region; // I18n
    }
    _nameLabel.setText(text);
    // Level
    int level=_summary.getLevel();
    _levelLabel.setText(String.valueOf(level));
    // Character icon
    RaceDescription race=_summary.getRace();
    CharacterSex sex=_summary.getCharacterSex();
    ImageIcon characterIcon=LotroIconsManager.getCharacterIcon(race,sex);
    _characterIconLabel.setIcon(characterIcon);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Listeners
    EventsManager.removeListener(CharacterEvent.class,this);
    // UI
    _characterIconLabel=null;
    // Data
    _toon=null;
    _summary=null;
  }
}

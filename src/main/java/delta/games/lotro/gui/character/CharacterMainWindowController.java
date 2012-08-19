package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.log.CharacterLogWindowController;

/**
 * Controller for a "character" window.
 * @author DAM
 */
public class CharacterMainWindowController implements ActionListener
{
  private static final String LOG_COMMAND="log";

  private JFrame _window;
  private CharacterSummaryPanelController _filterController;
  private ChararacterStatsPanelController _tableController;
  private EquipmentPanelController _equipmentController;
  private CharacterFile _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterMainWindowController(CharacterFile toon)
  {
    _toon=toon;
    _filterController=new CharacterSummaryPanelController(_toon);
    _tableController=new ChararacterStatsPanelController(_toon);
    Character c=_toon.getLastCharacterInfo();
    CharacterEquipment equipment=c.getEquipment();
    _equipmentController=new EquipmentPanelController(equipment);
  }

  /**
   * Show the managed window.
   */
  public void show()
  {
    JFrame frame=getFrame();
    frame.setVisible(true);
  }

  /**
   * Get the managed frame.
   * @return the managed frame.
   */
  public JFrame getFrame()
  {
    if (_window==null)
    {
      _window=build();
    }
    return _window;
  }

  private JFrame build()
  {
    String name=_toon.getName();
    // Summary panel
    JPanel summaryPanel=_filterController.getPanel();
    // Stats panel
    JPanel statsPanel=_tableController.getPanel();
    // Equipment panel
    JPanel equipmentPanel=_equipmentController.getPanel();

    // Whole panel
    JPanel panel=new JPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(summaryPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(equipmentPanel,c);
    c=new GridBagConstraints(1,0,1,2,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(statsPanel,c);
    c=new GridBagConstraints(0,2,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,c);
    
    // Frame
    JFrame frame=new JFrame();
    frame.getContentPane().add(panel);
    String title="Character: "+name;
    frame.setTitle(title);
    frame.pack();
    frame.setLocation(200,200);
    WindowAdapter closeWindowAdapter=new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        close();
      }
    };
    frame.addWindowListener(closeWindowAdapter);
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    return frame;
  }

  private void close()
  {
    dispose();
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=new JPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JButton b=new JButton("Log");
    b.setActionCommand(LOG_COMMAND);
    b.addActionListener(this);
    panel.add(b,c);
    return panel;
  }

  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    CharacterLogWindowController logController=new CharacterLogWindowController(_toon);
    logController.show();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_window!=null)
    {
      _window.setVisible(false);
      _window.removeAll();
      _window.dispose();
      _window=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_equipmentController!=null)
    {
      _equipmentController.dispose();
      _equipmentController=null;
    }
    _toon=null;
  }
}

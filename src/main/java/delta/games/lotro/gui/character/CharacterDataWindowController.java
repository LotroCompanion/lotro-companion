package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.OKCancelPanelController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.character.io.xml.CharacterDataIO;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.gui.character.buffs.BuffEditionPanelController;
import delta.games.lotro.gui.character.essences.AllEssencesEditionWindowController;
import delta.games.lotro.gui.character.essences.EssencesSummaryWindowController;
import delta.games.lotro.gui.character.gear.EquipmentPanelController;
import delta.games.lotro.gui.character.tomes.TomesEditionPanelController;
import delta.games.lotro.gui.character.virtues.VirtuesDisplayPanelController;
import delta.games.lotro.gui.character.virtues.VirtuesEditionDialogController;

/**
 * Controller for a "character data" window.
 * @author DAM
 */
public class CharacterDataWindowController extends DefaultWindowController implements CharacterEventListener
{
  private CharacterMainAttrsEditionPanelController _attrsController;
  private CharacterStatsSummaryPanelController _statsController;
  private EquipmentPanelController _equipmentController;
  private VirtuesDisplayPanelController _virtuesController;
  private BuffEditionPanelController _buffsController;
  private TomesEditionPanelController _tomesController;
  private OKCancelPanelController _okCancelController;
  private CharacterFile _toonFile;
  private CharacterData _toon;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param toon Parent toon.
   * @param toonData Managed toon.
   */
  public CharacterDataWindowController(CharacterFile toon, CharacterData toonData)
  {
    _toonFile=toon;
    _toon=toonData;
    _windowsManager=new WindowsManager();
    _attrsController=new CharacterMainAttrsEditionPanelController(toon,toonData);
    _attrsController.set();
    _statsController=new CharacterStatsSummaryPanelController(this,toonData);
    _equipmentController=new EquipmentPanelController(this,toon,toonData);
    _virtuesController=new VirtuesDisplayPanelController();
    _virtuesController.setVirtues(toonData.getVirtues());
    _buffsController=new BuffEditionPanelController(this,toonData);
    _tomesController=new TomesEditionPanelController(toonData);
    _okCancelController=new OKCancelPanelController();
  }

  /**
   * Get the window identifier for a given toon.
   * @param data Data to use.
   * @return A window identifier.
   */
  public static String getIdentifier(CharacterData data)
  {
    String id="DATA#"+data.getFile();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel editionPanel=buildEditionPanel();
    panel.add(editionPanel,BorderLayout.CENTER);
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().setText("OK");

    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (OKCancelPanelController.OK_COMMAND.equals(action))
        {
          ok();
        }
        else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
        {
          cancel();
        }
      }
    };
    _okCancelController.getOKButton().addActionListener(al);
    _okCancelController.getCancelButton().addActionListener(al);

    return panel;
  }

  private JPanel buildEditionPanel()
  {
    // North: attributes panel
    JPanel attrsPanel=_attrsController.getPanel();

    // Center: equipment and stats
    // Stats panel
    JPanel statsPanel=_statsController.getPanel();
    // Equipment panel
    JPanel equipmentPanel=_equipmentController.getPanel();
    TitledBorder equipmentBorder=GuiFactory.buildTitledBorder("Equipment");
    equipmentPanel.setBorder(equipmentBorder);
    // Essences panel
    JPanel essencesPanel=buildEssencesPanel();
    TitledBorder essencesBorder=GuiFactory.buildTitledBorder("Essences");
    essencesPanel.setBorder(essencesBorder);

    JPanel gearingPanel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;
    c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    gearingPanel.add(equipmentPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(3,3,0,0),0,0);
    gearingPanel.add(essencesPanel,c);

    // Center panel
    JPanel centerPanel;
    {
      centerPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
      centerPanel.add(gearingPanel,c);
      c=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      centerPanel.add(GuiFactory.buildPanel(new BorderLayout()),c);
      c=new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
      centerPanel.add(statsPanel,c);
    }

    // Bottom panel
    JPanel bottomPanel=GuiFactory.buildPanel(new GridBagLayout());
    JPanel bottomPanel1=GuiFactory.buildPanel(new GridBagLayout());
    // - virtues
    {
      JPanel virtuesPanel=buildVirtuesPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Virtues");
      virtuesPanel.setBorder(border);
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      bottomPanel1.add(virtuesPanel,c);
    }
    // - tomes
    {
      JPanel tomesPanel=_tomesController.getPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Tomes");
      tomesPanel.setBorder(border);
      c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      bottomPanel1.add(tomesPanel,c);
    }
    // Space on right
    c=new GridBagConstraints(2,0,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    bottomPanel1.add(GuiFactory.buildPanel(new GridBagLayout()),c);
    c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    bottomPanel.add(bottomPanel1,c);

    JPanel bottomPanel2=GuiFactory.buildPanel(new GridBagLayout());
    // - buffs
    {
      JPanel buffsPanel=_buffsController.getPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Buffs");
      buffsPanel.setBorder(border);
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
      bottomPanel2.add(buffsPanel,c);
    }
    // Space on right
    c=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    bottomPanel2.add(GuiFactory.buildPanel(new GridBagLayout()),c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    bottomPanel.add(bottomPanel2,c);

    JPanel fullPanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    fullPanel.add(attrsPanel,BorderLayout.NORTH);
    fullPanel.add(centerPanel,BorderLayout.CENTER);
    fullPanel.add(bottomPanel,BorderLayout.SOUTH);

    // Register to events
    CharacterEventsManager.addListener(this);
    return fullPanel;
  }

  private JPanel buildEssencesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    // Edition
    {
      JButton edit=GuiFactory.buildButton("Edit...");
      ActionListener alEssences=new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          doEssencesEdition();
        }
      };
      edit.addActionListener(alEssences);
      panel.add(edit);
    }
    // Summary
    {
      JButton summary=GuiFactory.buildButton("Summary...");
      ActionListener alEssences=new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          doEssencesSummary();
        }
      };
      summary.addActionListener(alEssences);
      panel.add(summary);
    }
    return panel;
  }

  private JPanel buildVirtuesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    JPanel virtuesPanel=_virtuesController.getPanel();
    panel.add(virtuesPanel);
    JButton button=GuiFactory.buildButton("Edit...");
    panel.add(button);
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        VirtuesSet virtues=VirtuesEditionDialogController.editVirtues(CharacterDataWindowController.this,_toon.getVirtues());
        if (virtues!=null)
        {
          _toon.getVirtues().copyFrom(virtues);
          _virtuesController.setVirtues(virtues);
          // Broadcast virtues update event...
          CharacterEvent event=new CharacterEvent(null,_toon);
          CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
        }
      }
    };
    button.addActionListener(al);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Character: "+name+" @ "+serverName;
    frame.setTitle(title);
    // Set values
    setValues();
    // Size
    frame.pack();
    frame.setResizable(false);
    return frame;
  }

  private void setValues()
  {
    _statsController.update();
  }

  @Override
  public String getWindowIdentifier()
  {
    String id=getIdentifier(_toon);
    return id;
  }

  public TypedProperties getUserProperties(String id)
  {
    return CharacterPreferencesManager.getUserProperties(_toonFile,id);
  }

  private void doEssencesEdition()
  {
    AllEssencesEditionWindowController editionController=(AllEssencesEditionWindowController)_windowsManager.getWindow(AllEssencesEditionWindowController.IDENTIFIER);
    if (editionController==null)
    {
      editionController=new AllEssencesEditionWindowController(this,_toon);
      _windowsManager.registerWindow(editionController);
      editionController.getWindow().setLocationRelativeTo(this.getWindow());
    }
    editionController.bringToFront();
  }

  private void doEssencesSummary()
  {
    EssencesSummaryWindowController summaryController=(EssencesSummaryWindowController)_windowsManager.getWindow(EssencesSummaryWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new EssencesSummaryWindowController(_toon);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(this.getWindow());
    }
    summaryController.bringToFront();
  }

  public void eventOccured(CharacterEventType type, CharacterEvent event)
  {
    if (type==CharacterEventType.CHARACTER_DATA_UPDATED)
    {
      CharacterData data=event.getToonData();
      if (data==_toon)
      {
        // Compute new stats
        CharacterStatsComputer computer=new CharacterStatsComputer();
        BasicStatsSet stats=computer.getStats(data);
        BasicStatsSet toonStats=_toon.getStats();
        toonStats.clear();
        toonStats.setStats(stats);
        // Update stats display
        _statsController.update();
        // Update buffs display
        _buffsController.update();
      }
    }
    if (type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
    {
      CharacterFile toonFile=event.getToonFile();
      if (toonFile==_toonFile)
      {
        // Update sex
        _attrsController.updateSexDisplay();
      }
    }
  }

  private void ok()
  {
    _attrsController.get();
    boolean ok=CharacterDataIO.saveInfo(_toon.getFile(),_toon);
    if (ok)
    {
      CharacterEvent event=new CharacterEvent(null,_toon);
      CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
      dispose();
    }
    else
    {
      // TODO warn
    }
  }

  private void cancel()
  {
    _toon.revert();
    dispose();
  }

  @Override
  protected void doWindowClosing()
  {
    cancel();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    CharacterEventsManager.removeListener(this);
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_statsController!=null)
    {
      _statsController.dispose();
      _statsController=null;
    }
    if (_equipmentController!=null)
    {
      _equipmentController.dispose();
      _equipmentController=null;
    }
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    if (_buffsController!=null)
    {
      _buffsController.dispose();
      _buffsController=null;
    }
    if (_tomesController!=null)
    {
      _tomesController.dispose();
      _tomesController=null;
    }
    _toon=null;
    if (_toonFile!=null)
    {
      _toonFile.getPreferences().saveAllPreferences();
      _toonFile=null;
    }
  }
}

package delta.games.lotro.gui.stats.reputation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.OKCancelPanelController;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.reputation.FactionData;
import delta.games.lotro.character.reputation.ReputationData;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Controller for a "character reputation" dialog.
 * @author DAM
 */
public class CharacterReputationWindowController extends DefaultDialogController implements ActionListener
{
  // Data
  private CharacterFile _toon;
  // UI
  private ReputationDeedsDisplayController _deedsDisplay;
  private HashMap<String,FactionEditionPanelController> _editors;
  private OKCancelPanelController _okCancelController;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public CharacterReputationWindowController(WindowController parentController, CharacterFile toon)
  {
    super(parentController);
    _toon=toon;
    _editors=new HashMap<String,FactionEditionPanelController>();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Reputation editor");
    dialog.setResizable(false);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel dataPanel=buildPanel();
    panel.add(dataPanel,BorderLayout.CENTER);
    _okCancelController=new OKCancelPanelController();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().addActionListener(this);
    _okCancelController.getCancelButton().addActionListener(this);
    initData();
    return panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Top: reputation deeds
    ReputationData stats=_toon.getReputation();
    _deedsDisplay=new ReputationDeedsDisplayController(stats);
    JPanel deedsDisplayPanel=_deedsDisplay.getPanel();
    TitledBorder deedsBorder=GuiFactory.buildTitledBorder("Deeds");
    deedsDisplayPanel.setBorder(deedsBorder);
    panel.add(deedsDisplayPanel,BorderLayout.NORTH);
    // Center: faction levels
    FactionsRegistry registry=FactionsRegistry.getInstance();
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    List<String> categories=registry.getFactionCategories();
    for(String category : categories)
    {
      List<Faction> factions=registry.getFactionsForCategory(category);
      JPanel reputationPanel=buildReputationPanelForCategory(category,factions);
      tabs.add(category,reputationPanel);
    }
    TitledBorder factionsBorder=GuiFactory.buildTitledBorder("Factions");
    tabs.setBorder(factionsBorder);
    panel.add(tabs,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildReputationPanelForCategory(String category, List<Faction> factions)
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());

    // Factions
    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    GridBagConstraints cBar=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);

    int y=0;
    for(Faction faction : factions)
    {
      FactionEditionPanelController editionController=new FactionEditionPanelController(faction);
      _editors.put(faction.getKey(),editionController);
      // Label
      JLabel label=editionController.getLabel();
      cLabel.gridy=y;
      panel.add(label,cLabel);
      // Bar
      JProgressBar bar=editionController.getBar();
      cBar.gridy=y;
      panel.add(bar,cBar);
      y++;
    }
    JPanel gluePanel=GuiFactory.buildPanel(new BorderLayout());
    GridBagConstraints glueC=new GridBagConstraints(0,y,2,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(gluePanel,glueC);

    return panel;
  }

  private void initData()
  {
    ReputationData stats=_toon.getReputation();
    for(FactionEditionPanelController editor : _editors.values())
    {
      Faction faction=editor.getFaction();
      FactionLevel current;
      FactionData factionData=stats.getFactionStat(faction);
      if (factionData!=null)
      {
        current=factionData.getFactionLevel();
      }
      else
      {
        current=faction.getInitialLevel();
      }
      editor.setFactionLevel(current);
    }
    _deedsDisplay.update();
  }

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

  private void ok()
  {
    dispose();
  }

  private void cancel()
  {
    dispose();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_editors!=null)
    {
      for(FactionEditionPanelController editor : _editors.values())
      {
        editor.dispose();
      }
      _editors.clear();
      _editors=null;
    }
    _toon=null;
  }
}

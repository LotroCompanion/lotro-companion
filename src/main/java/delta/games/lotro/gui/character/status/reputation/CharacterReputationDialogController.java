package delta.games.lotro.gui.character.status.reputation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.character.status.reputation.ReputationStatus;
import delta.games.lotro.gui.character.status.reputation.form.FactionEditionDialogController;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionsRegistry;
import delta.games.lotro.stats.deeds.SyncDeedsStatusAndReputationStatus;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a "character reputation" dialog.
 * @author DAM
 */
public class CharacterReputationDialogController extends DefaultFormDialogController<ReputationStatus> implements ActionListener
{
  // Data
  private CharacterFile _toon;
  // UI
  private ReputationDeedsDisplayController _deedsDisplay;
  private ReputationRewardsDisplayController _rewardsDisplay;
  private HashMap<Integer,FactionStatusEditionGagdgetsController> _editors;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public CharacterReputationDialogController(WindowController parentController, CharacterFile toon)
  {
    super(parentController,null);
    _toon=toon;
    _data=_toon.getReputation();
    _editors=new HashMap<Integer,FactionStatusEditionGagdgetsController>();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Reputation editor"); // I18n
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildPanel();
    initData();
    return dataPanel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Top: reputation deeds
    JPanel topPanel=buildTopPanel();
    panel.add(topPanel,BorderLayout.NORTH);
    // Center: faction levels
    FactionsRegistry registry=FactionsRegistry.getInstance();
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    List<String> categories=registry.getFactionCategories();
    for(String category : categories)
    {
      List<Faction> factions=registry.getFactionsForCategory(category);
      factions=filterFactions(factions);
      if (factions.size()==0)
      {
        continue;
      }
      JPanel reputationPanel=buildReputationPanelForCategory(category,factions);
      JPanel tabPanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
      tabPanel.add(reputationPanel,BorderLayout.CENTER);
      tabs.add(category,tabPanel);
    }
    TitledBorder factionsBorder=GuiFactory.buildTitledBorder("Factions"); // I18n
    tabs.setBorder(factionsBorder);
    panel.add(tabs,BorderLayout.CENTER);
    return panel;
  }

  private List<Faction> filterFactions(List<Faction> factions)
  {
    List<Faction> ret=new ArrayList<Faction>();
    for(Faction faction : factions)
    {
      if (!faction.isGuildFaction())
      {
        ret.add(faction);
      }
    }
    return ret;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Deeds
    _deedsDisplay=new ReputationDeedsDisplayController(_data);
    JPanel deedsDisplayPanel=_deedsDisplay.getPanel();
    TitledBorder deedsBorder=GuiFactory.buildTitledBorder("Deeds"); // I18n
    deedsDisplayPanel.setBorder(deedsBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(deedsDisplayPanel,c);
    // Rewards
    _rewardsDisplay=new ReputationRewardsDisplayController(_data);
    JPanel rewardsDisplayPanel=_rewardsDisplay.getPanel();
    TitledBorder rewardsBorder=GuiFactory.buildTitledBorder("Rewards"); // I18n
    rewardsDisplayPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(5,5,5,5),0,0);
    panel.add(rewardsDisplayPanel,c);
    return panel;
  }

  private JPanel buildReputationPanelForCategory(String category, List<Faction> factions)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Factions
    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    GridBagConstraints cBar=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
    GridBagConstraints cEdit=new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);

    int y=0;
    for(Faction faction : factions)
    {
      FactionStatusEditionGagdgetsController editionController=new FactionStatusEditionGagdgetsController(this,faction);
      Integer key=Integer.valueOf(faction.getIdentifier());
      _editors.put(key,editionController);
      // Label
      JLabel label=editionController.getLabel();
      cLabel.gridy=y;
      panel.add(label,cLabel);
      // Bar and buttons panel
      FlowLayout layout=new FlowLayout(FlowLayout.TRAILING);
      layout.setVgap(0);
      JPanel barButtonsPanel=GuiFactory.buildPanel(layout);
      // - button minus
      JButton minus=editionController.getMinusButton();
      minus.addActionListener(this);
      barButtonsPanel.add(minus);
      // - bar
      JProgressBar bar=editionController.getBar();
      barButtonsPanel.add(bar);
      // - button plus
      JButton plus=editionController.getPlusButton();
      plus.addActionListener(this);
      barButtonsPanel.add(plus);
      cBar.gridy=y;
      panel.add(barButtonsPanel,cBar);
      // - button edit
      JButton edit=editionController.getEditButton();
      edit.addActionListener(this);
      cEdit.gridy=y;
      panel.add(edit,cEdit);
      y++;
    }
    JPanel gluePanel=GuiFactory.buildPanel(new BorderLayout());
    GridBagConstraints glueC=new GridBagConstraints(0,y,2,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(gluePanel,glueC);

    return panel;
  }

  private void initData()
  {
    for(FactionStatusEditionGagdgetsController editor : _editors.values())
    {
      updateFactionDisplay(editor);
    }
    _deedsDisplay.update();
    _rewardsDisplay.update();
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    // +/- buttons
    Object source=event.getSource();
    for(FactionStatusEditionGagdgetsController editor : _editors.values())
    {
      if (source==editor.getMinusButton())
      {
        Faction faction=editor.getFaction();
        _data.updateFaction(faction,false);
        updateFactionDisplay(editor);
        _deedsDisplay.update();
        _rewardsDisplay.update();
      }
      else if (source==editor.getPlusButton())
      {
        Faction faction=editor.getFaction();
        _data.updateFaction(faction,true);
        updateFactionDisplay(editor);
        _deedsDisplay.update();
        _rewardsDisplay.update();
      }
      else if (source==editor.getEditButton())
      {
        Faction faction=editor.getFaction();
        FactionStatus status=_data.getOrCreateFactionStat(faction);
        FactionEditionDialogController edit=new FactionEditionDialogController(this,status);
        edit.show(true);
        updateFactionDisplay(editor);
        _deedsDisplay.update();
        _rewardsDisplay.update();
      }
    }
  }

  private void updateFactionDisplay(FactionStatusEditionGagdgetsController editor)
  {
    Faction faction=editor.getFaction();
    FactionStatus factionStatus=_data.getFactionStatus(faction);
    editor.setFactionStatus(factionStatus);
  }

  @Override
  protected void okImpl()
  {
    _toon.saveReputation();
    // Sync deeds status
    {
      AchievablesStatusManager deedsStatus=DeedsStatusIo.load(_toon);
      SyncDeedsStatusAndReputationStatus.syncDeedsStatus(_data,deedsStatus);
      DeedsStatusIo.save(_toon,deedsStatus);
      CharacterEvent event=new CharacterEvent(CharacterEventType.DEEDS_STATUS_UPDATED,_toon,null);
      EventsManager.invokeEvent(event);
    }
    // Broadcast reputation update event...
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_REPUTATION_UPDATED,_toon,null);
    EventsManager.invokeEvent(event);
  }

  @Override
  protected void cancelImpl()
  {
    _toon.revertReputation();
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
      for(FactionStatusEditionGagdgetsController editor : _editors.values())
      {
        editor.dispose();
      }
      _editors.clear();
      _editors=null;
    }
    _deedsDisplay=null;
    _rewardsDisplay=null;
    _toon=null;
  }
}

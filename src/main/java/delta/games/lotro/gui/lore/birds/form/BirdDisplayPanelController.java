package delta.games.lotro.gui.lore.birds.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.collections.birds.BirdDescription;
import delta.games.lotro.utils.gui.HtmlUiUtils;

/**
 * Controller for a bird display panel.
 * @author DAM
 */
public class BirdDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private BirdDescription _bird;
  // GUI
  private JLabel _largeIcon;
  private JLabel _name;
  private JLabel _elvishName;
  private JEditorPane _description;
  // Controllers
  private BirdReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param bird Bird to show.
   */
  public BirdDisplayPanelController(NavigatorWindowController parent, BirdDescription bird)
  {
    super(parent);
    _bird=bird;
    _references=new BirdReferencesDisplayController(parent,bird.getIdentifier());
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    return "Bird: "+_bird.getName();
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenter();
    if (center!=null)
    {
      c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
      panel.add(center,c);
    }
    return panel;
  }

  private JPanel buildCenter()
  {
    JPanel panel=null;
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      panel=GuiFactory.buildPanel(new BorderLayout());
      panel.add(references,BorderLayout.CENTER);
      panel.setBorder(GuiFactory.buildTitledBorder("References"));
    }
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    JPanel summaryPanel=buildTopSummaryPanel();
    panel.add(summaryPanel,c);
    // Description
    String description=_bird.getItem().getDescription();
    _description=HtmlUiUtils.buildEditorPane(description);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,5,5,5),0,0);
    panel.add(_description,c);
    return panel;
  }

  private JPanel buildTopSummaryPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Icon
    _largeIcon=GuiFactory.buildIconLabel(null);
    ImageIcon icon=LotroIconsManager.getBirdIcon(_bird.getLargeIconID());
    _largeIcon.setIcon(icon);
    GridBagConstraints c=new GridBagConstraints(0,0,1,3,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    panel.add(_largeIcon,c);
    // Name
    _name=GuiFactory.buildLabel("");
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    String name=_bird.getName();
    _name.setText(name);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_name,c);
    // Elvish name
    _elvishName=GuiFactory.buildLabel("");
    String elvishName=_bird.getElvishName();
    _elvishName.setText((elvishName!=null)?elvishName:"");
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_elvishName,c);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _bird=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    _largeIcon=null;
    _name=null;
    _elvishName=null;
    _description=null;
  }
}

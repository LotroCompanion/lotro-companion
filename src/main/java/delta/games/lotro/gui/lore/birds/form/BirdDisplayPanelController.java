package delta.games.lotro.gui.lore.birds.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
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

/**
 * Controller for a bird display panel.
 * @author DAM
 */
public class BirdDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private BirdDescription _bird;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _name;
  private JLabel _elvishName;
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
  }

  @Override
  public String getTitle()
  {
    return "Bird: "+_bird.getName();
  }

  @Override
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
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

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Icon
      _icon=GuiFactory.buildIconLabel(null);
      panelLine.add(_icon);
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }

    // Line 2
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Elvish name
      panelLine.add(GuiFactory.buildLabel("Elvish name: "));
      _elvishName=GuiFactory.buildLabel("");
      panelLine.add(_elvishName);
    }

    _panel=panel;
    setTitle();
    return _panel;
  }

  /**
   * Set the title to display.
   */
  private void setTitle()
  {
    String name=_bird.getName();
    // Name
    _name.setText(name);
    // Icon
    
    ImageIcon icon=LotroIconsManager.getItemIcon(_bird.getItem().getIcon());
    _icon.setIcon(icon);
    // Elvish name
    String elvishName=_bird.getElvishName();
    _elvishName.setText((elvishName!=null)?elvishName:"");
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
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _elvishName=null;
  }
}

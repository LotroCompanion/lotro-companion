package delta.games.lotro.gui.lore.races.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RaceGender;
import delta.games.lotro.character.races.RaceTrait;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a race display panel.
 * @author DAM
 */
public class RaceDisplayPanelController implements NavigablePanelController
{
  // Data
  private RaceDescription _race;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private RaceReferencesDisplayController _references;
  private List<IconLinkLabelGadgetsController> _traits;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param race Race to show.
   */
  public RaceDisplayPanelController(NavigatorWindowController parent, RaceDescription race)
  {
    _parent=parent;
    _race=race;
    _references=new RaceReferencesDisplayController(parent,race.getRace());
  }

  @Override
  public String getTitle()
  {
    return "Race: "+_race.getRace().getLabel();
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenter();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(center,c);
    panel.setPreferredSize(new Dimension(500,500));
    return panel;
  }

  private JPanel buildCenter()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Description
    JEditorPane description=buildEditorPane(_race.getDescription());
    if (description!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
      y++;
    }
    // Build components for tabs
    JEditorPane references=_references.getComponent();
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    // - traits
    JPanel traitsPanel=buildTraitsPanel();
    tabbedPane.add("Traits", buildPanelForTab(traitsPanel));
    // - references
    tabbedPane.add("References",buildPanelForTab(references));
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(tabbedPane,c);
    y++;
    return panel;
  }

  private JPanel buildPanelForTab(Component contents)
  {
    JPanel wrapper=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JScrollPane scrollPane=GuiFactory.buildScrollPane(contents);
    wrapper.add(scrollPane,BorderLayout.CENTER);
    return wrapper;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      RaceGender maleGender=_race.getMaleGender();
      if (maleGender!=null)
      {
        ImageIcon icon=LotroIconsManager.getCharacterIcon(_race.getRace(),CharacterSex.MALE);
        JLabel iconLabel=GuiFactory.buildIconLabel(icon);
        panelLine.add(iconLabel);
      }
      RaceGender femaleGender=_race.getFemaleGender();
      if (femaleGender!=null)
      {
        ImageIcon icon=LotroIconsManager.getCharacterIcon(_race.getRace(),CharacterSex.FEMALE);
        JLabel iconLabel=GuiFactory.buildIconLabel(icon);
        panelLine.add(iconLabel);
      }
      // Name
      String name=_race.getRace().getLabel();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Attributes
    List<String> lines=getAttributesLines();
    for(String line : lines)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panelLine.add(GuiFactory.buildLabel(line));
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    panel.add(paddingPanel,c);

    return panel;
  }

  private List<String> getAttributesLines()
  {
    List<String> ret=new ArrayList<String>();
    // Tall?
    boolean isTall=_race.isTall();
    ret.add(isTall?"Tall":"Small");
    return ret;
  }

  private JPanel buildTraitsPanel()
  {
    // Build traits gadgets controllers
    _traits=new ArrayList<IconLinkLabelGadgetsController>();

    // Traits acquired with level
    List<RaceTrait> traits=_race.getTraits();
    for(RaceTrait raceTrait : traits)
    {
      TraitDescription trait=raceTrait.getTrait();
      IconLinkLabelGadgetsController gadget=GadgetsControllersFactory.build(_parent,trait);
      int level=raceTrait.getRequiredLevel();
      if (level>1)
      {
        gadget.getComplement().setText("at level "+level);
      }
      _traits.add(gadget);
    }
    // Earnable traits
    List<TraitDescription> earnableTraits=_race.getEarnableTraits();
    for(TraitDescription earnableTrait : earnableTraits)
    {
      IconLinkLabelGadgetsController gadget=GadgetsControllersFactory.build(_parent,earnableTrait);
      _traits.add(gadget);
    }
    return buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    for(IconLinkLabelGadgetsController gadgets : _traits)
    {
      // Icon
      GridBagConstraints c=new GridBagConstraints(0,y,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getIcon().getIcon(),c);
      // Text
      boolean hasComplements=(gadgets.getComplement().getText().length()>0);
      int height=(hasComplements?1:2);
      c=new GridBagConstraints(1,y,1,height,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getLink().getLabel(),c);
      if (hasComplements)
      {
        c=new GridBagConstraints(1,y+1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
        ret.add(gadgets.getComplement(),c);
      }
      y+=2;
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createVerticalGlue(),c);
    return ret;
  }


  private JEditorPane buildEditorPane(String input)
  {
    JEditorPane editor=null;
    if ((input!=null) && (input.length()>0))
    {
      editor=GuiFactory.buildHtmlPanel();
      StringBuilder sb=new StringBuilder();
      sb.append("<html><body>");
      sb.append(HtmlUtils.toHtml(input));
      sb.append("</body></html>");
      editor.setText(sb.toString());
    }
    return editor;
  }

  @Override
  public void dispose()
  {
    // Data
    _race=null;
    // Controllers
    _parent=null;
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    if (_traits!=null)
    {
      for(IconLinkLabelGadgetsController gadget : _traits)
      {
        gadget.dispose();
      }
      _traits=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}

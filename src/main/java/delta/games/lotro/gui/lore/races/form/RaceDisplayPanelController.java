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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RaceGender;
import delta.games.lotro.character.races.RaceTrait;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.Genders;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.enums.TraitNature;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.gui.utils.SharedLinks;
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
  private List<HyperLinkController> _nationalities;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param race Race to show.
   */
  public RaceDisplayPanelController(NavigatorWindowController parent, RaceDescription race)
  {
    _parent=parent;
    _race=race;
    _references=new RaceReferencesDisplayController(parent,race);
    _nationalities=new ArrayList<HyperLinkController>();
  }

  @Override
  public String getTitle()
  {
    return "Race: "+_race.getName();
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
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    // - traits
    JPanel traitsPanel=buildTraitsPanel();
    tabbedPane.add("Traits", buildPanelForTab(traitsPanel));
    // - references
    JEditorPane references=_references.getComponent();
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
        ImageIcon icon=LotroIconsManager.getCharacterIcon(_race,Genders.MALE);
        JLabel iconLabel=GuiFactory.buildIconLabel(icon);
        panelLine.add(iconLabel);
      }
      RaceGender femaleGender=_race.getFemaleGender();
      if (femaleGender!=null)
      {
        ImageIcon icon=LotroIconsManager.getCharacterIcon(_race,Genders.FEMALE);
        JLabel iconLabel=GuiFactory.buildIconLabel(icon);
        panelLine.add(iconLabel);
      }
      // Name
      String name=_race.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(28f).deriveFont(Font.BOLD));
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
    // Nationalities
    JPanel nationalitiesPanel=buildNationalitiesPanel();
    nationalitiesPanel.setBorder(GuiFactory.buildTitledBorder("Nationalities"));
    panel.add(nationalitiesPanel,c);
    c.gridy++;

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

  private JPanel buildNationalitiesPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    List<NationalityDescription> nationalities=new ArrayList<NationalityDescription>(_race.getNationalities());
    Collections.sort(nationalities,new NamedComparator());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    for(NationalityDescription nationality : nationalities)
    {
      c.insets=new Insets((c.gridy==0)?2:0,5,2,5);
      HyperLinkController ctrl=SharedLinks.buildNationalityLink(_parent,nationality);
      _nationalities.add(ctrl);
      ret.add(ctrl.getLabel(),c);
      c.gridy++;
    }
    Component minWidthComponent=Box.createHorizontalStrut(100);
    ret.add(minWidthComponent,c);
    return ret;
  }

  private JPanel buildTraitsPanel()
  {
    // Build traits gadgets controllers
    _traits=new ArrayList<IconLinkLabelGadgetsController>();

    // Fetch traits
    Map<Integer,TraitDescription> traitsMap=getTraits();
    // Build gadgets
    Map<Integer,IconLinkLabelGadgetsController> gadgets=new HashMap<Integer,IconLinkLabelGadgetsController>();
    for(Map.Entry<Integer,TraitDescription> entry : traitsMap.entrySet())
    {
      IconLinkLabelGadgetsController gadget=GadgetsControllersFactory.build(_parent,entry.getValue());
      gadgets.put(entry.getKey(),gadget);
    }
    // Update level data
    List<RaceTrait> traits=_race.getTraits();
    for(RaceTrait raceTrait : traits)
    {
      TraitDescription trait=raceTrait.getTrait();
      Integer key=Integer.valueOf(trait.getIdentifier());
      int level=raceTrait.getRequiredLevel();
      if (level>1)
      {
        IconLinkLabelGadgetsController gadget=gadgets.get(key);
        gadget.getComplement().setText("at level "+level);
      }
    }
    _traits.addAll(gadgets.values());

    // Sort by trait nature
    Map<TraitNature,List<TraitDescription>> mapByNature=sortTraitsByNature(traitsMap.values());
    // Earnable traits
    List<TraitDescription> earnableTraits=_race.getEarnableTraits();
    for(TraitDescription earnableTrait : earnableTraits)
    {
      IconLinkLabelGadgetsController gadget=GadgetsControllersFactory.build(_parent,earnableTrait);
      _traits.add(gadget);
    }
    return buildPanel(mapByNature,gadgets);
  }

  private Map<Integer,TraitDescription> getTraits()
  {
    Map<Integer,TraitDescription> map=new HashMap<Integer,TraitDescription>();
    List<RaceTrait> traits=_race.getTraits();
    for(RaceTrait raceTrait : traits)
    {
      TraitDescription trait=raceTrait.getTrait();
      Integer key=Integer.valueOf(trait.getIdentifier());
      map.put(key,trait);
    }
    // Earnable traits
    List<TraitDescription> earnableTraits=_race.getEarnableTraits();
    for(TraitDescription earnableTrait : earnableTraits)
    {
      Integer key=Integer.valueOf(earnableTrait.getIdentifier());
      map.put(key,earnableTrait);
    }
    return map;
  }

  private Map<TraitNature,List<TraitDescription>> sortTraitsByNature(Collection<TraitDescription> traits)
  {
    Map<TraitNature,List<TraitDescription>> ret=new HashMap<TraitNature,List<TraitDescription>>();
    for(TraitDescription trait : traits)
    {
      TraitNature nature=trait.getNature();
      List<TraitDescription> list=ret.get(nature);
      if (list==null)
      {
        list=new ArrayList<TraitDescription>();
        ret.put(nature,list);
      }
      list.add(trait);
    }
    for(List<TraitDescription> list : ret.values())
    {
      Collections.sort(list,new NamedComparator());
    }
    return ret;
  }

  private JPanel buildPanel(Map<TraitNature,List<TraitDescription>> mapByNature, Map<Integer,IconLinkLabelGadgetsController> gadgetsMap)
  {
    List<TraitNature> natures=new ArrayList<TraitNature>();
    natures.addAll(mapByNature.keySet());
    Collections.sort(natures,new LotroEnumEntryNameComparator<TraitNature>());

    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    for(TraitNature nature : natures)
    {
      List<IconLinkLabelGadgetsController> gadgetsList=new ArrayList<IconLinkLabelGadgetsController>();
      for(TraitDescription trait : mapByNature.get(nature))
      {
        Integer key=Integer.valueOf(trait.getIdentifier());
        IconLinkLabelGadgetsController gadgets=gadgetsMap.get(key);
        gadgetsList.add(gadgets);
      }
      JPanel panel=buildPanel(gadgetsList);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      panel.setBorder(GuiFactory.buildTitledBorder(nature.getLabel()));
      ret.add(panel,c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0);
    ret.add(GuiFactory.buildPanel(null),c);
    return ret;
  }

  private JPanel buildPanel(List<IconLinkLabelGadgetsController> gadgetsList)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(IconLinkLabelGadgetsController gadgets : gadgetsList)
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
    if (_nationalities!=null)
    {
      for(HyperLinkController ctrl : _nationalities)
      {
        ctrl.dispose();
      }
      _nationalities=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}

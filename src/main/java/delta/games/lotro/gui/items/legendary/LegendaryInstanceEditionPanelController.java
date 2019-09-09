package delta.games.lotro.gui.items.legendary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.items.legendary.imbued.ImbuedLegendaryAttrsEditionPanelController;
import delta.games.lotro.gui.items.legendary.non_imbued.NonImbuedLegendaryAttrsEditionPanelController;
import delta.games.lotro.gui.items.legendary.passives.PassivesEditionPanelController;
import delta.games.lotro.gui.items.legendary.relics.RelicsEditionPanelController;
import delta.games.lotro.gui.items.legendary.titles.LegendaryTitleEditionPanelController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.relics.RelicsSet;

/**
 * Panel to edit the attributes of a legendary item instance (name, legacies, title, passives, relics).
 * @author DAM
 */
public class LegendaryInstanceEditionPanelController
{
  // GUI
  private JPanel _panel;
  private JTextField _name;
  private CheckboxController _isImbued;
  private JPanel _legaciesPanel;
  // Controllers
  private ImbuedLegendaryAttrsEditionPanelController _imbued;
  private NonImbuedLegendaryAttrsEditionPanelController _nonImbued;
  private LegendaryTitleEditionPanelController _title;
  private PassivesEditionPanelController _passives;
  private RelicsEditionPanelController _relics;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item to edit.
   * @param constraints Class/slot constraints.
   */
  public LegendaryInstanceEditionPanelController(WindowController parent, ItemInstance<? extends Item> item, ClassAndSlot constraints)
  {
    LegendaryInstance legendaryInstance=(LegendaryInstance)item;
    LegendaryInstanceAttrs attrs=legendaryInstance.getLegendaryAttributes();
    // Name
    _name=GuiFactory.buildTextField("");
    _name.setColumns(25);
    String legendaryName=attrs.getLegendaryName();
    _name.setText(legendaryName);
    // Is Imbued?
    _isImbued=new CheckboxController("Imbued?");
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setImbued(_isImbued.isSelected());
      }
    };
    _isImbued.getCheckbox().addActionListener(l);
    // Legacies panel
    _legaciesPanel=GuiFactory.buildPanel(new BorderLayout());
    // Imbued attributes
    ImbuedLegendaryInstanceAttrs imbuedAttrs=attrs.getImbuedAttrs();
    _imbued=new ImbuedLegendaryAttrsEditionPanelController(parent,imbuedAttrs,constraints);
    // Non-imbued attributes
    NonImbuedLegendaryInstanceAttrs nonImbuedAttrs=attrs.getNonImbuedAttrs();
    _nonImbued=new NonImbuedLegendaryAttrsEditionPanelController(parent,nonImbuedAttrs,constraints);
    Integer itemLevelInt=item.getEffectiveItemLevel();
    int itemLevel=(itemLevelInt!=null)?itemLevelInt.intValue():1;
    Item itemReference=item.getReference();
    _nonImbued.setReferenceData(itemLevel,itemReference);
    // Title
    _title=new LegendaryTitleEditionPanelController(parent,attrs);
    // Passives
    int itemId=item.getIdentifier();
    _passives=new PassivesEditionPanelController(parent,attrs,itemId,itemLevel);
    // Relics
    RelicsSet relics=attrs.getRelicsSet();
    _relics=new RelicsEditionPanelController(parent,relics);
    // Build panel
    _panel=build();
    boolean isImbued=attrs.isImbued();
    _isImbued.setSelected(isImbued);
    setImbued(isImbued);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
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
    // Left
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    JPanel leftPanel=buildLeftPanel();
    panel.add(leftPanel,c);
    // Right
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel rightPanel=buildRightPanel();
    panel.add(rightPanel,c);
    return panel;
  }

  private JPanel buildLeftPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Attributes
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    JPanel attributesPanel=buildAttributesPanel();
    panel.add(attributesPanel,c);
    // Legacies
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(_legaciesPanel,c);
    // Set the non-imbued contents as default
    setImbued(false);
    return panel;
  }

  private JPanel buildRightPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Title
    GridBagConstraints c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    JPanel titlePanel=_title.getPanel();
    titlePanel.setBorder(GuiFactory.buildTitledBorder("Title"));
    panel.add(titlePanel,c);
    // Passives
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    JPanel passivesPanel=_passives.getPanel();
    passivesPanel.setBorder(GuiFactory.buildTitledBorder("Passives"));
    panel.add(passivesPanel,c);
    // Relics
    c=new GridBagConstraints(1,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    JPanel relicsPanel=_relics.getPanel();
    relicsPanel.setBorder(GuiFactory.buildTitledBorder("Relics"));
    panel.add(relicsPanel,c);
    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    JPanel namePanel=GuiFactory.buildPanel(new FlowLayout());
    namePanel.add(GuiFactory.buildLabel("Inscription:"));
    namePanel.add(_name);
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(namePanel,c);
    c.gridy++;
    ret.add(_isImbued.getCheckbox(),c);
    return ret;
  }

  private void setImbued(boolean imbued)
  {
    _legaciesPanel.removeAll();
    if (imbued)
    {
      _legaciesPanel.add(_imbued.getPanel(),BorderLayout.CENTER);
    }
    else
    {
      _legaciesPanel.add(_nonImbued.getPanel(),BorderLayout.CENTER);
    }
    if (_panel!=null)
    {
      _panel.revalidate();
      _panel.repaint();
    }
  }

  /**
   * Get the contents of the edited data into the given storage.
   * @param legendaryAttrs Storage for data.
   */
  public void getData(LegendaryInstanceAttrs legendaryAttrs)
  {
    // TODO
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    _legaciesPanel=null;
    // Controllers
    if (_isImbued!=null)
    {
      _isImbued.dispose();
      _isImbued=null;
    }
    if (_imbued!=null)
    {
      _imbued.dispose();
      _imbued=null;
    }
    if (_nonImbued!=null)
    {
      _nonImbued.dispose();
      _nonImbued=null;
    }
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    if (_passives!=null)
    {
      _passives.dispose();
      _passives=null;
    }
    if (_relics!=null)
    {
      _relics.dispose();
      _relics=null;
    }
  }
}

package delta.games.lotro.gui.lore.items.legendary.titles;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.LegendaryTitleTier;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.DamageTypes;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;

/**
 * Controller for the UI items of a single legendary title.
 * @author DAM
 */
public class SingleTitleEditionController
{
  // Data
  private LegendaryTitle _legendaryTitle;
  // Controllers
  private WindowController _parent;
  // GUI
  // - title name
  private JLabel _name;
  // - complement:
  //    - damage type, if not COMMON
  //    - slayer if any + tier if no stats
  private JLabel _complement;
  // - stats
  private MultilineLabel2 _stats;
  // - buttons:
  private JButton _chooseButton;
  private JButton _deleteButton;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param legendaryTitle Current legendary title (may be <code>null</code>).
   */
  public SingleTitleEditionController(WindowController parent, LegendaryTitle legendaryTitle)
  {
    _parent=parent;
    _legendaryTitle=legendaryTitle;
    // UI
    // - name
    _name=GuiFactory.buildLabel("");
    // - complement
    _complement=GuiFactory.buildLabel("");
    // - value display
    _stats=new MultilineLabel2();
    Dimension dimension=new Dimension(200,32);
    _stats.setMinimumSize(dimension);
    _stats.setSize(dimension);
    // - chooser button
    _chooseButton=GuiFactory.buildButton("...");
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButtonClick();
      }
    };
    _chooseButton.addActionListener(listener);
    // - delete button
    _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
    ActionListener listenerDelete=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleDelete();
      }
    };
    _deleteButton.addActionListener(listenerDelete);
    updateUi();
  }

  private void handleButtonClick()
  {
    LegendaryTitle legendaryTitle=LegendaryTitleChooser.selectLegendaryTitle(_parent,_legendaryTitle);
    if (legendaryTitle!=null)
    {
      _legendaryTitle=legendaryTitle;
      updateUi();
    }
  }

  private void handleDelete()
  {
    _legendaryTitle=null;
    updateUi();
  }

  /**
   * Get the managed legendary title.
   * @return the managed legendary title.
   */
  public LegendaryTitle getLegendaryTitle()
  {
    return _legendaryTitle;
  }

  /**
   * Update UI to show the internal legacy data.
   */
  private void updateUi()
  {
    // Name
    String name=_legendaryTitle!=null?_legendaryTitle.getName():"";
    _name.setText(name);
    // Complement
    StringBuilder complementSb=new StringBuilder();
    if (_legendaryTitle!=null)
    {
      boolean useTier=false;
      DamageType damageType=_legendaryTitle.getDamageType();
      if ((damageType!=null) && (damageType!=DamageTypes.COMMON))
      {
        String damageStr=damageType.getName();
        complementSb.append(damageStr).append(" damage");
        useTier=true;
      }
      Genus slayerType=_legendaryTitle.getSlayerGenusType();
      if (slayerType!=null)
      {
        if (complementSb.length()>0) complementSb.append(", ");
        complementSb.append(slayerType.getLabel()).append(" slayer");
        useTier=true;
      }
      if (useTier)
      {
        LegendaryTitleTier tier=_legendaryTitle.getTier();
        complementSb.append(", ").append(tier);
      }
      _complement.setText(complementSb.toString());
    }
    else
    {
      _complement.setText("");
    }
    // Stats
    updateStats();
    // Resize window
    _parent.pack();
  }

  private void updateStats()
  {
    if (_legendaryTitle!=null)
    {
      BasicStatsSet stats=_legendaryTitle.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _stats.setText(lines);
    }
    else
    {
      _stats.setText(new String[]{});
    }
  }

  /**
   * Get the label for the title name.
   * @return a label.
   */
  public JLabel getNameLabel()
  {
    return _name;
  }

  /**
   * Get the label for the complements.
   * @return a label.
   */
  public JLabel getComplementLabel()
  {
    return _complement;
  }

  /**
   * Get the label to display the legendary title.
   * @return a multiline label.
   */
  public MultilineLabel2 getStatsLabel()
  {
    return _stats;
  }

  /**
   * Get the managed choose button.
   * @return the managed choose button.
   */
  public JButton getChooseButton()
  {
    return _chooseButton;
  }

  /**
   * Get the managed 'delete' button.
   * @return the managed 'delete' button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _legendaryTitle=null;
    // Controllers
    _parent=null;
    // UI
    _name=null;
    _complement=null;
    _stats=null;
    _chooseButton=null;
    _deleteButton=null;
  }
}

package delta.games.lotro.gui.lore.items.essences;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.MultilineLabel;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.lore.items.essences.Essence;

/**
 * Controller for the UI items of a single essence.
 * @author DAM
 */
public class SimpleSingleEssenceEditionController
{
  // Data
  private BasicCharacterAttributes _attrs;
  private Essence _essence;
  private int _linesCount;
  private SocketType _type;
  // Controllers
  private WindowController _parent;
  // UI
  private JButton _essenceIconButton;
  private MultilineLabel _essenceName;
  private JButton _deleteButton;
  // Listeners
  private EssenceUpdatedListener _listener;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param linesCount Number of lines to display the essence name.
   * @param attrs Attributes of toon to use.
   * @param type Socket type.
   */
  public SimpleSingleEssenceEditionController(WindowController parent, int linesCount, BasicCharacterAttributes attrs, SocketType type)
  {
    _parent=parent;
    _essence=null;
    _linesCount=linesCount;
    _attrs=attrs;
    _type=type;
    // Button
    _essenceIconButton=GuiFactory.buildButton("");
    _essenceIconButton.setOpaque(false);
    _essenceIconButton.setBorderPainted(false);
    _essenceIconButton.setMargin(new Insets(0,0,0,0));
    _essenceIconButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButtonClick((JButton)e.getSource());
      }
    };
    _essenceIconButton.addActionListener(listener);
    // Label
    _essenceName=new MultilineLabel();
    setEssence(null);
    // Delete button
    ImageIcon icon=IconsManager.getIcon("/resources/gui/icons/cross.png");
    _deleteButton=GuiFactory.buildButton("");
    _deleteButton.setIcon(icon);
    _deleteButton.setMargin(new Insets(0,0,0,0));
    _deleteButton.setContentAreaFilled(false);
    _deleteButton.setBorderPainted(false);
    _deleteButton.addActionListener(listener);
  }

  /**
   * Set the listener for this controller.
   * @param listener A listener.
   */
  public void setListener(EssenceUpdatedListener listener)
  {
    _listener=listener;
  }

  private void handleButtonClick(JButton button)
  {
    if (button==_essenceIconButton)
    {
      Essence essence=EssenceChoice.chooseEssence(_parent,_attrs,_type);
      if (essence!=null)
      {
        setEssence(essence);
        if (_listener!=null)
        {
          _listener.essenceUpdated(this);
        }
      }
    }
    else if (button==_deleteButton)
    {
      setEssence(null);
      if (_listener!=null)
      {
        _listener.essenceUpdated(this);
      }
    }
  }

  /**
   * Get the managed essence.
   * @return the managed essence.
   */
  public Essence getEssence()
  {
    return _essence;
  }

  /**
   * Set current essence.
   * @param essence Essence to set.
   */
  public void setEssence(Essence essence)
  {
    // Store essence
    _essence=essence;
    // Set essence icon
    Icon icon=null;
    if (essence!=null)
    {
      icon=ItemUiTools.buildItemIcon(essence);
    }
    else
    {
      icon=LotroIconsManager.getDefaultEssenceIcon(_type.getCode());
    }
    _essenceIconButton.setIcon(icon);
    // Text
    String text="";
    if (essence!=null)
    {
      text=essence.getName();
    }
    _essenceName.setText(text,_linesCount);
  }

  /**
   * Get the managed essence button.
   * @return the managed essence button.
   */
  public JButton getEssenceButton()
  {
    return _essenceIconButton;
  }

  /**
   * Get the label for the essence.
   * @return a label.
   */
  public JPanel getEssenceNameLabel()
  {
    return _essenceName;
  }

  /**
   * Get the delete button associated with this essence.
   * @return a button.
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
    _essence=null;
    // Controllers
    _parent=null;
    // UI
    _essenceIconButton=null;
    _essenceName=null;
    _deleteButton=null;
    // Listeners
    _listener=null;
  }
}

package delta.games.lotro.gui.utils.toolbar;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;

/**
 * Controller for a toolbar.
 * @author DAM
 */
public class ToolbarController implements ActionListener
{
  private ToolbarModel _model;
  private JToolBar _toolBar;
  private HashMap<String,JButton> _items;
  private List<ActionListener> _actionListeners;

  /**
   * Constructor.
   */
  public ToolbarController()
  {
    _items=new HashMap<String,JButton>();
    _model=new ToolbarModel();
    _actionListeners=new ArrayList<ActionListener>();
  }

  /**
   * Get the managed model.
   * @return the managed model.
   */
  public ToolbarModel getModel()
  {
    return _model;
  }

  /**
   * Get the managed GUI (build it if necessary).
   * @return the managed GUI.
   */
  public JToolBar getToolBar()
  {
    if (_toolBar==null)
    {
      updateGUI();
    }
    return _toolBar;
  }

  private void updateGUI()
  {
    if (_toolBar==null)
    {
      _toolBar=new JToolBar();
      _toolBar.setBackground(GuiFactory.getBackgroundColor());
      _toolBar.setOpaque(false);
      _toolBar.setFloatable(false);
    }
    else
    {
      _toolBar.removeAll();
    }
    ToolbarItem[] items=_model.getItems();
    int nb=items.length;
    for(int i=0;i<nb;i++)
    {
      ToolbarItem item=items[i];
      if (item instanceof ToolbarIconItem)
      {
        JButton button=buildButton((ToolbarIconItem)item);
        _toolBar.add(button);
      }
      else if (item instanceof ToolbarSeparatorItem)
      {
        _toolBar.addSeparator();
      }
    }
    
    _toolBar.add(Box.createHorizontalGlue());
  }

  private JButton buildButton(ToolbarIconItem item)
  {
    JButton button=GuiFactory.buildButton("");
    String itemId=item.getItemId();
    if (item!=null)
    {
      button.setName(itemId);
    }
    String actionId=item.getActionId();
    if (actionId!=null)
    {
      button.setActionCommand(actionId);
    }
    String toolTipText=item.getTooltip();
    if (toolTipText!=null)
    {
      button.setToolTipText(toolTipText);
    }
    button.addActionListener(this);
  
    Icon icon=null;
    String iconPath=item.getIconPath();
    if (iconPath!=null)
    {
      icon=IconsManager.getIcon(iconPath);
    }
    // no image found
    if (icon!=null)
    {
      button.setIcon(icon);
    }
    else
    {
      String altText=item.getAlternativeText();
      if (altText==null) altText="???";
      button.setText(altText);
    }
    button.setMargin(new Insets(1,1,1,1));
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    //button.setRequestFocusEnabled(false);
    //button.setFocusPainted(false);
    button.setEnabled(true);
    _items.put(itemId,button);
    return button;
  }

  /**
   * Buttons callback.
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source instanceof JButton)
    {
      JButton button=(JButton)source;
      String action=button.getActionCommand();
      invokeAction(e,action);
    }
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _actionListeners.add(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _actionListeners.remove(al);
  }

  /**
   * Invoke an action!
   * @param e Source event.
   * @param actionId Identifier of action to invoke.
   */
  private void invokeAction(ActionEvent e,String actionId)
  {
    ActionListener[] als=_actionListeners.toArray(new ActionListener[_actionListeners.size()]);
    for(ActionListener al : als)
    {
      al.actionPerformed(e);
    }
  }

  /**
   * Set item state.
   * @param itemId Identifier of the targeted item.
   * @param visible <code>true</code> to show it, <code>false</code> to hide it.
   * @param enabled <code>true</code> to enable it, <code>false</code> to disable it.
   */
  public void setItemState(String itemId, boolean visible, boolean enabled)
  {
    JButton item=_items.get(itemId);
    if (item!=null)
    {
      item.setVisible(visible);
      item.setEnabled(enabled);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _model=null;
    if (_toolBar!=null)
    {
      _toolBar.removeAll();
      _toolBar=null;
    }
    _items=null;
  }
}

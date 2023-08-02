package delta.games.lotro.gui.character.virtues;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.text.NumberEditionController;
import delta.common.ui.swing.text.NumberListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.gui.lore.virtues.VirtueUiTools;

/**
 * Gather UI items to edit a single virtue.
 * @author DAM
 */
public class VirtueEditionUiController implements ActionListener
{
  // Data
  private VirtueDescription _virtue;
  private int _tier;
  private int _bonus;
  private int _characterLevel;
  // Controllers/UI
  private VirtueIconController _iconController;
  private HyperLinkController _virtueName;
  private JButton _plus;
  private IntegerEditionController _tierEdit;
  private JButton _minus;
  private JPanel _panel;
  // Listener
  private TierValueListener _listener;

  /**
   * Tier value listener.
   * @author DAM
   */
  public interface TierValueListener
  {
    /**
     * Called when the tier of the managed virtue has changed.
     * @param virtue Targeted virtue.
     * @param tier New tier value.
     */
    void tierChanged(VirtueDescription virtue, int tier);
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param virtue Virtue to use.
   * @param characterLevel Character level.
   */
  public VirtueEditionUiController(WindowController parent, VirtueDescription virtue, int characterLevel)
  {
    _virtue=virtue;
    _tier=0;
    _characterLevel=characterLevel;
    // Icon
    _iconController=new VirtueIconController(virtue,false);
    JLabel label=_iconController.getLabel();
    label.setTransferHandler(new DragTransferHandler(virtue));
    label.setName(virtue.getName());
    label.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        JComponent lab=(JComponent)e.getSource();
        TransferHandler handle=lab.getTransferHandler();
        handle.exportAsDrag(lab,e,TransferHandler.COPY);
      }
    });
    // Virtue name
    JLabel virtueNameLabel=GuiFactory.buildLabel("");
    _virtueName=VirtueUiTools.buildVirtueLink(parent,virtue,virtueNameLabel);
    // "+" button
    _plus=buildButton("circled_plus");
    // "-" button
    _minus=buildButton("circled_minus");
    // Tier editor
    JTextField tierEditTextField=GuiFactory.buildTextField("");
    _tierEdit=new IntegerEditionController(tierEditTextField,3);
    int maxRank=virtue.getMaxRank(_characterLevel);
    _tierEdit.setValueRange(Integer.valueOf(0),Integer.valueOf(maxRank));
    NumberListener<Integer> valueListener=new NumberListener<Integer>()
    {
      @Override
      public void valueChanged(NumberEditionController<Integer> source, Integer newValue)
      {
        if (newValue!=null)
        {
          _tier=newValue.intValue();
          if (_listener!=null)
          {
            _listener.tierChanged(_iconController.getVirtue(),_tier);
          }
          updateUi();
        }
      }
    };
    _tierEdit.addValueListener(valueListener);
    // Panel
    _panel=buildPanel();
    // Listener
    _listener=null;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(_iconController.getLabel(),c);
    c=new GridBagConstraints(1,0,3,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(_virtueName.getLabel(),c);
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(_minus,c);
    c=new GridBagConstraints(2,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(_tierEdit.getTextField(),c);
    c=new GridBagConstraints(3,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(_plus,c);
    Component strut=Box.createHorizontalStrut(120);
    c=new GridBagConstraints(0,2,4,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(strut,c);
    return panel;
  }

  private static class DragTransferHandler extends TransferHandler
  {
    private VirtueDescription _virtue;

    @Override
    protected Transferable createTransferable(JComponent c)
    {
      Transferable t=new Transferable()
      {
        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
          return flavor==DataFlavor.stringFlavor;
        }
        @Override
        public DataFlavor[] getTransferDataFlavors()
        {
          return new DataFlavor[] { DataFlavor.stringFlavor };
        }
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
          return Integer.valueOf(_virtue.getIdentifier());
        }
      };
      return t;
    }
    public DragTransferHandler(VirtueDescription virtue)
    {
      super("text");
      _virtue=virtue;
    }

    @Override
    public boolean canImport(TransferSupport support)
    {
      return false;
    }
  }

  /**
   * Set a tier value listener.
   * @param listener Listener to set.
   */
  public void setListener(TierValueListener listener)
  {
    _listener=listener;
  }

  private JButton buildButton(String key)
  {
    JButton button=GuiFactory.buildIconButton("/resources/gui/icons/"+key+".png");
    button.addActionListener(this);
    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (_plus==source)
    {
      int maxRank=_virtue.getMaxRank(_characterLevel);
      if (_tier<maxRank)
      {
        _tier++;
        if (_listener!=null)
        {
          _listener.tierChanged(_iconController.getVirtue(),_tier);
        }
        updateUi();
      }
    }
    if (_minus==source)
    {
      if (_tier>0)
      {
        _tier--;
        if (_listener!=null)
        {
          _listener.tierChanged(_iconController.getVirtue(),_tier);
        }
        updateUi();
      }
    }
  }

  /**
   * Get the displayed tier.
   * @return a tier value.
   */
  public int getTier()
  {
    return _tier;
  }

  /**
   * Get the displayed bonus.
   * @return a bonus value.
   */
  public int getBonus()
  {
    return _bonus;
  }

  /**
   * Set the tier for the managed virtue.
   * @param tier Tier to set.
   */
  public void setTier(int tier)
  {
    _tier=tier;
    updateUi();
  }

  /**
   * Set the tier and bonus for the managed virtue.
   * @param tier Tier to set.
   * @param bonus Bonus ranks for the managed virtue.
   */
  public void init(int tier,int bonus)
  {
    _tier=tier;
    _bonus=bonus;
    updateUi();
  }

  private void updateUi()
  {
    _iconController.setTier(_tier);
    _iconController.setBonus(_bonus);
    _tierEdit.setValue(Integer.valueOf(_tier));
    _minus.setEnabled(_tier>0);
    int maxRank=_virtue.getMaxRank(_characterLevel);
    _plus.setEnabled(_tier<maxRank);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _virtue=null;
    if (_iconController!=null)
    {
      _iconController.dispose();
      _iconController=null;
    }
    if (_virtueName!=null)
    {
      _virtueName.dispose();
      _virtueName=null;
    }
    _plus=null;
    if (_tierEdit!=null)
    {
      _tierEdit.dispose();
      _tierEdit=null;
    }
    _minus=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _listener=null;
  }
}

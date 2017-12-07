package delta.games.lotro.gui.character.virtues;

import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.common.VirtueId;

/**
 * Gather UI items to edit a single virtue.
 * @author DAM
 */
public class VirtueEditionUiController implements ActionListener
{
  private int _tier;
  private VirtueIconController _iconController;
  private JButton _plus;
  private JButton _minus;
  private TierValueListener _listener;

  /**
   * Tier value listener.
   * @author DAM
   */
  public interface TierValueListener
  {
    /**
     * Called when the tier of the managed virtue has changed.
     * @param virtueId Targeted virtue.
     * @param tier New tier value.
     */
    void tierChanged(VirtueId virtueId, int tier);
  }

  /**
   * Constructor.
   * @param virtueId Virtue to use.
   * @param panel Parent panel.
   */
  public VirtueEditionUiController(VirtueId virtueId, JPanel panel)
  {
    _tier=0;
    _iconController=new VirtueIconController(virtueId);
    JLabel label=_iconController.getLabel();

    label.setTransferHandler(new DragTransferHandler(virtueId));
    label.setName(virtueId.name());
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

    panel.add(label);
    _plus=buildButton('+');
    panel.add(_plus);
    _minus=buildButton('-');
    panel.add(_minus);
    _listener=null;
  }

  private static class DragTransferHandler extends TransferHandler
  {
    private VirtueId _virtueId;

    @Override
    protected Transferable createTransferable(JComponent c)
    {
      Transferable t=new Transferable()
      {
        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
          return flavor==DataFlavor.stringFlavor;
        }
        public DataFlavor[] getTransferDataFlavors()
        {
          return new DataFlavor[] { DataFlavor.stringFlavor };
        }
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
          return _virtueId;
        }
      };
      return t;
    }
    public DragTransferHandler(VirtueId virtueId)
    {
      super("text");
      _virtueId=virtueId;
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

  /**
   * Set the location of the managed UI items in the parent panel.
   * @param x Base horizontal location.
   * @param y Base vertical location.
   */
  public void setLocation(int x, int y)
  {
    JLabel label=_iconController.getLabel();
    x-=label.getWidth()/2;
    y-=label.getHeight()/2;
    label.setLocation(x,y);
    int buttonX=x+label.getWidth()+2;
    int buttonY=y+(label.getHeight()-_plus.getHeight())/2;
    _plus.setLocation(buttonX,buttonY-7);
    _minus.setLocation(buttonX,buttonY+7);
  }

  private JButton buildButton(char text)
  {
    JButton button=GuiFactory.buildButton(String.valueOf(text));
    button.setSize(15,15);
    button.setMargin(new Insets(1,1,1,1));
    button.setContentAreaFilled(false);
    button.setBorderPainted(true);
    button.addActionListener(this);
    return button;
  }

  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (_plus==source)
    {
      if (_tier<VirtuesSet.MAX_TIER)
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
   * Set the tier for the managed virtue.
   * @param tier Tier to set.
   */
  public void setTier(int tier)
  {
    _tier=tier;
    updateUi();
  }

  private void updateUi()
  {
    _iconController.setTier(_tier);
    _minus.setEnabled(_tier>0);
    _plus.setEnabled(_tier<VirtuesSet.MAX_TIER);
  }
}

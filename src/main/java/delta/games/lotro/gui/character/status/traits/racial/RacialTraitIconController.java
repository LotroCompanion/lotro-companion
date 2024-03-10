package delta.games.lotro.gui.character.status.traits.racial;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.lore.traits.TraitIconController;
import delta.games.lotro.gui.lore.traits.TraitUiUtils;

/**
 * A draggable trait icon.
 * @author DAM
 */
public class RacialTraitIconController extends TraitIconController
{
  // Controllers/UI
  private HyperLinkController _traitName;
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param trait Trait to use.
   * @param characterLevel Character level.
   */
  public RacialTraitIconController(WindowController parent, TraitDescription trait, int characterLevel)
  {
    super(parent,trait,characterLevel,false);
    JButton button=getIcon();
    button.setTransferHandler(new DragTransferHandler(trait));
    button.setName(trait.getName());
    button.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        JComponent lab=(JComponent)e.getSource();
        TransferHandler handle=lab.getTransferHandler();
        handle.exportAsDrag(lab,e,TransferHandler.COPY);
      }
    });
    // Name
    JLabel nameLabel=GuiFactory.buildLabel("");
    _traitName=TraitUiUtils.buildTraitLink(parent,trait,nameLabel);
    // Panel
    _panel=buildPanel();
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(getComponent(),c);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(_traitName.getLabel(),c);
    Component strut=Box.createHorizontalStrut(120);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(strut,c);
    return panel;
  }

  private static class DragTransferHandler extends TransferHandler
  {
    private TraitDescription _trait;

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
          return String.valueOf(_trait.getIdentifier());
        }
      };
      return t;
    }

    public DragTransferHandler(TraitDescription trait)
    {
      super("text");
      _trait=trait;
    }

    @Override
    public boolean canImport(TransferSupport support)
    {
      return false;
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_traitName!=null)
    {
      _traitName.dispose();
      _traitName=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}

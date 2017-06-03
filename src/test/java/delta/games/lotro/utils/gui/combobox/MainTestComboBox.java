package delta.games.lotro.utils.gui.combobox;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Test for the combo box controller.
 * @author DAM
 */
public class MainTestComboBox implements ItemSelectionListener<Integer>
{
  private ComboBoxController<Integer> _ctrl;

  private void doIt()
  {
    _ctrl=buildComboBox();
    JFrame f=new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel=new JPanel(new BorderLayout());
    JComboBox comboBox=_ctrl.getComboBox();
    panel.add(comboBox,BorderLayout.CENTER);
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
  }

  private ComboBoxController<Integer> buildComboBox()
  {
    int nbLevels=10;
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>(true,Integer.class);
    List<Integer> levels=new ArrayList<Integer>();
    for(int i=1;i<=nbLevels;i++)
    {
      levels.add(Integer.valueOf(i));
    }
    for(Integer level : levels)
    {
      ctrl.addItem(level,level.toString());
    }
    ctrl.selectItem(levels.get(nbLevels-1));
    ctrl.addListener(this);
    return ctrl;
  }

  public void itemSelected(Integer item)
  {
    System.out.println(item);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestComboBox().doIt();
  }
}

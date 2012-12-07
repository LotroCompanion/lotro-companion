package delta.games.lotro.tools.lore.warbands;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.common.SIZE;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandsRegistry;
import delta.games.lotro.lore.warbands.io.xml.WarbandsRegistryXMLWriter;

/**
 * @author DAM
 */
public class WarbandsGenerator
{
  private static final String WOLD="The Wold";
  private static final String NORCROFTS="Norcrofts";
  private static final String ENTWASH_VALE="Entwash Vale";
  private static final String SUTCROFTS="Sutcrofts";
  
  private WarbandsRegistry buildRegistry()
  {
    WarbandsRegistry registry=new WarbandsRegistry();
    // Cinder
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Cinder");
      w.setRegion(WOLD);
      w.setLevel(Integer.valueOf(77));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("A huge Salamander with two allies (Minions) - Typically found near Venomtongue in the Seething Mire west of Harwick.");
      registry.addWarband(w);
    }
    // Hanrun
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Hanrun");
      w.setRegion(WOLD);
      w.setLevel(Integer.valueOf(77));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("A large, saddled warg notably missing his rider, escorted by two smaller wargs, who roves south of Feldburg.");
      registry.addWarband(w);
    }
    // Urush
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Urush");
      w.setRegion(WOLD);
      w.setLevel(Integer.valueOf(77));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("A mounted Easterling, leading a band of other Easterling riders through the hills west of Jóshkhin Orda.");
      registry.addWarband(w);
    }
    // Bughrakh
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Bughrakh");
      w.setRegion(WOLD);
      w.setLevel(Integer.valueOf(77));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SMALL_FELLOWSHIP);
      w.setDescription("An orc commander who leads a patrol on foot, typically in the woods east of Floodwend.");
      registry.addWarband(w);
    }

    // Dahámab
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Dahámab");
      w.setRegion(NORCROFTS);
      w.setLevel(Integer.valueOf(79));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("The leader of a mounted Uruk patrol southwest of Cliving.");
      registry.addWarband(w);
    }
    // Skútog
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Skútog");
      w.setRegion(NORCROFTS);
      w.setLevel(Integer.valueOf(79));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("Skútog and his warg-riders have been spotted patrolling the river crossing near Caddabrand's Camp.");
      registry.addWarband(w);
    }
    // Swertríper
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Swertríper");
      w.setRegion(NORCROFTS);
      w.setLevel(Integer.valueOf(79));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("A massive láthbear who roams the woods south of Elthengels, along with a pair of smaller láthbears.");
      registry.addWarband(w);
    }
    // Haglob
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Haglob");
      w.setRegion(NORCROFTS);
      w.setLevel(Integer.valueOf(79));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SMALL_FELLOWSHIP);
      w.setDescription("A mighty troll often found northwest of Elthengels, near the border with the Wold. He is his own warband.");
      registry.addWarband(w);
    }

    // Bensengan
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Bensengan");
      w.setRegion(ENTWASH_VALE);
      w.setLevel(Integer.valueOf(80));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("The drakeling Bensengan leads an aerial warband over the eastern fields of the Entwash Vale.");
      registry.addWarband(w);
    }
    // Mâthum
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Mâthum");
      w.setRegion(ENTWASH_VALE);
      w.setLevel(Integer.valueOf(80));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("Mâthum and his warg-riders patrol the fields north of Eaworth.");
      registry.addWarband(w);
    }
    // Dâl
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Dâl");
      w.setRegion(ENTWASH_VALE);
      w.setLevel(Integer.valueOf(80));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("The mighty Dâl leads a band of mounted uruks across the hillside east of Eaworth.");
      registry.addWarband(w);
    }
    // Fearrhorn
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Fearrhorn");
      w.setRegion(ENTWASH_VALE);
      w.setLevel(Integer.valueOf(80));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SOLO);
      w.setDescription("A massive auroch, Fearrhorn and his herd can often be found grazing in the eastern fields of the Entash Vale.");
      registry.addWarband(w);
    }
    // Mirz
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Mirz");
      w.setRegion(ENTWASH_VALE);
      w.setLevel(Integer.valueOf(81));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SMALL_FELLOWSHIP);
      w.setDescription("The half-orc Mirz heads a band of dunlendings, goblins, and orcs in the fields southeast of Thornhope.");
      registry.addWarband(w);
    }
    // Urgai
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Urgai");
      w.setRegion(ENTWASH_VALE);
      w.setLevel(Integer.valueOf(81));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.FELLOWSHIP);
      w.setDescription("Urgai, Black Uruk of Mordor, leads a band in the hills southeast of Eaworth.");
      registry.addWarband(w);
    }

    // Gundul
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Craban-master Gundul");
      w.setRegion(SUTCROFTS);
      w.setLevel(Integer.valueOf(85));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.SMALL_FELLOWSHIP);
      w.setDescription("Gundul and his sizable flock of crebain can be found wandering the ravaged crofts southeast of Garsfeld.");
      registry.addWarband(w);
    }
    // Kramp
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Kramp");
      w.setRegion(SUTCROFTS);
      w.setLevel(Integer.valueOf(85));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.FELLOWSHIP);
      w.setDescription("Kramp's band of warg-riders rove through the central fields of the Sutcrofts, often coming dangerously close to Snowborn.");
      registry.addWarband(w);
    }
    // Bugud
    {
      WarbandDefinition w=new WarbandDefinition();
      w.setName("Warmaster Bugud");
      w.setRegion(SUTCROFTS);
      w.setLevel(Integer.valueOf(85));
      w.setMorale(Integer.valueOf(0));
      w.setSize(SIZE.RAID);
      w.setDescription("Warmaster Bugud oversees a powerful warband in the eastern fields of the Sutcrofts, south of Hytbold.");
      registry.addWarband(w);
    }
    return registry;
  }

  private void doIt()
  {
    Config cfg=Config.getInstance();
    File configDir=cfg.getConfigDir();
    File warbandsFile=new File(configDir,"warbands.xml");
    WarbandsRegistry r=buildRegistry();
    WarbandsRegistryXMLWriter w=new WarbandsRegistryXMLWriter();
    w.write(warbandsFile,r,EncodingNames.UTF_8);
  }

  /**
   * Main for the warbands generator.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new WarbandsGenerator().doIt();
  }

}

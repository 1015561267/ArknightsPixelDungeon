package com.unifier.arknightspixeldungeon.scripts.NPCPlot;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.mobs.FetidRat;
import com.unifier.arknightspixeldungeon.actors.mobs.GnollTrickster;
import com.unifier.arknightspixeldungeon.actors.mobs.GreatCrab;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.actors.mobs.npcs.Ghost;
import com.unifier.arknightspixeldungeon.actors.mobs.npcs.NPC;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.armor.LeatherArmor;
import com.unifier.arknightspixeldungeon.items.armor.MailArmor;
import com.unifier.arknightspixeldungeon.items.armor.PlateArmor;
import com.unifier.arknightspixeldungeon.items.armor.ScaleArmor;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Shortsword;
import com.unifier.arknightspixeldungeon.journal.Notes;
import com.unifier.arknightspixeldungeon.levels.SewerLevel;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scripts.Choice;
import com.unifier.arknightspixeldungeon.scripts.Plot;
import com.unifier.arknightspixeldungeon.scripts.Script;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndDialog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class FrostNovaQuestPlot extends Plot {

    {
        process = 1 ;
    }

    @Override
    protected String getPlotName() {
        return FROSTNOVA_QUEST_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        if(Quest.starting() || Quest.completed())
        {
            if(Quest.completed())
            {
                process = 14;
            }

            while(this.process < needed_process )
            {
                this.process();
            }
        }
        else if(Quest.processing())
        {
            process_to_13();
        }
        else if(Quest.rewarding())
        {
            process_to_14();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
            if (Quest.starting()) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
                        break;
                    case 2:
                        process_to_2();
                        break;
                    case 3:
                        process_to_3();
                        break;
                    case 4:
                        process_to_4();
                        break;
                    case 5:
                        process_to_5();
                        break;
                    case 6:
                        process_to_6();
                        break;
                    case 7:
                        process_to_7();
                        break;
                    case 8:
                        process_to_8();
                        break;
                    case 9:
                        process_to_9();
                        break;
                    case 10:
                        process_to_10();
                        break;
                    case 11:
                        process_to_11();
                        break;
                    case 12:
                        process_to_12();
                        break;
                    case 13:
                        diagulewindow.hide();
                        break;
                }
                diagulewindow.update();
                process++;
            }
            else if(Quest.processing())
            {
                diagulewindow.hide();
            }
            else if(Quest.rewarding())
            {
                diagulewindow.hide();
            }
            else if(Quest.completed())
            {
                switch (process)
                {
                    case 15:
                        process_to_15();
                        break;
                    case 16:
                        process_to_16();
                        break;
                    case 17:
                        process_to_17();
                        break;
                    case 18:
                        process_to_18();
                        break;
                    case 19:
                        process_to_19();
                        break;
                    case 20:
                        process_to_20();
                        break;
                    case 21:
                        process_to_21();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }
    }

    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        if (Quest.starting()) {
            process = 2;//Note this avoid the first dialog be repeated once
            process_to_1();
        }
        else if(Quest.processing())
        {
            process_to_13();
        }
        else if(Quest.rewarding())
        {
            process_to_14();
        }
        else if(Quest.completed()) {
            process = 15;
            process_to_15();
        }
    }

    @Override
    public boolean end() {

        if(process == 13 || process == 14 || process > 21 )
        {
            return true;
        }
        else return false;
    }

    @Override
    public void skip() {
        if (Quest.starting()) {
          if(process < 12) {
              while (process < 12)
                  process();
          }
          else
              diagulewindow.skipWait();
        }
        else if(Quest.processing() || Quest.completed())
        {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }
        else if(Quest.rewarding())
        {
            diagulewindow.skipWait();
        }
    }

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.FROSTNOVA));
        diagulewindow.darkenMainAvatar();

        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.AMIYA));
        diagulewindow.setRightName(Script.Name(Script.Character.AMIYA));

        diagulewindow.changeText(Messages.get(this, "txt1"));
    }

    private void process_to_2()
    {
        diagulewindow.darkenSecondAvatar();
        diagulewindow.lightenMainAvatar();

        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        diagulewindow.changeText(Messages.get(this, "txt2"));
    }

    private void process_to_3()
    {

        diagulewindow.darkenMainAvatar();

        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.CHEN));
        diagulewindow.setRightName(Script.Name(Script.Character.CHEN));

        diagulewindow.changeText(Messages.get(this, "txt3"));
    }

    private void process_to_4()
    {
        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.RED));
        diagulewindow.setRightName(Script.Name(Script.Character.RED));

        diagulewindow.changeText(Messages.get(this, "txt4"));
    }

    private void process_to_5()
    {
        diagulewindow.darkenThirdAvatar();

        diagulewindow.lightenMainAvatar();
        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        diagulewindow.changeText(Messages.get(this, "txt5"));
    }
    private void process_to_6()
    {
        diagulewindow.darkenMainAvatar();

        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.CHEN));
        diagulewindow.lightenThirdAvatar();
        diagulewindow.setRightName(Script.Name(Script.Character.CHEN));

        diagulewindow.changeText(Messages.get(this, "txt6"));
    }
    private void process_to_7()
    {
        diagulewindow.darkenThirdAvatar();

        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.EXUSIAI));
        diagulewindow.lightenSecondAvatar();
        diagulewindow.setRightName(Script.Name(Script.Character.EXUSIAI));

        diagulewindow.secondAvatarToFront();
        diagulewindow.changeText(Messages.get(this, "txt7"));

        process = 7;
    }
    private void process_to_8()
    {
        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.AMIYA));
        diagulewindow.setRightName(Script.Name(Script.Character.AMIYA));

        diagulewindow.changeText(Messages.get(this, "txt8"));
    }
    private void process_to_9()
    {
        diagulewindow.darkenSecondAvatar();
        diagulewindow.lightenThirdAvatar();

        diagulewindow.setRightName(Script.Name(Script.Character.CHEN));
        diagulewindow.thirdAvaratToFront();

        diagulewindow.changeText(Messages.get(this, "txt9"));
    }

    private void process_to_10()
    {
        diagulewindow.darkenThirdAvatar();
        diagulewindow.lightenMainAvatar();
        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        diagulewindow.changeText(Messages.get(this, "txt10"));
    }

    private void process_to_11()
    {
        diagulewindow.lightenMainAvatar();
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.FROSTNOVA));
        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        switch (Quest.type)
        {
            default:
            case 1:diagulewindow.changeText(Messages.get(this, "quest1"));break;
            case 2:diagulewindow.changeText(Messages.get(this, "quest2"));break;
            case 3:diagulewindow.changeText(Messages.get(this, "quest3"));break;
        }
    }

    private void process_to_12()
    {
        generateMob();

        diagulewindow.lightenMainAvatar();
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.FROSTNOVA));
        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        switch (Quest.type)
        {
            default:
            case 1:diagulewindow.changeText(Messages.get(this, "quest1_desc"));break;
            case 2:diagulewindow.changeText(Messages.get(this, "quest2_desc"));break;
            case 3:diagulewindow.changeText(Messages.get(this, "quest3_desc"));break;
        }
    }

    private void generateMob()
    {
        Mob questBoss;
        switch (FrostNovaQuestPlot.Quest.type){
            case 1: default:
                questBoss = new FetidRat(); break;
            case 2:
                questBoss = new GnollTrickster(); break;
            case 3:
                questBoss = new GreatCrab();break;
        }
        questBoss.pos = Dungeon.level.randomRespawnCell();
        if (questBoss.pos != -1) {
            GameScene.add(questBoss);
            FrostNovaQuestPlot.Quest.given = true;
            Notes.add( Notes.Landmark.GHOST );
        }
        Quest.given = true;
    }

    private void process_to_13()
    {
        diagulewindow.hideAll();

        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.FROSTNOVA));
        diagulewindow.lightenMainAvatar();
        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        switch (Quest.type)
        {
            default:
            case 1:diagulewindow.changeText(Messages.get(this, "quest_unfinished1"));break;
            case 2:diagulewindow.changeText(Messages.get(this, "quest_unfinished2"));break;
            case 3:diagulewindow.changeText(Messages.get(this, "quest_unfinished3"));break;
        }

        process = 13;
    }

    private void process_to_14()
    {
        diagulewindow.hideAll();

        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.FROSTNOVA));
        diagulewindow.lightenMainAvatar();

        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        diagulewindow.changeText(Messages.get(this, "txt11"));

        diagulewindow.haveChoice(new ChoiceWeapon(),new ChoiceArmor(),new ChoiceCancel());
    }

    private void process_to_15()
    {
        diagulewindow.hideAll();

        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.FROSTNOVA));
        diagulewindow.lightenMainAvatar();

        diagulewindow.setLeftName(Script.Name(Script.Character.FROSTNOVA));
        diagulewindow.changeText(Messages.get(this, "txt12"));

        diagulewindow.resizeAfterChoice();
    }


    private void process_to_16()
    {
        diagulewindow.changeText(Messages.get(this, "txt13"));
    }

    private void process_to_17()
    {
        diagulewindow.changeText(Messages.get(this, "txt14"));

        diagulewindow.hideMainAvatar();

        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.CHEN));
        diagulewindow.lightenSecondAvatar();

        diagulewindow.setRightName(Script.Name(Script.Character.CHEN));
    }

    private void process_to_18()
    {
        diagulewindow.changeText(Messages.get(this, "txt15"));

        diagulewindow.darkenSecondAvatar();

        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.EXUSIAI));
        diagulewindow.lightenThirdAvatar();

        diagulewindow.setRightName(Script.Name(Script.Character.EXUSIAI));

    }

    private void process_to_19()
    {
        diagulewindow.changeText(Messages.get(this, "txt16"));

        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.RED));
        diagulewindow.lightenThirdAvatar();

        diagulewindow.setRightName(Script.Name(Script.Character.RED));
    }

    private void process_to_20()
    {
        diagulewindow.darkenThirdAvatar();
        diagulewindow.lightenSecondAvatar();

        diagulewindow.secondAvatarToFront();

        diagulewindow.setRightName(Script.Name(Script.Character.CHEN));

        diagulewindow.changeText(Messages.get(this, "txt17"));
    }

    private void process_to_21()
    {
        diagulewindow.darkenSecondAvatar();

        diagulewindow.changeText(Messages.get(this, "txt18"));

        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.AMIYA));
        diagulewindow.lightenThirdAvatar();
        diagulewindow.thirdAvaratToFront();

        diagulewindow.setRightName(Script.Name(Script.Character.AMIYA));
    }

    public static class Quest {

        private static boolean spawned;

        public static int type;

        public static boolean given;
        public static boolean processed;

        private static int depth;

        public static Weapon weapon;
        public static Armor armor;

        public static NPC ghost;

        public static void reset() {
            spawned = false;

            weapon = null;
            armor = null;
        }

        private static final String NODE		= "sadGhost";

        private static final String GHOST       = "ghost";
        private static final String SPAWNED		= "spawned";
        private static final String TYPE        = "type";
        private static final String GIVEN		= "given";
        private static final String PROCESSED	= "processed";
        private static final String DEPTH		= "depth";
        private static final String WEAPON		= "weapon";
        private static final String ARMOR		= "armor";

        public static void storeInBundle( Bundle bundle ) {

            Bundle node = new Bundle();

            node.put( SPAWNED, spawned );

            if (spawned) {

                node.put( TYPE, type );

                node.put( GIVEN, given );
                node.put( DEPTH, depth );
                node.put( PROCESSED, processed);

                node.put( WEAPON, weapon );
                node.put( ARMOR, armor );
            }

            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {

            Bundle node = bundle.getBundle( NODE );

            if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

                type = node.getInt(TYPE);
                given	= node.getBoolean( GIVEN );
                processed = node.getBoolean( PROCESSED );

                depth	= node.getInt( DEPTH );

                weapon	= (Weapon)node.get( WEAPON );
                armor	= (Armor)node.get( ARMOR );
            } else {
                reset();
            }
        }

        public static void spawn( SewerLevel level ) {
            if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {

                Ghost ghost = new Ghost();
                do {
                    ghost.pos = level.randomRespawnCell();
                } while (ghost.pos == -1);
                level.mobs.add( ghost );

                spawned = true;
                //dungeon depth determines type of quest.
                //depth2=fetid rat, 3=gnoll trickster, 4=great crab
                type = Dungeon.depth-1;

                given = false;
                processed = false;
                depth = Dungeon.depth;

                //50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
                float itemTierRoll = Random.Float();
                int wepTier;

                if (itemTierRoll < 0.5f) {
                    wepTier = 2;
                    armor = new LeatherArmor();
                } else if (itemTierRoll < 0.8f) {
                    wepTier = 3;
                    armor = new MailArmor();
                } else if (itemTierRoll < 0.95f) {
                    wepTier = 4;
                    armor = new ScaleArmor();
                } else {
                    wepTier = 5;
                    armor = new PlateArmor();
                }

                try {
                    do {
                        weapon = (Weapon) Generator.wepTiers[wepTier - 1].classes[Random.chances(Generator.wepTiers[wepTier - 1].probs)].newInstance();
                    } while (!(weapon instanceof MeleeWeapon));
                } catch (Exception e){
                    ArknightsPixelDungeon.reportException(e);
                    weapon = new Shortsword();
                }

                //50%:+0, 30%:+1, 15%:+2, 5%:+3
                float itemLevelRoll = Random.Float();
                int itemLevel;
                if (itemLevelRoll < 0.5f){
                    itemLevel = 0;
                } else if (itemLevelRoll < 0.8f){
                    itemLevel = 1;
                } else if (itemLevelRoll < 0.95f){
                    itemLevel = 2;
                } else {
                    itemLevel = 3;
                }
                weapon.upgrade(itemLevel);
                armor.upgrade(itemLevel);

                //10% to be enchanted
                if (Random.Int(10) == 0){
                    weapon.enchant();
                    armor.inscribe();
                }

            }
        }

        public static void process() {
            if (spawned && given && !processed && (depth == Dungeon.depth)) {
                GLog.n( Messages.get(Ghost.class, "find_me") );
                Sample.INSTANCE.play( Assets.SND_GHOST );
                processed = true;
            }
        }

        public static void complete() {
            weapon = null;
            armor = null;
            Notes.remove( Notes.Landmark.GHOST );
        }

        public static boolean starting() {return spawned && !given;}

        public static boolean processing(){
            return spawned && !processed;
        }

        public static boolean processed(){
            return spawned && processed;
        }

        public static boolean rewarding(){
            return processed() && (weapon !=null || armor != null);
        }
        public static void setGhost(NPC ghost)
        {
            Quest.ghost = ghost;
        }

        public static boolean completed(){
            return processed() && weapon == null && armor == null;
        }
    }

    public static class ChoiceWeapon extends Choice {
        {
            text = Messages.get(FrostNovaQuestPlot.class, "choose1",Quest.weapon.name());
        }

        @Override
        public void react() {

            Item reward = Quest.weapon;

            if (reward == null) return;

            reward.identify();

            for(Mob mob : Dungeon.level.mobs)
            {
                if(mob instanceof Ghost)
                {
                    Quest.setGhost((NPC) mob);
                }
            }

            if (reward.doPickUp( Dungeon.hero )) {
                GLog.i( Messages.get(Dungeon.hero, "you_now_have", reward.name()) );
            } else {
                Dungeon.level.drop( reward, Quest.ghost.pos).sprite.drop();
            }



            Quest.ghost.die(null);
            FrostNovaQuestPlot.Quest.complete();

            WndDialog.settedPlot.process = 15;
            WndDialog.settedPlot.process();
        }
    }

    public static class ChoiceArmor extends Choice {
        {
            text = Messages.get(FrostNovaQuestPlot.class, "choose2",Quest.armor.name());
        }

        @Override
        public void react() {

            Item reward = Quest.armor;

            if (reward == null) return;

            reward.identify();

            for(Mob mob : Dungeon.level.mobs)
            {
                if(mob instanceof Ghost)
                {
                    Quest.setGhost((NPC) mob);
                }
            }

            if (reward.doPickUp( Dungeon.hero )) {
                GLog.i( Messages.get(Dungeon.hero, "you_now_have", reward.name()) );
            } else {
                Dungeon.level.drop( reward, Quest.ghost.pos).sprite.drop();
            }

            Quest.ghost.die(null);
            FrostNovaQuestPlot.Quest.complete();

            WndDialog.settedPlot.process = 15;
            WndDialog.settedPlot.process();
        }
    }

    public class ChoiceCancel extends Choice {
        {
            text = "待会再说";
        }

        @Override
        public void react() {
            diagulewindow.cancel();
        }
    }
}
package com.unifier.arknightspixeldungeon.ui.skilltreelayouter;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.ui.TalentButton;
import com.unifier.arknightspixeldungeon.ui.TalentsPane;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;

public class ChenSkillTreeLayouter {

    protected static final int DIM_GRAY		= 0xFF696969;

    private static float HUDHeight = 1f;

    private static float Horizontal_Separator= ButtonWidth() / 2f;//Half of Talent Button's width
    private static float Vertical_separator= ButtonHeight() / 2f;//Half of Talent Button's height

    public static float ContentWidth = 10 * Horizontal_Separator + 5 * ButtonWidth();
    public static float ContentHeight = 21 * Vertical_separator + 20 * ButtonHeight() + titleHeight() + 2 * HUDHeight;

    private static final float ButtonHeight(){
        return TalentButton.HEIGHT;
    }

    private static final float ButtonWidth(){ return TalentButton.WIDTH; }

    private static float titleHeight() {
        BitmapText point = new BitmapText( PixelScene.pixelFont);
        point.text(Dungeon.hero.talentPointsAvailable(1) + "/" + Dungeon.hero.talentPointsAvailable(1));
        point.measure();
        PixelScene.align(point);
        return point.height();
    }

    private static float HUDHeight(){
        return titleHeight() + HUDHeight;
    }


    private static float titleWidth() {
        BitmapText point = new BitmapText( PixelScene.pixelFont);
        point.text(Dungeon.hero.talentPointsAvailable(1) + "/" + Dungeon.hero.talentPointsAvailable(1));
        point.measure();
        PixelScene.align(point);
        return point.width();
    }

    public static void layoutSkillTree(TalentsPane talentsPane){
        layoutSHEATHED_STRIKE(talentsPane);
        layoutSurvival(talentsPane);
        layoutUNSHEATH(talentsPane);
        layoutREFLECT(talentsPane);
        layoutSHADOWLESS(talentsPane);
        layoutSUBCLASS(talentsPane);
        layoutWEAPONMASTERY(talentsPane);

        layoutTitle(talentsPane,1);

        talentsPane.allbuttons.get(0).get(0).setPos(Horizontal_Separator,Vertical_separator + ButtonHeight() + HUDHeight());//SHEATHED_STRIKE

        talentsPane.allbuttons.get(0).get(1).setPos(Horizontal_Separator,5 * Vertical_separator + 4 * ButtonHeight() + HUDHeight());//FAST_RECOVERY

        talentsPane.allbuttons.get(0).get(2).setPos(Horizontal_Separator,11 * Vertical_separator + 10 * ButtonHeight() + HUDHeight());//PREEMPTIVE_STRIKE

        talentsPane.allbuttons.get(0).get(3).setPos(Horizontal_Separator,19f * Vertical_separator + 18f * ButtonHeight() + HUDHeight());//ARM_INTUITION

        setSeparators(talentsPane,1);
        layoutTitle(talentsPane,2);

        talentsPane.allbuttons.get(1).get(0).setPos(3 * Horizontal_Separator + ButtonWidth(),HUDHeight());//SHEATH_THROW
        talentsPane.allbuttons.get(1).get(1).setPos(3 * Horizontal_Separator + ButtonWidth(),Vertical_separator + ButtonHeight() + HUDHeight());//REPRIMAND
        talentsPane.allbuttons.get(1).get(2).setPos(3 * Horizontal_Separator + ButtonWidth(),2 * Vertical_separator + 2 * ButtonHeight() + HUDHeight());//PARRY

        talentsPane.allbuttons.get(1).get(3).setPos(3 * Horizontal_Separator + ButtonWidth(),4 * Vertical_separator + 3 * ButtonHeight() + HUDHeight());//VIGILANCE
        talentsPane.allbuttons.get(1).get(4).setPos(3 * Horizontal_Separator + ButtonWidth(),5 * Vertical_separator + 4 * ButtonHeight() + HUDHeight());//LAST_CHANCE
        talentsPane.allbuttons.get(1).get(5).setPos(3 * Horizontal_Separator + ButtonWidth(),6 * Vertical_separator + 5 * ButtonHeight() + HUDHeight());//DRAGON_SCALE

        talentsPane.allbuttons.get(1).get(6).setPos(3 * Horizontal_Separator + ButtonWidth(),8 * Vertical_separator + 7 * ButtonHeight() + HUDHeight());//UNSHEATH
        talentsPane.allbuttons.get(1).get(7).setPos(3 * Horizontal_Separator + ButtonWidth(),9 * Vertical_separator + 8 * ButtonHeight() + HUDHeight());//FLASH

        talentsPane.allbuttons.get(1).get(8).setPos(3 * Horizontal_Separator + ButtonWidth(),11 * Vertical_separator + 10 * ButtonHeight() + HUDHeight());//REFLECT

        talentsPane.allbuttons.get(1).get(9).setPos(3 * Horizontal_Separator + ButtonWidth(),13 * Vertical_separator + 12 * ButtonHeight() + HUDHeight());//CONTINUOUS_ASSAULT
        talentsPane.allbuttons.get(1).get(10).setPos(3 * Horizontal_Separator + ButtonWidth(),14 * Vertical_separator + 13 * ButtonHeight() + HUDHeight());//RED_RAGE

        talentsPane.allbuttons.get(1).get(11).setPos(3 * Horizontal_Separator + ButtonWidth(),19 * Vertical_separator + 18 * ButtonHeight() + HUDHeight());//WEAPON_ADAPT

        setSeparators(talentsPane,2);
        layoutTitle(talentsPane,3);

        talentsPane.allbuttons.get(2).get(0).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),HUDHeight());//SHEATH_BOUNCE
        talentsPane.allbuttons.get(2).get(1).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),Vertical_separator + ButtonHeight() + HUDHeight());//WELL_PREPARED
        talentsPane.allbuttons.get(2).get(2).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),2 * Vertical_separator + 2 * ButtonHeight() + HUDHeight());//COUNTER_STRIKE

        talentsPane.allbuttons.get(2).get(3).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),5 * Vertical_separator + 4 * ButtonHeight() + HUDHeight());//RALLY_FORCE


        talentsPane.allbuttons.get(2).get(4).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),7 * Vertical_separator + 6 * ButtonHeight() + HUDHeight());//FORMATION_BREAKER
        talentsPane.allbuttons.get(2).get(5).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),9 * Vertical_separator + 8 * ButtonHeight() + HUDHeight());//WIND_CUTTER

        talentsPane.allbuttons.get(2).get(6).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),10 * Vertical_separator + 9 * ButtonHeight() + HUDHeight());//SKILLFUL_GUARD
        talentsPane.allbuttons.get(2).get(7).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),12 * Vertical_separator + 11 * ButtonHeight() + HUDHeight());//EYE_FOR_EYE

        talentsPane.allbuttons.get(2).get(8).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),13 * Vertical_separator + 12 * ButtonHeight() + HUDHeight());//DEADLY_COMBO
        talentsPane.allbuttons.get(2).get(9).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),14 * Vertical_separator + 13 * ButtonHeight() + HUDHeight());//SCARLET_MOMENTUM

        talentsPane.allbuttons.get(2).get(10).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),16 * Vertical_separator + 15 * ButtonHeight() + HUDHeight());//SHADOWLESS

        talentsPane.allbuttons.get(2).get(11).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),18 * Vertical_separator + 17 * ButtonHeight() + HUDHeight());//LIGHT_WEAPON_MASTERY
        talentsPane.allbuttons.get(2).get(12).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),19 * Vertical_separator + 18 * ButtonHeight() + HUDHeight());//SWORD_WEAPON_MASTERY
        talentsPane.allbuttons.get(2).get(13).setPos(5 * Horizontal_Separator + 2 * ButtonWidth(),20 * Vertical_separator + 19 * ButtonHeight() + HUDHeight());//HEAVY_WEAPON_MASTERY

        setSeparators(talentsPane,3);
        layoutTitle(talentsPane,4);

        talentsPane.allbuttons.get(3).get(0).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(), 0.5f * ButtonHeight() + HUDHeight());//WEAPON_THROW
        talentsPane.allbuttons.get(3).get(1).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),Vertical_separator + 1.5f * ButtonHeight() + HUDHeight());//SEIZE_OPPORTUNITY

        talentsPane.allbuttons.get(3).get(2).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),5 * Vertical_separator + 4 * ButtonHeight() + HUDHeight());//FRUGALITY

        talentsPane.allbuttons.get(3).get(3).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),8 * Vertical_separator + 7 * ButtonHeight() + HUDHeight());//HEART_STRIKER

        talentsPane.allbuttons.get(3).get(4).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),13 * Vertical_separator + 12 * ButtonHeight() + HUDHeight());//BOTHSIDE_ATTACK
        talentsPane.allbuttons.get(3).get(5).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),14 * Vertical_separator + 13 * ButtonHeight() + HUDHeight());//CRIMSON_RAMPAGE

        talentsPane.allbuttons.get(3).get(6).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),15 * Vertical_separator + 14 * ButtonHeight() + HUDHeight());//SWORD_RAIN
        talentsPane.allbuttons.get(3).get(7).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),17 * Vertical_separator + 16 * ButtonHeight() + HUDHeight());//CRIMSON_EXTENSION

        //talentsPane.allbuttons.get(3).get(8).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),18f * Vertical_separator + 18 * ButtonHeight() + HUDHeight());//ARMOR_BREAKER
        //talentsPane.allbuttons.get(3).get(9).setPos(7 * Horizontal_Separator + 3 * ButtonWidth(),20f * Vertical_separator + 18 * ButtonHeight() + HUDHeight());//PERFECT_SMASH



        setSeparators(talentsPane,4);
        layoutTitle(talentsPane,5);

        talentsPane.allbuttons.get(4).get(0).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),7 * Vertical_separator + 6 * ButtonHeight() + HUDHeight());//FLOWING_WATER
        talentsPane.allbuttons.get(4).get(1).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),9 * Vertical_separator + 8 * ButtonHeight() + HUDHeight());//SLASH_ECHO
        talentsPane.allbuttons.get(4).get(2).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),11 * Vertical_separator + 10 * ButtonHeight() + HUDHeight());//ENGROSSED

        talentsPane.allbuttons.get(4).get(3).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),13 * Vertical_separator + 12 * ButtonHeight() + HUDHeight());//MORTAL_SKILL
        talentsPane.allbuttons.get(4).get(4).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),14 * Vertical_separator + 13 * ButtonHeight() + HUDHeight());//SURPASS_LIMIT

        talentsPane.allbuttons.get(4).get(5).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),16 * Vertical_separator + 15 * ButtonHeight() + HUDHeight());//MOTION_ACCUMULATION

        talentsPane.allbuttons.get(4).get(6).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),18 * Vertical_separator + 17 * ButtonHeight() + HUDHeight());//BLADE_STORM
        talentsPane.allbuttons.get(4).get(7).setPos(9 * Horizontal_Separator + 4 * ButtonWidth(),20 * Vertical_separator + 19 * ButtonHeight() + HUDHeight());//FULL_SUPPRESSION
    }



    private static void setSeparators(TalentsPane talentsPane, int tier) {
        //talentsPane.separators.get(tier-1).size(1,ContentHeight);
        //talentsPane.separators.get(tier-1).x = tier * ( 2 * Horizontal_Separator + ButtonWidth()) - ( talentsPane.separators.get(tier-1).width() / 2 );
        //talentsPane.separators.get(tier-1).y = 0;
    }

    private static void layoutTitle(TalentsPane talentsPane,int tier) {
        talentsPane.points.get(tier-1).measure();
        PixelScene.align(talentsPane.points.get(tier-1));
        talentsPane.points.get(tier-1).x = getTierHorizontalCenter(tier) - titleWidth()/2;
        talentsPane.points.get(tier-1).y = 0f;
    }

    private static float getTierHorizontalCenter(int tier){
        return ((2 * tier) * Horizontal_Separator) + ((tier-1) * ButtonWidth());
    }

    private static void layoutSHEATHED_STRIKE(TalentsPane talentsPane){
        ColorBlock bg;
        bg= new ColorBlock(2 * Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + ButtonWidth();
        bg.y = Vertical_separator + 1.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 2 * Horizontal_Separator + ButtonWidth();
        bg.y = 0.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(2 * Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 0.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 2 * Horizontal_Separator + ButtonWidth();
        bg.y = Vertical_separator + 1.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(2 * Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = Vertical_separator + 1.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 2 * Horizontal_Separator + ButtonWidth();
        bg.y = 2 * Vertical_separator + 2.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(2 * Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 2 * Vertical_separator + 2.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(2,2 * Vertical_separator + 2 * ButtonHeight() + 2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 2 * Horizontal_Separator + ButtonWidth() - 1;
        bg.y = 0.5f * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(2 ,2 * Vertical_separator + ButtonHeight(),DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + 0.5f * ButtonWidth() - 1;
        bg.y = Vertical_separator + 2 * ButtonHeight() + HUDHeight();

        bg = new ColorBlock(3 *  ButtonWidth() + 6 * Horizontal_Separator + 2 ,2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + 0.5f * ButtonWidth() - 1 ;
        bg.y = 3 * Vertical_separator + 3 * ButtonHeight() + HUDHeight() - 1;

        bg = new ColorBlock(2,2 * Vertical_separator + 0.5f * ButtonHeight(),DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 7 * Horizontal_Separator + 3.5f * ButtonWidth() - 1;
        bg.y = Vertical_separator + 2.5f * ButtonHeight() + HUDHeight();
    }

    private static void layoutSurvival(TalentsPane talentsPane) {
        ColorBlock bg;

        bg= new ColorBlock(2,0.5f * ButtonHeight() + Vertical_separator ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + 0.5f * ButtonWidth() - 1;
        bg.y = 4 * Vertical_separator + 3.5f * ButtonHeight() + HUDHeight();

        bg= new ColorBlock(2,0.5f * ButtonHeight() + Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + 0.5f * ButtonWidth() - 1;
        bg.y = 5 * Vertical_separator + 5 * ButtonHeight() + HUDHeight();

        bg= new ColorBlock(2 * Horizontal_Separator,2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + ButtonWidth();
        bg.y = 5 * Vertical_separator + 4.5f * ButtonHeight() + HUDHeight()-1;

        bg= new ColorBlock(0.5f * ButtonWidth() + 2 * Horizontal_Separator + 1,2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + 0.5f * ButtonWidth() - 1;
        bg.y = 4 * Vertical_separator + 3.5f * ButtonHeight() + HUDHeight()-1;

        bg= new ColorBlock(0.5f * ButtonWidth() + 2 * Horizontal_Separator + 1,2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = Horizontal_Separator + 0.5f * ButtonWidth() - 1;
        bg.y = 6 * Vertical_separator + 5.5f * ButtonHeight() + HUDHeight()-1;


        bg= new ColorBlock(2,0.5f * ButtonHeight() + Vertical_separator ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 4 * Vertical_separator + 3.5f * ButtonHeight() + HUDHeight();

        bg= new ColorBlock(2,0.5f * ButtonHeight() + Vertical_separator ,DIM_GRAY);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 5 * Vertical_separator + 5 * ButtonHeight() + HUDHeight();
        talentsPane.content.add(bg);

        bg= new ColorBlock(0.5f * ButtonWidth() + 2 * Horizontal_Separator + 1,2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2f * ButtonWidth();
        bg.y = 4 * Vertical_separator + 3.5f * ButtonHeight() + HUDHeight()-1;

        bg= new ColorBlock(0.5f * ButtonWidth() + 2 * Horizontal_Separator + 1,2 ,DIM_GRAY);
        bg.x = 3 * Horizontal_Separator + 2f * ButtonWidth();
        bg.y = 6 * Vertical_separator + 5.5f * ButtonHeight() + HUDHeight()-1;
        talentsPane.content.add(bg);
    }

    private static void layoutUNSHEATH(TalentsPane talentsPane){
        ColorBlock bg;

        bg= new ColorBlock(4 * Horizontal_Separator + 1 * ButtonWidth(),2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 8 * Vertical_separator + 7.5f * ButtonHeight() + HUDHeight() - 1;


        bg= new ColorBlock(2,2 * Vertical_separator + 2 * ButtonHeight(),DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 4 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 7 * Vertical_separator + 6.5f * ButtonHeight() + HUDHeight();


        bg= new ColorBlock(2,Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y = 8 * Vertical_separator + 8 * ButtonHeight() + HUDHeight();

        bg= new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 4 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 7 * Vertical_separator + 6.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 4 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 9 * Vertical_separator + 8.5f * ButtonHeight() + HUDHeight() - 1;


        bg= new ColorBlock(4 * Horizontal_Separator + ButtonWidth(),2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 3 * ButtonWidth();
        bg.y = 7 * Vertical_separator + 6.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(4 * Horizontal_Separator + ButtonWidth(),2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 3 * ButtonWidth();
        bg.y = 9 * Vertical_separator + 8.5f * ButtonHeight() + HUDHeight() - 1;
    }

    private static void layoutREFLECT(TalentsPane talentsPane){
        ColorBlock bg;

        bg= new ColorBlock(2, 2 * Vertical_separator + 2 * ButtonHeight() + 2 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 4 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 10 * Vertical_separator + 9.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(6 * Horizontal_Separator + 2 * ButtonWidth(),2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 11 * Vertical_separator + 10.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 4 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 10 * Vertical_separator + 9.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(Horizontal_Separator,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 4 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y = 12 * Vertical_separator + 11.5f * ButtonHeight() + HUDHeight() - 1;
    }

    private static void layoutSHADOWLESS(TalentsPane talentsPane){
        ColorBlock bg;

        bg= new ColorBlock(2, Vertical_separator + 0.5f * ButtonHeight() + 1 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 15 * Vertical_separator + 14.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2, Vertical_separator + 0.5f * ButtonHeight() + 1 ,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 16 * Vertical_separator + 16 * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 * Horizontal_Separator + 0.5f * ButtonWidth() + 1,2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 15 * Vertical_separator + 14.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 * Horizontal_Separator + 0.5f * ButtonWidth() + 1,2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 17 * Vertical_separator + 16.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(4 * Horizontal_Separator + ButtonWidth(),2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x =  5 * Horizontal_Separator + 3 * ButtonWidth();
        bg.y =  16 * Vertical_separator + 15.5f * ButtonHeight() + HUDHeight() - 1;
    }

    private static void layoutSUBCLASS(TalentsPane talentsPane){
        ColorBlock bg;


        bg= new ColorBlock(6 * Horizontal_Separator + 3 * ButtonWidth() + 2,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y = 12.5f * Vertical_separator + 12 * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(6 * Horizontal_Separator + 3 * ButtonWidth() + 2,2,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y = 14.5f * Vertical_separator + 14 * ButtonHeight() + HUDHeight() - 1;


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y = 12.5f * Vertical_separator + 12 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y = 12.5f * Vertical_separator + 12 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 7 * Horizontal_Separator + 3.5f * ButtonWidth() - 1;
        bg.y = 12.5f * Vertical_separator + 12 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 9 * Horizontal_Separator + 4.5f * ButtonWidth() - 1;
        bg.y = 12.5f * Vertical_separator + 12 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y =  14f * Vertical_separator + 14 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 2.5f * ButtonWidth() - 1;
        bg.y =  14f * Vertical_separator + 14 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 7 * Horizontal_Separator + 3.5f * ButtonWidth() - 1;
        bg.y =  14f * Vertical_separator + 14 * ButtonHeight() + HUDHeight();


        bg= new ColorBlock( 2 ,0.5f * Vertical_separator,DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 9 * Horizontal_Separator + 4.5f * ButtonWidth() - 1;
        bg.y =  14f * Vertical_separator + 14 * ButtonHeight() + HUDHeight() ;
    }

    private static void layoutWEAPONMASTERY(TalentsPane talentsPane){
        ColorBlock bg;

        //bg= new ColorBlock(2 * Horizontal_Separator,2 , DIM_GRAY);
        //talentsPane.content.add(bg);
        //bg.x = Horizontal_Separator + ButtonWidth();
        //bg.y =  19f * Vertical_separator + 18.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 * Horizontal_Separator,2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 2 * ButtonWidth();
        bg.y =  19f * Vertical_separator + 18.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 * Horizontal_Separator + 0.5f * ButtonWidth(),2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth();
        bg.y = 18 * Vertical_separator + 17.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 * Horizontal_Separator + 0.5f * ButtonWidth(),2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth();
        bg.y = 20 * Vertical_separator + 19.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 ,Vertical_separator + 0.5f * ButtonHeight() + 1, DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y = 18 * Vertical_separator + 17.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(2 ,Vertical_separator + 0.5f * ButtonHeight(), DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 3 * Horizontal_Separator + 1.5f * ButtonWidth() - 1;
        bg.y = 19 * Vertical_separator + 19 * ButtonHeight() + HUDHeight();

        bg= new ColorBlock(4 * Horizontal_Separator + ButtonWidth(),2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 3 * ButtonWidth();
        bg.y = 18 * Vertical_separator + 17.5f * ButtonHeight() + HUDHeight() - 1;

        bg= new ColorBlock(4 * Horizontal_Separator + ButtonWidth(),2 , DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 5 * Horizontal_Separator + 3 * ButtonWidth();
        bg.y = 20 * Vertical_separator + 19.5f * ButtonHeight() + HUDHeight() - 1;

        /*bg= new ColorBlock(2 , 0.5f * ButtonHeight(), DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 7 * Horizontal_Separator + 3.5f * ButtonWidth() - 1;
        bg.y = 18 * Vertical_separator + 17.5f * ButtonHeight() + HUDHeight();

        bg= new ColorBlock(2 , 0.5f * ButtonHeight(), DIM_GRAY);
        talentsPane.content.add(bg);
        bg.x = 7 * Horizontal_Separator + 3.5f * ButtonWidth() - 1;
        bg.y = 20 * Vertical_separator + 19 * ButtonHeight() + HUDHeight();*/
    }
}

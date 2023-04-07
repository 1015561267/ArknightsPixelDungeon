package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.SniperRifle;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.utils.BArray;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;

public class SkillLoader extends Tag{

    private static final float ENABLED	= 1.0f;
    private static final float DISABLED	= 0.3f;

    protected static final int ICON_SIZE = 16;

    private static Image cross = Icons.TARGET.get();

    public static Char lastTarget = null;
    public static boolean targetting = false;

    Image icon;

    float cooldownRatio = 1f;

    private boolean enabled = true;

    private HeroSkill skill;

    private ColorBlock coolDownBlock;

    public SkillLoader(HeroSkill loader) {

        super(0xAA222222);

        skill = loader;
        icon = skill == null ? new ItemSprite(ItemSpriteSheet.NULLWARN ) : skill.skillIcon();
        add(icon);

        coolDownBlock = new ColorBlock(32f,32f,0xAA222222);
        add(coolDownBlock);

        setSize( 32f, 32f );
    }

    @Override
    protected void createChildren() {

        super.createChildrenWithoutBg();

        bg = Chrome.get( Chrome.Type.TAB_SET );
        add( bg );

        cross = Icons.TARGET.get();
    }


    public void setIcon(Image img) {
        icon = img == null ? new ItemSprite(ItemSpriteSheet.NULLWARN ) : img;
        layout();
    }

    public boolean enabled( ) {
        return skill == null ? false : enabled;
    }

    private void enable( boolean value ) {
        enabled = value;
        if (icon != null) {
            icon.alpha( value ? ENABLED : DISABLED );
        }
    }

    @Override
    protected void layout() {
        super.layout();

        bg.x = x;
        bg.y = y;
        bg.size( width, height );

        icon.x = x -1 + (width - icon.width()) / 2;
        icon.y = y + (height - icon.height()) / 2;

        coolDownBlock.x = bg.x;
        coolDownBlock.y = bg.y;
        coolDownBlock.scale.y = -1.0f;
    }

    @Override
    public void update() {
        super.update();

        if(!skill.activated())
        {
            bg.lightness(0.25f);
            icon.lightness(0.75f);
        }
        else
        {
            bg.resetColor();
            icon.resetColor();
        }

        cooldownRatio = skill == null ? 1 : skill.cooldownRatio();
        coolDownBlock.size(bg.width(),bg.height() * cooldownRatio);

        if (Dungeon.hero.isAlive() && (cooldownRatio ==0 || (Dungeon.hero.hasTalent(Talent.CLOUD_CRACK) && skill.charge > 0))) {
            enable( Dungeon.hero.ready );
        } else {
            enable( false );
        }

        if (lastTarget != null && lastTarget.sprite != null){
            cross.point(lastTarget.sprite.center(cross));
        }
    }

    @Override
    protected void onPointerDown() { if(skill!=null && skill.activated()) { bg.brightness(1.2f); } }
    @Override
    protected void onPointerUp() { bg.resetColor(); }

    protected void onClick() {
        //FIXME feels weird no matter what change make,damn it should have more polish
        if(skill!=null && skill.activated())
        {
            if(!targetting && skill.useTargetting()){
                skill.doAction();
                targetting = true;
            }else {
                if(skill.useTargetting()){
                    int cell = autoAim(lastTarget);

                    if (cell != -1){
                        GameScene.handleCell(cell);
                        }
                    else {
                        //couldn't auto-aim, do nothing for now
                            if(lastTarget!=null)
                                GameScene.handleCell( lastTarget.pos );
                            //GameScene.handleCell( lastTarget.pos );
                        }
                }
                else skill.doAction();
            }
        }
    }

    protected boolean onLongClick() {

        if(skill!=null)
        {
            skill.handleLongClick();
            return true;
        }
        return false;
    }

    public static void target( Char target ) {
        if (target != null && target.alignment != Char.Alignment.ALLY) {
            lastTarget = target;
        }
    }

    public static void cancel() {
        if (targetting) {
            cross.remove();
            targetting = false;
        }
    }

    public static void useTargeting() {//We can make some targeted aiming tactics to specific skill

        if (lastTarget != null &&
                Actor.chars().contains( lastTarget ) &&
                lastTarget.isAlive() &&
                lastTarget.alignment != Char.Alignment.ALLY &&
                Dungeon.level.heroFOV[lastTarget.pos]) {

            CharSprite sprite = lastTarget.sprite;

            if (sprite.parent != null) {
                sprite.parent.addToFront(cross);
                cross.point(sprite.center(cross));
            }

        } else {
            lastTarget = null;
        }
    }

    public int autoAim(Char target){

        if(target!=null && skill!=null && skill.owner!=null){

            int params = Ballistica.PROJECTILE;

            if(skill instanceof SniperRifle)
            {
                params = Ballistica.STOP_CHARS;
            }

            Ballistica ballistica = new Ballistica(skill.owner.pos,target.pos,params);

            //first try to directly target
            if (ballistica.dist == target.pos) {
                return target.pos;
            }
            //Otherwise pick nearby tiles to try and 'angle' the shot, auto-aim basically.
            PathFinder.buildDistanceMap( target.pos, BArray.not( new boolean[Dungeon.level.length()], null ), 2 );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE
                        && ballistica.dist == target.pos)
                    return i;
            }
        }
        //couldn't find a cell, give up.
        return -1;
    }
}

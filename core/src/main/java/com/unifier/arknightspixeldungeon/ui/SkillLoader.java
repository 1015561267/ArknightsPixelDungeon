package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.windows.WndHeroSkill;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;

public class SkillLoader extends Tag{

    private static final float ENABLED	= 1.0f;
    private static final float DISABLED	= 0.3f;

    protected static final int ICON_SIZE = 16;

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


        if (Dungeon.hero.isAlive() && (cooldownRatio ==0 || (Dungeon.hero.hasTalent(Talent.MOTION_ACCUMULATION) && skill.charge > 0))) {
            enable( Dungeon.hero.ready );
        } else {
            enable( false );
        }
    }

    @Override
    protected void onPointerDown() { if(skill!=null && skill.activated()) { bg.brightness(1.2f); } }
    @Override
    protected void onPointerUp() { bg.resetColor(); }

    protected void onClick() {
        if(skill!=null && skill.activated())
        {
            skill.doAction();
        }
        else {

        }
    }

    protected boolean onLongClick() {

        if(skill!=null)
        {
            GameScene.show(new WndHeroSkill(skill));
            return true;
        }
        return false;
    }

}

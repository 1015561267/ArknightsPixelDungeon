package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.ExusiaiSkill;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.Revolver;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.Button;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class WndExusiaiSkill extends Window {

    private ArrayList<AttachmentButton> attachmentButton;

    private static final int WIDTH    = 135;
    private static final int HEIGHT   = 150;

    protected static final int GAP	= 2;

    public WndExusiaiSkill(ExusiaiSkill skill) {

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(skill, "title"), 12 );
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((WIDTH - title.width())/2f, 2);
        add(title);

        Image bluePrint = skill.bluePrintPicture();
        bluePrint.x = WIDTH / 2f - bluePrint.width() / 2f;
        bluePrint.y = title.height() + 2 * GAP;
        PixelScene.align(bluePrint);
        add(bluePrint);

        attachmentButton = new ArrayList<>();
        AttachmentButton button;

        if(skill instanceof Revolver){

            button = new AttachmentButton(Attachment.AttachType.GUN_SIGHT , skill.getGUN_SIGHT());
            button.setPos( (bluePrint.width * 27 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 62 / 64) );
            attachmentButton.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentButton(Attachment.AttachType.BULLET , skill.getBULLET());
            button.setPos( (bluePrint.width * 102 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 62 / 64) );
            attachmentButton.add(button);
            addToFront(button);
            add(button);
        }

        //for (AttachmentButton button:attachmentButton) {
        //    add(button);
        //}

        String message = skill.desc();
        RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
        text.text( message, WIDTH - 2 * GAP );
        text.setPos( GAP, attachmentButton.get(attachmentButton.size() - 1).bottom() + GAP );
        add( text );

        int height = layOutNecessaryButton(skill,text.bottom());

        update();

        resize( WIDTH, HEIGHT);
    }

    private int layOutNecessaryButton(ExusiaiSkill skill, float bottom) {

        if(skill.available()){

        }

        return (int) bottom;
    }

    private void arrangeRevolverButton(Image bluePrint) {
        //attachmentButton.get(0).setPos( bluePrint.x * (27/128) - attachmentButton.get(0).width() / 2 , bluePrint.y);
        //attachmentButton.get(1).setPos( bluePrint.x * (102/128) - attachmentButton.get(0).width() / 2 , bluePrint.y);
    }

    protected class AttachmentButton extends Button{
        protected NinePatch bg;
        public Attachment.AttachType attachType;
        public Attachment attachment;
        private Image icon;

        AttachmentButton(Attachment.AttachType attachType,Attachment attachment)
        {
            super();

            width = 32;
            height = 32;
            this.attachType = attachType;
            this.attachment = attachment;

            bg = Chrome.get( Chrome.Type.RED_BUTTON );
            addToBack( bg );
            bg.size( width, height );

            if(attachment!=null) {
                int n = attachment.icon() - 1;
                int row = n / 5;
                int col = n % 5;
                icon = new Image(Assets.GUN_ATTACHMENTS, 32 * col , 32 * row, 32, 32);
            }
            else {
                switch (attachType){
                    case GUN_SIGHT:icon = new ItemSprite(ItemSpriteSheet.NULLWARN, null);break;
                    case FRONT_HANG:icon = new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER, null);break;
                    case BELOW_HANG:icon = new ItemSprite(ItemSpriteSheet.ARMOR_HOLDER, null);break;
                    case BACK_HANG:icon = new ItemSprite(ItemSpriteSheet.WAND_HOLDER, null);break;
                    case AMMO_BOX:icon = new ItemSprite(ItemSpriteSheet.RING_HOLDER, null);break;
                    case BULLET:icon = new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER, null);break;
                }
            }

            add(icon);

            //update();
        }

        @Override
        protected void layout() {
            super.layout();
            bg.x = x;
            bg.y = y;
            icon.x=x;
            icon.y=y;
            PixelScene.align(icon);
        }

        @Override
        protected void onClick() {
            super.onClick();
            GLog.w(attachment!=null? attachment.title():attachType.name());
        }

        @Override
        protected void onPointerDown() {
            bg.brightness( 1.2f );
            Sample.INSTANCE.play( Assets.SND_CLICK );
        }

        @Override
        protected void onPointerUp() {
            bg.resetColor();
        }

        @Override
        public void update(){

            if(attachment!=null) {
                int n = attachment.icon() - 1;
                int row = n / 5;
                int col = n % 5;
                icon = new Image(Assets.GUN_ATTACHMENTS, 32 * col , 32 * row, 32, 32);
            }
            else {
                switch (attachType){
                    case GUN_SIGHT:icon = new ItemSprite(ItemSpriteSheet.NULLWARN, null);break;
                    case FRONT_HANG:icon = new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER, null);break;
                    case BELOW_HANG:icon = new ItemSprite(ItemSpriteSheet.ARMOR_HOLDER, null);break;
                    case BACK_HANG:icon = new ItemSprite(ItemSpriteSheet.WAND_HOLDER, null);break;
                    case AMMO_BOX:icon = new ItemSprite(ItemSpriteSheet.RING_HOLDER, null);break;
                    case BULLET:icon = new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER, null);break;
                }
            }

            layout();
            ///PixelScene.align(icon);
            //addToFront(icon);
        }
    }
}

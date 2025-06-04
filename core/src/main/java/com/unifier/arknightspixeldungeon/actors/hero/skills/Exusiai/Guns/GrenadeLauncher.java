package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.particles.BlastParticle;
import com.unifier.arknightspixeldungeon.effects.particles.SmokeParticle;
import com.unifier.arknightspixeldungeon.items.Bomb;
import com.unifier.arknightspixeldungeon.items.Heap;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GrenadeLauncher extends ExusiaiSkill {

    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.GRENADE_LAUNCHER, 0, 0, 128, 64);
    }

    @Override
    public int shootDamageMin() {
        return (int) (2 + owner.lvl / 4f + Dungeon.depth);
    }

    @Override
    public int shootDamageMax() {
        return (int) (4 + owner.lvl / 2f + Dungeon.depth);
    }

    private int explosionDamage() { //explosionDamage need further balance
        return Random.NormalIntRange(3 + Dungeon.depth/2 , 6 + Dungeon.depth);
    }

    @Override
    protected boolean checkAttachmentType(Attachment attachment) {
        if (attachment.attachType() == Attachment.AttachType.BELOW_HANG
                || attachment.attachType() == Attachment.AttachType.BACK_HANG) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equippingAttachment(Attachment attachment) {
        if (attachment.attachType() == Attachment.AttachType.BELOW_HANG || attachment.attachType() == Attachment.AttachType.BACK_HANG)
            return true;
        return false;
    }

    @Override
    public GunType getType() {
        return GunType.GRENADE_LAUNCHER;
    }

    @Override
    public Image skillIcon() {
        return new SkillIcons(SkillIcons.NONE);
    }

    @Override
    public float CD(Hero hero) {
        return rawCD();
    }

    @Override
    public float rawCD() {
        return 20f;
    }

    @Override
    public int getMaxCharge() {
        return 1;
    }

    protected void doShoot(Hero owner, Integer cell) {
        int from = owner.pos;

        Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.PROJECTILE);
        int result = ballistica.collisionPos;

        GrenadeItem grenade = new GrenadeItem();
        grenade.setParent(this);

        ((MissileSprite) owner.sprite.parent.recycle(MissileSprite.class)).
                reset(owner.sprite,
                        result,
                        grenade,
                        new Callback() {
                            @Override
                            public void call() {
                                doEnemyCheck(from,result);//already contains ammo check
                                grenade.onThrow(result);
                                owner.next();
                            }
                        });

        return;
    }

    public static class GrenadeItem extends Bomb {

        {
            dropsDownHeap = true;
            unique = true;

            image = ItemSpriteSheet.BOMB;
        }

        public GrenadeLauncher parent;

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            GLog.w(Messages.get(this, "cant_pickup"));
            return false;
        }

        @Override
        protected void onThrow(int cell) {

            float delay = 3f;

            if (Dungeon.level.pit[cell]) {
                explode(cell);
                return;
            }

            if (Actor.findChar(cell) != null && !(Actor.findChar(cell) instanceof Hero)) {
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int i : PathFinder.NEIGHBOURS8)
                    if (Dungeon.level.passable[cell + i])
                        candidates.add(cell + i);
                int newCell = candidates.isEmpty() ? cell : Random.element(candidates);
                Dungeon.level.drop(this, newCell).sprite.drop(cell);
                delay = 0.01f;
            } else {
                super.onThrow(cell);
            }

            Actor.addDelayed(fuse = new GrenadeFuse().ignite(this), delay);
        }

        @Override
        public void explode(int cell) {
            //We're blowing up, so no need for a fuse anymore.
            this.fuse = null;

            Sample.INSTANCE.play(Assets.SND_BLAST);

            ArrayList<Char> affected = new ArrayList<>();

            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }

            boolean terrainAffected = false;

            for (int n : PathFinder.NEIGHBOURS9) {
                int c = cell + n;
                if (c >= 0 && c < Dungeon.level.length()) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    }

                    if (Dungeon.level.flamable[c]) {
                        Dungeon.level.destroy(c);
                        GameScene.updateMap(c);
                        terrainAffected = true;
                    }

                    //destroys items / triggers bombs caught in the blast.
                    Heap heap = Dungeon.level.heaps.get(c);
                    if (heap != null)
                        heap.explode();

                    Char ch = Actor.findChar(c);
                    if (ch != null) {
                        affected.add(ch);
                    }
                }
            }

            for (Char ch : affected) {

                //if they have already been killed by another bomb
                if (!ch.isAlive()) {
                    continue;
                }

                int dmg = explosionDamage();

                //those not at the center of the blast take less damage
                if (ch.pos != cell) {
                    dmg = Math.round(dmg * 0.67f);
                }

                dmg -= ch.drRoll();

                if (dmg > 0) {
                    ch.damage(dmg, this);
                }

                if (ch == Dungeon.hero && !ch.isAlive()) {
                    //if (this instanceof MagicalBomb){
                    //	Badges.validateDeathFromFriendlyMagic();
                    //}
                    GLog.n(Messages.get(Bomb.class, "ondeath"));
                    //TODO dungeon.fail use class itself as cause in newer version
                    Dungeon.fail(Bomb.class);
                }
            }

            if (terrainAffected) {
                Dungeon.observe();
            }
        }

        private int explosionDamage() {
            return parent.explosionDamage();
        }

        @Override
        public Emitter emitter() {
            Emitter emitter = new Emitter();
            emitter.pos(7.5f, 3.5f);
            emitter.fillTarget = false;
            emitter.pour(SmokeParticle.SPEW, 0.05f);
            return emitter;
        }

        public void setParent(GrenadeLauncher grenadeLauncher) {
            parent = grenadeLauncher;
        }
    }

    public static class GrenadeFuse extends Bomb.Fuse {
        {
            actPriority = VFX_PRIO - 1; //always first as it could explode rightly after bounce,but after visual effect to avoid logic error
        }
    }

    @Override
    protected float gunAccuracyModifier(int from, int to, Char enemy){
        float modifier = super.gunAccuracyModifier(from,to,enemy);

        if(Dungeon.level.adjacent( from, to )) {
            modifier -= 0.5f;//this is the hit acc modifier of bomb itself,similar to missile weapon,explosion always hit
        }
        else {
            modifier += 0.5f;
        }

        return modifier;
    }
}
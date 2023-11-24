package com.mygdx2.assets;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {

    public static final AssetDescriptor<BitmapFont> UI_FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT, BitmapFont.class);

   /*public static final AssetDescriptor<Skin> UI_SKIN =
            new AssetDescriptor<Skin>(AssetPaths.UI_SKIN, Skin.class);*/

    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    public static final AssetDescriptor<Sound> DAMAGE_SOUNDS =
            new AssetDescriptor<Sound>(AssetPaths.DAMAGE_SOUND, Sound.class);

    public static final AssetDescriptor<Sound> LAUGH_SOUNDS =
            new AssetDescriptor<Sound>(AssetPaths.LAUGH_SOUND, Sound.class);


    public static final AssetDescriptor<ParticleEffect> WATER_PARTICLE_EFFECT =
            new AssetDescriptor<ParticleEffect>(AssetPaths.WATER_PARTICLE_EFFECT, ParticleEffect.class);

    public static final AssetDescriptor<ParticleEffect> FIRE_PARTICLE_EFFECT =
            new AssetDescriptor<ParticleEffect>(AssetPaths.FIRE_PARTICLE_EFFECT, ParticleEffect.class);


    private AssetDescriptors() {
    }
}
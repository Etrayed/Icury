package io.github.etrayed.icury.video;

import io.github.etrayed.icury.IcuryPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.common.model.Size;

import java.io.IOException;

/**
 * @author Etrayed
 */
public class VideoPlayer {

    private final Video video;

    private final Canvas canvas;

    private BukkitTask frameTicker;

    private int frameIndex;

    private boolean paused, stopped;

    private VideoPlayer(Video video, Canvas canvas) {
        this.video = video;
        this.canvas = canvas;

        setupMapViews();
    }

    private void setupMapViews() {
        video.forceResolution(new Size(canvas.frames.length * 128, canvas.frames[0].length * 128));

        for (int x = 0; x < canvas.frames.length; x++) {
            for (int y = 0; y < canvas.frames[0].length; y++) {
                canvas.frames[x][y].addRenderer(VideoPartRenderer.create(this, x == 0 ? 0 : (x * 128),
                        y == 0 ? 0 : (y * 128)));
            }
        }
    }

    public void play() {
        frameTicker = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(IcuryPlugin.class), new Runnable() {

            @Override
            public void run() {
                if(paused) {
                    return;
                }

                frameIndex++;
            }
        }, 1, 1);
    }

    public void resume() {
        paused = false;
    }

    public void pause() {
        paused = true;
    }

    public void stop() {
        stopped = true;

        if(frameTicker != null) {
            frameTicker.cancel();
            frameTicker = null;
        }
    }

    public boolean isPaused() {
        return paused;
    }

    boolean isStopped() {
        return stopped;
    }

    Picture getCurrentFrame() {
        try {
            return video.getFrameAt(frameIndex);
        } catch (IOException | JCodecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static VideoPlayer create(Video video, Canvas canvas) {
        return new VideoPlayer(video, canvas);
    }
}

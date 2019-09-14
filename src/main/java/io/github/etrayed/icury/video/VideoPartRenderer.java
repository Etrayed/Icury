package io.github.etrayed.icury.video;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jcodec.scale.AWTUtil;

/**
 * @author Etrayed
 */
public class VideoPartRenderer extends MapRenderer {

    private final VideoPlayer videoPlayer;

    private final int frameXOffset, frameYOffset;

    private VideoPartRenderer(VideoPlayer videoPlayer, int frameXOffset, int frameYOffset) {
        this.videoPlayer = videoPlayer;
        this.frameXOffset = frameXOffset;
        this.frameYOffset = frameYOffset;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if(videoPlayer.isStopped()) {
            mapView.removeRenderer(this);

            return;
        }

        if(videoPlayer.isPaused()) {
            return;
        }

        mapCanvas.drawImage(0, 0, AWTUtil.toBufferedImage(videoPlayer.getCurrentFrame()).getSubimage(frameXOffset,
                frameYOffset, 128, 128));
    }

    public static VideoPartRenderer create(VideoPlayer videoPlayer, int frameXOffset, int frameYOffset) {
        return new VideoPartRenderer(videoPlayer, frameXOffset, frameYOffset);
    }
}

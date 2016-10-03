package ch.bildspur.sva.sketch

import ch.bildspur.sva.model.Sector
import ch.bildspur.sva.sketch.controller.ClipController
import ch.bildspur.sva.sketch.controller.SyphonController
import ch.bildspur.sva.sketch.controller.UIController
import ch.bildspur.sva.sound.SoundVarianceAnalyser
import processing.core.PApplet
import processing.core.PConstants
import processing.opengl.PJOGL
import processing.video.Movie
import javax.sound.sampled.Clip
import kotlin.properties.Delegates

/**
 * Created by cansik on 25.09.16.
 */
class SVASketch : PApplet()
{
    internal val OUTPUT_WIDTH = 600
    internal val OUTPUT_HEIGHT = 450

    internal val NAME = "Sound Variance Analyser"

    internal val FRAME_RATE = 30f

    internal var sva : SoundVarianceAnalyser = SoundVarianceAnalyser(this)

    internal val syphon = SyphonController(this)
    internal var ui:UIController by Delegates.notNull()
    internal var clips:ClipController by Delegates.notNull()

    val sectors = mutableListOf<Sector>()

    override fun settings()
    {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT, PConstants.P2D)
        PJOGL.profile = 1
    }

    override fun setup()
    {
        smooth()
        frameRate(FRAME_RATE)

        surface.setTitle(NAME)
        syphon.setupSyphon(NAME)

        ui = UIController(this)
        ui.init()

        // add default sectors
        sectors.add(Sector("Low", 0f, 0.3333f, "low"))
        sectors.add(Sector("Mid", 0.3333f, 0.6666f, "mid"))
        sectors.add(Sector("High", 0.6666f, 1f, "high"))

        for(sector in sectors)
            ui.sectorView.addSector(sector)

        clips = ClipController(this, sectors[0])


        val player = sva.minim.loadFile("techhouse-minimix.mp3", 2048)
        player.play()

        sva.init(player)

    }

    override fun draw()
    {
        background(55f)
        sva.listen()
        clips.update()

        ui.render()
    }

    fun movieEvent(m: Movie) {
        m.read()
    }

    override fun mousePressed() {
        ui.mousePressed()
    }

    override fun mouseDragged() {
        ui.mouseDragged()
    }

    override fun mouseMoved() {
        ui.mouseMoved()
    }

    override fun mouseReleased() {
        ui.mouseReleased()
    }
}
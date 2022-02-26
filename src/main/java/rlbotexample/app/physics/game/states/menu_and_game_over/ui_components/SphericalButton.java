package rlbotexample.app.physics.game.states.menu_and_game_over.ui_components;


import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;
import util.renderers.ShapeRenderer;

import java.awt.*;

public class SphericalButton {

    private final Vector3 position;
    private final String name;
    private final Color color;
    private final double radii;

    public SphericalButton(final Vector3 position, final double radii, final String name, final Color color) {
        this.position = position;
        this.name = name;
        this.color = color;
        this.radii = radii;
    }

    public void render() {
        RenderTasks.append(renderer -> renderer.drawString3d(name,
                Color.WHITE,
                position.plus(new Vector3(0, 0, 400)).toFlatVector(),
                1, 1));
        RenderTasks.append(renderer -> new ShapeRenderer(renderer).renderSwerlingSphere(position, radii, color));
    }

    public boolean isPressed(DataPacket input) {
        return input.humanCar.hitBox.closestPointOnSurface(position)
                .minus(position).magnitudeSquared() < radii*radii;
    }

    public Vector3 getPosition() {
        return position;
    }
}

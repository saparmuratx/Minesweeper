import processing.core.PApplet;

public class CloseButton extends Button {
    private Actionable action;

    Window window;

    public CloseButton(App app, float x, float y, float width, float height) {
        super(app, x, y, width, height);
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void addAction(Actionable action) {
        this.action = action;
    }

    @Override
    public void draw() {
        super.draw();
        cross(posX + width / 2f, posY + height / 2f);
    }

    @Override
    public void action() {
        action.perform();
    }

    private void cross(float x, float y) {
        PA.pushMatrix();
        PA.translate(x, y);
        PA.stroke(100);
        float angle = 90;
        PA.rotate(PApplet.radians(45));
        PA.strokeWeight(height / 12);
        for (int i = 0; i < 4; i++) {
            PA.rotate(PApplet.radians(angle));
            PA.line(0, 0, width / 3.f, 0);
        }
        PA.popMatrix();
    }
}

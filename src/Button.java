public abstract class Button {
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;

    protected Color pressed = new Color(48, 179, 219, 100);
    protected Color cursor = new Color(2, 154, 219);
    protected Color stroke = new Color(60, 60, 60);
    protected Color body = new Color(130, 130, 130);

    protected float strokeWeight = 1.f;
    protected float roundness = 0;

    private boolean active = true;

    App PA; // PApplet

    public Button(App app, float x, float y, float w, float h) {
        posX = x;
        posY = y;
        width = w;
        height = h;
        PA = app;
    }

    public void enable() {
        active = true;
    }

    public void disable() {
        active = false;
    }

    public void setRoundness(float r) {
        roundness = r;
    }

    public void setStroke(Color c) {
        stroke = c;
    }

    public void setBody(Color c) {
        body = c;
    }

    public void setCursor(Color c) {
        cursor = c;
    }

    public void setPressed(Color c) {
        pressed = c;
    }

    public void setStrokeWeight(float sw) {
        strokeWeight = sw;
    }

    public abstract void action();

    protected boolean containsMouse() {
        return active && PA.mouseX >= posX && PA.mouseX <= posX + width && PA.mouseY >= posY
                && PA.mouseY <= posY + height;
    }

    protected void triggerAction() {
        if (active && containsMouse() && PA.isMouseClicked()) {
            action();
        }
    }

    public void draw() {
        PA.strokeWeight(strokeWeight);
        PA.stroke(stroke.getR(), stroke.getG(), stroke.getB());
        if (containsMouse() && active) {
            drawCursor();
        }

        drawBody();
        if (containsMouse() && PA.mousePressed && active) {
            drawPressed();
        }
        if (containsMouse() && PA.isMouseClicked() && active) {
            action();
        }
    }

    private void drawBody() {
        PA.fill(body.getR(), body.getG(), body.getB());
        PA.rect(posX, posY, width, height, roundness);
    }

    private void drawCursor() {
        PA.stroke(cursor.getR(), cursor.getG(), cursor.getB());
    }

    private void drawPressed() {
        PA.strokeWeight(0);
        PA.fill(pressed.getR(), pressed.getG(), pressed.getB(), pressed.getAlpha());
        PA.rect(posX, posY, width, height, roundness);
    }
}

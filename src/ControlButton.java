public class ControlButton extends Button {
    private Actionable action;

    private float textSize;
    private final String text;
    private Color textColor = new Color(0, 0, 0);
    private final int returnValue;

    public ControlButton(App app, float x, float y, float w, float h, String txt, int value) {
        super(app, x, y, w, h);
        textSize = height - height / 2f;
        text = txt;
        returnValue = value;
    }

    public void addAction(Actionable action) {
        this.action = action;
    }

    @Override
    public void action() {
        action.perform();
    }

    public void setTextColor(int r, int g, int b) {
        textColor = new Color(r, g, b);
    }

    public void setTextSize(float size) {
        textSize = size;
    }

    @Override
    public void draw() {
        super.draw();
        PA.textSize(textSize);
        PA.textAlign(3, 3); // CENTER, CENTER
        PA.fill(textColor.getR(), textColor.getG(), textColor.getB());
        PA.text(text, posX + width / 2f, posY + height / 2f - textSize / 8f);
    }
}

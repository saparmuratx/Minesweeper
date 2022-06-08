public class Panel {
    App PA;

    protected Color stroke = new Color(60, 60, 60);
    protected Color body = new Color(130, 130, 130);

    private float x;
    private float y;
    private float width;
    private float height;
    private String label = "Label";
    private String content = "Content";

    public Panel(App PA, float x, float y, float width, float height, String label, String content) {
        this.PA = PA;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void drawPanel() {
        PA.textSize(height * 0.5f);
        PA.fill(255);
        PA.text(label, x + width / 2f, y + height / 2f - height * 0.5f / 8f);

        PA.strokeWeight(2f);
        PA.stroke(stroke.getR(), stroke.getB(), stroke.getB());
        PA.fill(body.getR(), body.getG(), body.getB());
        PA.rect(x, y + height, width, height);

        PA.fill(200, 200);
        PA.text(content, x + width / 2f, y + height + height / 2f - height * 0.5f / 8f);
    }
}

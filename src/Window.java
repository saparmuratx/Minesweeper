public abstract class Window {
    App PA;
    Game game;
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;

    protected boolean exitOnESC = true;
    protected boolean exitOnClose = true;
    protected boolean windowIsOn = false;

    protected Color pressed = new Color(48, 179, 219, 100);
    protected Color cursor = new Color(2, 154, 219);
    protected Color stroke = new Color(60, 60, 60);
    protected Color body = new Color(130, 130, 130);

    protected float strokeWeight = 1.f;
    protected float roundness = 0;
    CloseButton close;

    public Window(App app, float x, float y, float w, float h) {
        PA = app;
        posX = x;
        posY = y;
        height = h;
        width = w;
        initClose();
    }

    public void setEngine(Game e) {
        game = e;
    }

    public void open() {
        windowIsOn = true;
    }

    protected void close() {
        windowIsOn = false;
    }

    protected abstract void closeWindow();

    public void initClose() {
        float x = posX + width - (width / 14);
        float y = posY;
        close = new CloseButton(PA, x, y, width / 14, width / 14);
        close.addAction(this::closeWindow);
        close.setPressed(new Color(255, 10, 10, 200));
    }

    public void draw() {
        if (windowIsOn) {
            PA.fill(body.getR(), body.getG(), body.getB(), 220);
            PA.stroke(stroke.getR(), stroke.getG(), stroke.getB(), 220);
            PA.rect(posX, posY, width, height, roundness);
            close.draw();
        }
    }

    public void process() {
    }
}

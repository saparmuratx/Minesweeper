import processing.core.PApplet;

public class App extends PApplet {
    Game game;
    private float windowWidth;
    private float windowHeight;
    private boolean mouseClicked = false;

    @Override
    public void settings() {
        fullScreen();
        // size(1000,600);
    }

    @Override
    public void setup() {
        windowWidth = width;
        windowHeight = height;
        game = new Game(this);
    }

    public float getWindowWidth() {
        return windowWidth;
    }

    public float getWindowHeight() {
        return windowHeight;
    }

    @Override
    public void mouseClicked() {
        mouseClicked = true;
    }

    public boolean isMouseClicked() {
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved() {
        mouseClicked = false;
    }

    @Override
    public void draw() {
        background(180);
        game.run();
    }

    public static void main(String[] args) {
        PApplet.main("App");
    }
}

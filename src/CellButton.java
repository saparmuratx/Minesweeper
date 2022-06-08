import processing.core.PApplet;

public class CellButton extends Button {
    Game game;

    private int flagCount = 0;
    private int iIndex;
    private int jIndex;

    private boolean covered = true;
    private boolean flagged = false;

    private int status = 0;
    // 0 - empty cell, 1-8 - counter, -1 - mine

    private Color emptyBody = new Color(160, 160, 160);
    private final Color emptyStroke = new Color(60, 60, 60);
    private final Color number = new Color(30, 30, 30);
    private final Color flag = new Color(255, 30, 18);
    private final Color mineFound = new Color(171, 17, 0);
    private Color mine = new Color(69, 69, 69);

    public CellButton(App app, float x, float y, float width, float height) {
        super(app, x, y, width, height);
    }

    // Setters:
    public void setEmptyBody(int r, int g, int b) {
        emptyBody = new Color(r, g, b);
    }

    public void setEmptyStroke(int r, int g, int b) {
        emptyBody = new Color(r, g, b);
    }

    public void setNumber(int r, int g, int b) {
        emptyBody = new Color(r, g, b);
    }

    public void setMineColor(int r, int g, int b) {
        emptyBody = new Color(r, g, b);
    }

    public void setEngine(Game game) {
        this.game = game;
    }

    public void setIndex(int i, int j) {
        iIndex = i;
        jIndex = j;
    }

    public void setMine() {
        status = -1;
    }

    public void mineCounter() {
        if (isMined())
            return;
        status++;
    }

    public void open() {
        if (isMined() && game.loseState == 0 && game.winState == 0) {
            mine = mineFound;
            game.loseState++;
        }
        covered = false;
    }

    // Getters:
    public int getCounter() {
        return status;
    }

    // Guessers:
    public boolean isMined() {
        return status == -1;
    }

    public boolean isCounter() {
        return status >= 1 && status <= 8;
    }

    public boolean isEmpty() {
        return status == 0;
    }

    public boolean flagsMatch() {
        return status == flagCount;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isOpen() {
        return !covered;
    }

    // Action time
    @Override
    public void action() {
        if (PA.mouseButton == PA.LEFT && !flagged) {
            game.open(iIndex, jIndex);
        }
        if (PA.mouseButton == PA.RIGHT && game.getFlagsCount() < game.MINES) {
            if (flagged)
                unflag();
            else
                flag();
        }
    }

    // Flag & Unflag
    public void flag() {
        if (!covered)
            return;
        flagged = true;

        game.flagCounter(iIndex + 1, jIndex + 1, 1);
        game.flagCounter(iIndex - 1, jIndex - 1, 1);
        game.flagCounter(iIndex + 1, jIndex - 1, 1);
        game.flagCounter(iIndex - 1, jIndex + 1, 1);
        game.flagCounter(iIndex, jIndex - 1, 1);
        game.flagCounter(iIndex, jIndex + 1, 1);
        game.flagCounter(iIndex + 1, jIndex, 1);
        game.flagCounter(iIndex - 1, jIndex, 1);
    }

    public void unflag() {
        if (!covered)
            return;
        flagged = false;
        game.flagCounter(iIndex + 1, jIndex + 1, -1);
        game.flagCounter(iIndex - 1, jIndex - 1, -1);
        game.flagCounter(iIndex + 1, jIndex - 1, -1);
        game.flagCounter(iIndex - 1, jIndex + 1, -1);
        game.flagCounter(iIndex, jIndex - 1, -1);
        game.flagCounter(iIndex, jIndex + 1, -1);
        game.flagCounter(iIndex + 1, jIndex, -1);
        game.flagCounter(iIndex - 1, jIndex, -1);
    }

    public void flagCounter(int cnt) {
        flagCount += cnt;
    }

    // Drawing:
    @Override
    public void draw() {
        if (covered) {
            super.draw();
            if (flagged)
                drawFlag();
        } else {
            triggerAction();
            drawEmpty();
            if (isMined())
                drawBomb();
            else if (isCounter())
                drawNum();
        }
        // text(flagged+"", 20, new Color(0, 0, 0));
    }

    // EMPTY CELL
    private void drawEmpty() {
        PA.stroke(emptyStroke.getR(), emptyStroke.getG(), emptyStroke.getB());
        PA.strokeWeight(0);
        PA.fill(emptyBody.getR(), emptyBody.getG(), emptyBody.getB());
        PA.rect(posX, posY, width, height, roundness);
    }

    // FLAG
    private void drawFlag() {
        PA.pushMatrix();
        PA.fill(flag.getR(), flag.getG(), flag.getB());
        PA.textSize(height - height / 2f);
        PA.textAlign(PA.CENTER, PA.CENTER);
        PA.text("!", posX + width / 2.f, posY + height / 2.f - height / 15);
        PA.popMatrix();
    }

    // NUMBER
    private void drawNum() {
        PA.fill(emptyStroke.getR(), emptyStroke.getG(), emptyStroke.getB());
        PA.textSize(height - height / 2f);
        PA.textAlign(PA.CENTER, PA.CENTER);
        PA.text(status, posX + width / 2.f, posY + height / 2.f - height / 10);
    }

    private void text(String text, float textSize, Color textColor) {
        PA.fill(textColor.getR(), textColor.getG(), textColor.getB());
        PA.textSize(textSize);
        PA.textAlign(PA.CENTER, PA.CENTER);
        PA.text(text, posX + width / 2.f, posY + height / 2.f - height / 10);
    }

    // BOMB
    private void drawBomb() {
        PA.pushMatrix();
        bomb(posX + width / 2.f, posY + height / 2.f);
        PA.popMatrix();
    }

    private void bomb(float x, float y) {
        PA.pushMatrix();
        PA.translate(x, y);
        PA.stroke(mine.getR(), mine.getG(), mine.getB());
        PA.fill(mine.getR(), mine.getG(), mine.getB());
        PA.ellipse(0, 0, height / 3f, width / 3f);
        float angle = 60;
        PA.strokeWeight(height / 12);
        for (int i = 0; i < 6; i++) {
            PA.rotate(PApplet.radians(angle));
            PA.line(0, 0, width / 4.f, 0);
        }
        PA.popMatrix();
    }
}

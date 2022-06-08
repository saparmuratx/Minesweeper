public class ResultWindow extends Window {
    App PA;
    private boolean windowOn = false;
    private String mines = "9";
    private String time = "00:00";
    private String message = "No Message Yet!";
    private String size = "10X10";
    private ControlButton next;
    private Panel minesP;
    private Panel sizeP;
    private Panel timeP;

    public ResultWindow(App PA, float x, float y, float width, float height) {
        super(PA, x, y, width, height);
        this.PA = PA;
        initNext();
        initPanels();
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void setTime(String t) {
        time = t;
    }

    public void setMines(int m) {
        mines = m + "";
    }

    public void setSize(int h, int w) {
        size = h + "x" + w;
    }

    @Override
    protected void closeWindow() {
        game.enableMainFrame();
        if (game.loseState == 3)
            game.loseState++;
        if (game.winState == 3)
            game.winState++;
        close();
    }

    @Override
    public void process() {
        close();
        game.newField(game.difficulty);
    }

    public void initNext() {
        float buttonW = width / 5;
        float buttonH = height / 7.5f;
        float buttonX = posX + (width - buttonW) / 2;
        float buttonY = posY + height - buttonH * 1.5f;
        next = new ControlButton(PA, buttonX, buttonY, buttonW, buttonH, "Next", 0);
        next.addAction(this::process);
    }

    private void initPanels() {
        float panelW = width / 5;
        float panelH = height / 7.5f;
        float panelX = posX + (width - panelW) / 2;
        float panelY = posY + height - panelH * 4;
        float space = width / 15f;

        timeP = new Panel(PA, panelX, panelY, panelW, panelH, "Time", time);
        minesP = new Panel(PA, panelX - space - panelW, panelY, panelW, panelH, "Mines", mines + "");
        sizeP = new Panel(PA, panelX + space + panelW, panelY, panelW, panelH, "Size", size);
    }

    @Override
    public void draw() {
        super.draw();
        if (windowIsOn) {
            PA.fill(200, 220);
            PA.textAlign(3, 3); // CENTER, CENTER
            PA.textSize(height / 15f);
            PA.text(message, posX + width / 2f, posY + height / 3.5f);
            drawResults();
            next.draw();
        }
    }

    private void drawResults() {
        timeP.updateContent(time);
        minesP.updateContent(mines);
        sizeP.updateContent(size);
        timeP.drawPanel();
        minesP.drawPanel();
        sizeP.drawPanel();
    }

}

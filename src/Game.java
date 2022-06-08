public class Game {
    private final App PA;
    private CellButton[][] field;

    // Current options:
    protected int MINES = 10;
    private int currentH = 9;
    private int currentW = 9;
    protected int difficulty = 1;

    // Current measures and positions:
    private float cellSize;
    private float buttonW;
    private float buttonH;
    private float buttonX;
    private float buttonY;

    // Result window:
    private ResultWindow resultWindow;
    private float resultWindowH;
    private float resultWindowW;
    private float resultWindowX;
    private float resultWindowY;

    // Info panels:
    private Panel timer;
    private Panel flags;

    private float border; // Border of game frame and control frame
    private float baseX;
    private float baseY;

    // Game process controllers:
    private boolean firstClick = true;

    // ControlButtons:
    ControlButton newGame;
    ControlButton easy;
    ControlButton medium;
    ControlButton hard;

    // Timer:
    private long begin;

    //
    protected int loseState = 0;
    protected int winState = 0;

    public Game(App PA) {
        this.PA = PA;
        calibrate();
        // shuffle();
        initControlButtons();
        initResultWindow();
        newField(difficulty);
        initInfoPanel();
    }

    // Initializers:
    private void initInfoPanel() {
        timer = new Panel(PA, buttonX + buttonW / 2f, buttonY - buttonH * 2f - 20f, buttonW, buttonH, "Time",
                getTime());
        flags = new Panel(PA, buttonX - buttonW / 2f, buttonY - buttonH * 2f - 20f, buttonW, buttonH, "Flags",
                (MINES - getFlagsCount()) + "");
    }

    private void initControlButtons() {
        newGame = new ControlButton(PA, buttonX, buttonY, buttonW, buttonH, "New game", 0);
        easy = new ControlButton(PA, buttonX, buttonY + (buttonH + 20), buttonW, buttonH, "Easy", 1);
        medium = new ControlButton(PA, buttonX, buttonY + (buttonH + 20) * 2, buttonW, buttonH, "Medium", 2);
        hard = new ControlButton(PA, buttonX, buttonY + (buttonH + 20) * 3, buttonW, buttonH, "Hard", 3);
        newGame.addAction(() -> newField(0));
        easy.addAction(() -> newField(1));
        medium.addAction(() -> newField(2));
        hard.addAction(() -> newField(3));
    }

    private void initResultWindow() {
        resultWindow = new ResultWindow(PA, resultWindowX, resultWindowY, resultWindowW, resultWindowH);
        resultWindow.open();
        resultWindow.setEngine(this);
    }

    // Calculates coordinates and positions to provide resizebility of window
    private void calibrate() {
        float easyRatio = 0.30966325036f;
        float mediumRatio = 0.44509516837f;
        float hardRatio = 0.7027818448f;
        float ratio = 0;

        float width = PA.getWindowWidth();
        float height = PA.getWindowHeight();
        border = width / 4;
        float gameFrameWidth = width - border;

        if (currentW == 9) {
            ratio = easyRatio;
        }
        if (currentW == 16) {
            ratio = mediumRatio;
        }
        if (currentW == 30) {
            ratio = hardRatio;
        }

        baseX = border + (gameFrameWidth - (ratio * width)) / 2;
        baseY = (height - (ratio * width) * (1.f * currentH / currentW)) / 2;
        cellSize = (ratio * width) / currentW - 2;
        buttonW = border / 2.5f;
        buttonH = height / 18;
        buttonX = border / 2 - buttonW / 2;
        buttonY = (height - 4 * buttonH - 3 * 20) / 2;
        resultWindowH = height / 2f;
        resultWindowW = width / 2.5f;
        resultWindowX = width / 2f - resultWindowW / 2f;
        resultWindowY = (height - resultWindowH) / 2;
    }

    private void resetGameSettings() {
        loseState = 0;
        winState = 0;
        enableMainFrame();
        if (resultWindow != null)
            resultWindow.open();
        firstClick = true;
    }

    protected void setByDifficulty() {
        switch (difficulty) {
            case 1:
                currentW = 9;
                currentH = 9;
                MINES = 10;
                break;
            case 2:
                currentW = 16;
                currentH = 16;
                MINES = 40;
                break;
            case 3:
                currentW = 30;
                currentH = 16;
                MINES = 99;
                break;
            default:
                break;
        }
    }

    protected void newField(int d) {
        resetGameSettings();
        difficulty = d;
        setByDifficulty();
        calibrate();
        field = Utils.createField(PA, this, currentH, currentW, MINES, baseX, baseY, cellSize, cellSize);
    }

    public void flagCounter(int i, int j, int cnt) {
        if (inField(i, j))
            field[i][j].flagCounter(cnt);
    }

    private void resetTimer() {
        begin = System.currentTimeMillis();
    }

    // Guessers:
    public boolean inField(int i, int j) {
        return i < currentH && j < currentW && i >= 0 && j >= 0;
    }

    private int getCoveredCells() {
        int counter = 0;
        for (CellButton[] cellButtons : field) {
            for (CellButton cellButton : cellButtons) {
                if (!cellButton.isOpen())
                    counter++;
            }
        }
        return counter;
    }

    protected int getFlagsCount() {
        int counter = 0;
        for (CellButton[] cellButtons : field) {
            for (CellButton cellButton : cellButtons) {
                if (cellButton.isFlagged())
                    counter++;
            }
        }
        return counter;
    }

    private String getTime() {
        if (firstClick)
            return "00:00";
        long end = System.currentTimeMillis();
        end -= begin;
        long sec = end / 1000;
        long min = end / 60000;
        StringBuilder time = new StringBuilder();

        if (min < 10)
            time.append(0);
        time.append(min);
        time.append(':');
        if ((sec % 60) < 10)
            time.append(0);
        time.append((sec % 60));

        return time.toString();
    }

    // Action:
    public void open(int i, int j) {
        if (firstClick) {
            while (!field[i][j].isEmpty()) {
                newField(difficulty);
            }
            firstClick = false;
            resetTimer();
        }

        if (field[i][j].isCounter() && field[i][j].flagsMatch() && field[i][j].isOpen()) {
            quickOpen(i + 1, j + 1);
            quickOpen(i - 1, j - 1);
            quickOpen(i + 1, j - 1);
            quickOpen(i - 1, j + 1);
            quickOpen(i, j - 1);
            quickOpen(i, j + 1);
            quickOpen(i + 1, j);
            quickOpen(i - 1, j);
        } else if (field[i][j].isEmpty()) {
            openEmpty(i, j);
        } else
            field[i][j].open();
    }

    public void openEmpty(int i, int j) {
        if (!inField(i, j) || field[i][j].isMined() || field[i][j].isOpen() || field[i][j].isFlagged())
            return;
        if (field[i][j].isCounter() || field[i][j].isEmpty()) {
            field[i][j].open();
        }
        if (field[i][j].isEmpty()) {
            openEmpty(i - 1, j - 1);
            openEmpty(i + 1, j + 1);
            openEmpty(i + 1, j - 1);
            openEmpty(i - 1, j + 1);
            openEmpty(i, j - 1);
            openEmpty(i, j + 1);
            openEmpty(i + 1, j);
            openEmpty(i - 1, j);
        }
    }

    protected void quickOpen(int i, int j) {
        if (!inField(i, j) || field[i][j].isFlagged())
            return;
        if (field[i][j].isCounter() || field[i][j].isMined()) {
            field[i][j].open();
        }
        if (field[i][j].isEmpty())
            openEmpty(i, j);
    }

    protected void enableMainFrame() {
        newGame.enable();
        easy.enable();
        medium.enable();
        hard.enable();
    }

    protected void disableMainFrame() {
        newGame.disable();
        easy.disable();
        medium.disable();
        hard.disable();
        for (CellButton[] cellButtons : field) {
            for (CellButton cellButton : cellButtons) {
                cellButton.disable();
            }
        }
    }

    protected void openCovered() {
        for (CellButton[] cellButtons : field) {
            for (CellButton cellButton : cellButtons) {
                if (!cellButton.isOpen() && !cellButton.isFlagged()) {
                    cellButton.open();
                }
            }
        }
    }

    private void openMines() {
        for (CellButton[] cellButtons : field) {
            for (CellButton cellButton : cellButtons) {
                if (cellButton.isMined() && !cellButton.isFlagged()) {
                    cellButton.open();
                }
            }
        }
    }

    // Running:
    public void run() {
        drawField();
        drawControlButtons();
        drawInfoPanels();

        if (winState > 0 && loseState == 0) {
            win();
        }

        if (loseState > 0 && winState == 0) {
            lose();
        }

        if (getCoveredCells() == MINES && winState == 0)
            winState = 1;
    }

    private int cnt;

    private void lose() {
        if (loseState == 4) {
            enableMainFrame();
        }
        if (loseState == 3) {
            resultWindow.draw();
        }
        if (loseState == 2) {
            cnt++;
            if (cnt == 20)
                loseState = 3;
        }
        if (loseState == 1) {
            resultWindow.setMessage("You lose. Try again!");
            resultWindow.setTime(getTime());
            resultWindow.setMines(MINES);
            resultWindow.setSize(currentH, currentW);
            disableMainFrame();
            openMines();
            loseState = 2;
            cnt = 0;
        }

    }

    private void win() {
        if (winState == 4) {
            enableMainFrame();
        }
        if (winState == 3) {
            resultWindow.draw();
        }
        if (winState == 2) {
            cnt++;
            if (cnt >= 20)
                winState = 3;
        }
        if (winState == 1) {
            resultWindow.setMessage("Congratulations, you won!");
            resultWindow.setTime(getTime());
            resultWindow.setMines(MINES);
            resultWindow.setSize(currentH, currentW);
            disableMainFrame();
            openCovered();
            winState = 2;
            cnt = 0;
        }

    }

    // Drawing:
    private void drawField() {
        for (CellButton[] cellButtons : field) {
            for (CellButton cellButton : cellButtons) {
                cellButton.draw();
            }
        }
    }

    private void drawControlButtons() {
        newGame.draw();
        easy.draw();
        medium.draw();
        hard.draw();
    }

    private void drawInfoPanels() {
        if (winState == 0 && loseState == 0)
            timer.updateContent(getTime());
        flags.updateContent((MINES - getFlagsCount()) + "");
        timer.drawPanel();
        flags.drawPanel();
    }

}

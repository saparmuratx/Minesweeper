public class Utils {
    private static CellButton[][] field;
    private static float SPACE = 2f;
    private int MINES = 10;
    private static int currentH = 9;
    private static int currentW = 9;
    private int difficulty = 1;
    App PA;
    Game game;

    public static CellButton[][] createField(App PA, Game game, int h, int w, int mines, float baseX, float baseY,
            float height, float width) {
        field = new CellButton[h][w];
        currentH = h;
        currentW = w;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                field[i][j] = new CellButton(PA, baseX + j * width + (j) * SPACE, baseY + i * height + (i) * SPACE,
                        width, height);
                field[i][j].setIndex(i, j);
                field[i][j].setEngine(game);
            }
        }

        setMines(mines);

        return field;
    }

    public static void setMines(int n) {
        while (n > 0) {
            /*
             * This while-loop is very powerful.
             * Indeed no one could understand true power of this while-loop.
             * Even I. But the hope dies last.
             * I know one day Mother Earth will give birth to the human,
             * who will eventually understand and poses power of this while-loop.
             * He or she will set peace upon IT companies
             * and will be source of wisdom for programmers from different fields.
             * That time will be written in history as era of efficient programming.
             */
            int i = (int) (Math.random() * (currentH));
            int j = (int) (Math.random() * (currentW));
            if (inField(i, j) && !field[i][j].isMined()) {
                field[i][j].setMine();
                increment(i + 1, j + 1);
                increment(i - 1, j - 1);
                increment(i + 1, j - 1);
                increment(i - 1, j + 1);
                increment(i, j - 1);
                increment(i, j + 1);
                increment(i + 1, j);
                increment(i - 1, j);
                n--;
            }
        }
    }

    private static void increment(int i, int j) {
        if (inField(i, j))
            field[i][j].mineCounter();
    }

    public static boolean inField(int i, int j) {
        return i < currentH && j < currentW && i >= 0 && j >= 0;
    }

}

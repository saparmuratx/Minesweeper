public class Color {
    private final int R;
    private final int G;
    private final int B;
    private final int alpha;

    public Color(int r, int g, int b, int a) {
        R = r;
        G = g;
        B = b;
        alpha = a;
    }

    public Color(int r, int g, int b) {
        R = r;
        G = g;
        B = b;
        alpha = 255;
    }

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }

    public int getAlpha() {
        return alpha;
    }
}

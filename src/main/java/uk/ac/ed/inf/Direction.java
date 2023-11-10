package uk.ac.ed.inf;

public enum Direction {
    E(0.0), ENE(22.5), NE(45.0), NNE(67.5),
    N(90.0), NNW(112.5), NW(135.0), WNW(157.5),
    W(180.0), WSW(202.5), SW(225.0), SSW(247.5),
    S(270.0), SSE(292.5), SE(315.0), ESE(337.5),
    HOVER(999);

    /** Angle associated with the specific direction. */
    public final Double angle;

    Direction(Double angle) {
        this.angle = angle;
    }
    Direction(int angle) {
        this.angle = (double) angle;
    }
}


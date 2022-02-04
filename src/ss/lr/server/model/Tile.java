package ss.lr.server.model;

/***
 Simple Enum to represent tiles,each enum has named and value,value is its score.
 Added name for tiles like BLANK and EMPTY.
 @author Lukas Reika s2596237.
 */

public enum Tile {
    A(1, "A"),
    B(3, "B"),
    C(3, "C"),
    D(2, "D"),
    E(1, "E"),
    F(4, "F"),
    G(2, "G"),
    H(4, "H"),
    I(1, "I"),
    J(8, "J"),
    K(5, "K"),
    L(1, "L"),
    M(3, "M"),
    N(1, "N"),
    O(1, "O"),
    P(3, "P"),
    Q(10, "Q"),
    R(1, "R"),
    S(1, "S"),
    T(1, "T"),
    U(1, "U"),
    V(4, "V"),
    W(4, "W"),
    X(8, "X"),
    Y(4, "Y"),
    Z(10, "Z"),
    BLANK(0, "0"),
    EMPTY(0, "-");

    public final int value;
    public final String name;
    Tile(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return this.value;
    }

}



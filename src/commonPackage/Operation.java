package commonPackage;

import java.io.Serializable;;
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private int operande1;      // premier opérande
    private char operateur;     // + - * /
    private int operande2;      // deuxième opérande

    public Operation(int operande1, char operateur, int operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public int getOperande1() {
        return operande1;
    }

    public char getOperateur() {
        return operateur;
    }

    public int getOperande2() {
        return operande2;
    }

    @Override
    public String toString() {
        return operande1 + " " + operateur + " " + operande2;
    }
}
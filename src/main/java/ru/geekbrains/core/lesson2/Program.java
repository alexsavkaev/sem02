package ru.geekbrains.core.lesson2;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Program {
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static  int FIELD_SIZE; // константа для размера поля
    private static final int WIN_COUNT = 4;  // Количество очков, которое нужно получить, чтобы выиграть

    /**
     * Инициализация поля, с вводом размера.
     * При некорректном вводе в цикле выбрасывается (и тут же ловится) исключение InputMismatchException.
     * Цикл продолжается до тех пор, пока размер поля не будет получен.
     */
    public static void initialize() {
        while (true) {
        System.out.print("Введите размер поля: ");
        try {
            FIELD_SIZE = scanner.nextInt();
            break;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // чистим буфер
        }
    }
        fieldSizeX = FIELD_SIZE;
        fieldSizeY = FIELD_SIZE;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;

            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField() {
        System.out.print("  ");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println(" ");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + " |");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeX * 2 + 3; i++) {
            System.out.print("‾");
        }
        System.out.println();
    }

    /**
     * Ход человека. Человек вводит свои координаты игровой клетки. Если клетка свободна и в пределах поля,
     *  то в клетке ставится его символ.
     *  Я переместил метод checkState() внутрь методов хода, чтобы не проверять после каждого хода всё поле целиком,
     *  а проверять только те варианты, которые связаны с произошедшим ходом. Это сделано с учетом того, что
     *  поле может быть и 1000х1000 и более, таким образом мы экономим ресурсы на проверку победы.
     * @return Вызов метода для проверки победы.
     */

    public static boolean humanTurn() {
        int x;
        int y;
        do {
            try {
                System.out.printf("Введите координаты хода X и Y\n(от 1 до %d) через пробел: ", FIELD_SIZE);
                x = scanner.nextInt() - 1;
                y = scanner.nextInt() - 1;
            } catch (InputMismatchException e) {
                x = y = -1;
            }
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
        return checkState(x, y, DOT_HUMAN);
    }

    /**
     * Ход игрока компьютера.
     * @return  Вызов метода для проверки победы.
     */
    public static boolean aiTurn() {
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
        checkState(x, y, DOT_AI);
        return checkState(x, y, DOT_AI);
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     *
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * \
     * Проверка валидности координат
     *
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка на ничью (все ячейки игрового поля заполнены фишками человека или компьютера)
     *
     * @return результат проверки
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

//    /**
//     * TODO: Переработать в дз
//     * Метод проверки победы
//     *
//     * @param dot фишка игрока
//     * @return результат проверки победы
//     */
//    static boolean checkWin(char dot) {
//        // проверка по трем горизонталям
//        if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot) return true;
//        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot) return true;
//        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot) return true;
//        // проверка по трем вертикалям
//        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot) return true;
//        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot) return true;
//        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot) return true;
//        // проверка по двум диагоналям
//        if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) return true;
//        if (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot) return true;
//        return false;
//    }

    /**
     * Проверка на победу. Собирает функции проверки во все стороны от заданной точки
     * @param x координата X
     * @param y координата Y
     * @param symbol Символ для сверки
     * @return результат проверки победы
     */
    public static boolean checkWin(int x, int y, char symbol) {
            try {
                if (checkHorizontal(x, y, symbol)) return true;
                if (checkVertical(x, y, symbol)) return true;
                if (checkDiagonalLeftUp(x, y, symbol)) return true;
                return checkDiagonalRightUp(x, y, symbol);
            }catch (IndexOutOfBoundsException e) {
                return false;
            }
    }

    /**
     * Проверка на победу по горизонтали. Во внутреннем цикле, с исходной точки, проверяются все доступные клетки в право,
     * затем, во внешнем цикле, исходная точка смещается влево и внутренний цикл запускается снова.
     * @param x координата X
     * @param y координата Y
     * @param symbol Символ для сверки
     * @return результат проверки победы
     */
    static boolean checkHorizontal(int x, int y, char symbol) {
            for (int i = 0; i < WIN_COUNT; i++) {
                boolean win = true;
                for (int j = 0; j < WIN_COUNT; j++) {
                    if (x - i + j >= FIELD_SIZE || x - i + j < 0 || field[x - i + j][y] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return true;
            }
            return false;
        }

    /** Проверка на победу по вертикали. Во внутреннем цикле, с исходной точки, проверяются все доступные клетки вниз,
     * затем, во внешнем цикле, исходная точка смещается вверх и внутренний цикл запускается снова.
     *
     * @param x координата X
     * @param y координата Y
     * @param symbol    Символ для сверки
     * @return результат проверки победы
     */

    static boolean checkVertical(int x, int y, char symbol) {
        for (int i = 0; i < WIN_COUNT; i++) {
            boolean win = true;
            for (int j = 0; j < WIN_COUNT; j++) {
                if (y - i + j >= FIELD_SIZE || y - i + j < 0 || field[x][y - i + j] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        return false;
    }

    /**
     * Проверка на победу по диагонали. Во внутреннем цикле, с исходной точки, проверяются все доступные клетки в
     * право-вниз, затем, во внешнем цикле, исходная точка смещается влево-вверх и внутренний цикл запускается снова.
     * @param x координата X
     * @param y координата Y
     * @param symbol символ для сверки
     * @return результат проверки победы
     */
    static boolean checkDiagonalLeftUp(int x, int y, char symbol) {

        for (int i = 0; i < WIN_COUNT; i++) {
            boolean win = true;
            for (int j = 0; j < WIN_COUNT; j++) {
                int dxLeftUp = x - i + j;
                int dyLeftUp = y - i + j;
                if (dxLeftUp < 0 || dxLeftUp >= field.length || dyLeftUp < 0 || dyLeftUp >= field[0].length) {
                    win = false;
                    break;
                }
                if (field[dxLeftUp][dyLeftUp] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        return false;
    }
    /**
     * Проверка на победу по диагонали. Во внутреннем цикле, с исходной точки, проверяются все доступные клетки в
     * лево-вниз, затем, во внешнем цикле, исходная точка смещается вправо-вверх и внутренний цикл запускается снова.
     * @param x координата X
     * @param y координата Y
     * @param symbol символ для сверки
     * @return результат проверки победы
     */
    static boolean checkDiagonalRightUp(int x, int y, char symbol) {

        for (int i = 0; i < WIN_COUNT; i++) {
            boolean win = true;
            for (int j = 0; j < WIN_COUNT; j++) {
                int dxRightUp = x + i - j;
                int dyRightUp = y - i + j;
                if (dxRightUp < 0 || dxRightUp >= field.length || dyRightUp < 0 || dyRightUp >= field[0].length) {
                    win = false;
                    break;
                }
                if (field[dxRightUp][dyRightUp] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        return false;
    }

    /**
     * Проверка на победу/ничью исходной точки. Вызывается после хода игрока, принимает на вход координаты хода
     * @param x координата X
     * @param y координата Y
     * @param dot символ для сверки
     * @return результат проверки победы
     */
    static boolean checkState(int x, int y, char dot) {
        if (checkWin(x, y, dot )) {
            System.out.printf("%s победил! \n", dot); // победа!
            return true;
        } else if (checkDraw()) {
            System.out.println("Ничья");
            return true;
        }
        // игра продолжается
        return false;
    }

    public static void main(String[] args) {
        do {
            initialize();
            printField();
            while (true) {
                if (humanTurn()) break;
                printField();
                if (aiTurn()) break;
                printField();
            }
            printField(); // выводим поле после игры
            System.out.println("Желаете сыграть еще раз? (y - да): ");
        } while (scanner.next().equalsIgnoreCase("Y"));
    }
}

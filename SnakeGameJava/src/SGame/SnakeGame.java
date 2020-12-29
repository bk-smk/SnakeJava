package SGame;

import javax.swing.*;
import java.sql.SQLException;

public class SnakeGame {
    // Двумерный массив для хранения игрового поля.
    public int[][] mas;
    // Текущее направление движения.
    public int direction;
    // Новое направление движения.
    public int newDirection;
    // Координаты головы змейки.
    private int hX, hY;
    // Количество очков.
    public int score;
    // Количество очков в конце игры.
    private int finalScore;
    // Длина змейки.
    private int snakeLength;
    // Признак окончания игры.
    public boolean endGame;
    // Имя игрока.
    private String playerName;

    // Конструктор класса.
    public SnakeGame() {
        //Создаем новый массив 16x16.
        mas = new int[30][30];
    }

    // Изменение направления движения змейки (поворот).
    private void turnSnake() {
        if (Math.abs((newDirection - direction)) != 2) {
            direction = newDirection;
        }
    }

    // Генерация нового объекта в случайном месте.
    private void generateNew() {
      //Глухой (бесконечный цикл)
      while (true) {
          int x = (int)(Math.random()*30);
          int y = (int)(Math.random()*30);
    // Если в этом месте массива нулевое значение,
    // то помещаем туда объект для поедания змейкой
    // и прерываем цикл.
      if (mas[y][x] == 0) {
            mas[y][x] = -1;
            break;
        }
    }
}

    // Начало игры.
    public void start() {
    // Заполняем весь массив нулями.
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                mas[i][j] = 0;
            }
        }
    // Начальное направление - влево.
        direction = 0;
    // Обнуляем старое направление, чтобы оно не срабатывало при начале новой игры.
        newDirection = 0;
    // Начальное количество очков.
        score = 0;
    // Создаем начальную змейку с длиной три ячейки.
        mas[14][14] = 1;
        mas[14][15] = 2;
        mas[14][16] = 3;
        snakeLength = 3;
    // Записываем в переменные координаты головы змейки.
        hX = hY = 14;
    // Признак окончания игры.
        endGame = false;
    // Формируем первый объект для поедания змейкой.
        generateNew();
    }

    // Перемещение головы змейки.
    public int snakeHeadMove() {
    // Если текущее направление влево.
        if (direction == 0) {
            if ((hX - 1) >= 0)
                hX--;
            else
                hX = 29;
        }
    // Если текущее направление вверх.
        else if (direction == 1) {
            if ((hY - 1) >= 0)
                hY--;
            else
                hY = 29;
        }
    // Если текущее направление вправо.
        else if (direction == 2) {
            if ((hX + 1) <= 29)
                hX++;
            else
                hX = 0;
        }

    // Если текущее направление вниз.
        else if (direction == 3) {
            if ((hY + 1) <= 29)
                hY++;
            else
                hY = 0;
        }
    // Метод возвращает тот или иной результат перемещения в зависимости от условия.
    // Результат.
        int lastHeadPosition = 0;
    // Если съеден объект.
        if (mas[hY][hX] == -1)
            lastHeadPosition = 1;
    // Если перемещение в пустую ячейку.
        else if (mas[hY][hX] == 0)
            lastHeadPosition = 2;
    // Если попадание в туловище змейки.
        else if (mas[hY][hX] > 0)
            lastHeadPosition = 3;
    // В место перемещения головы установим значение - минус два.
        mas[hY][hX] = -2;
    // Возвращаем результат.
        return lastHeadPosition;
    }

    // Перемещение змейки.
    public void moveSnake() {
        // Перемещаем голову змейки и получаем результат перемещения.
        int flag = snakeHeadMove();
        // Если змейка врезалась в себя, то признак окончания игры устанавливаем в истину.
        if (flag == 3) {
            endGame = true;
            // Вызываем метод записи имени игрока.
            enterName();
            // Вызываем метод сохранения имени игрока и очков в базу данных.
            try {
                saveData();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Два вложенных цикла, которые перебирают все элементы двухмерного массива.
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                // Если значение больше нуля, то увеличиваем на 1.
                if (mas[i][j] > 0)
                    mas[i][j]++;
                // Если значение минус два,то меняем его на 1.
                else if (mas[i][j] == -2)
                    mas[i][j]=1;
                // Если не было поедания объекта.
                if (flag != 1) {
                    // Если это хвост, то записываем в него нулевое значение.
                    if (mas[i][j] == (snakeLength + 1))
                        mas[i][j] = 0;
                }
            }
        }
         // Если съеден объект.
        if (flag == 1) {
            // Увеличиваем длину змейки на единицу.
            snakeLength++;
            // Генерируем новый объект для поедания.
            generateNew();
            // Увеличиваем количество очков на 10.
            score += 10;
            finalScore = score;
        }
        // Изменяем направление движения.
        turnSnake();
    }

    // Метод для записи имени и результата игры в базу данных.
    public void saveData() throws SQLException, ClassNotFoundException {
        String fScore = Integer.toString(finalScore);
        if (playerName != null && playerName.length() != 0) {
            DataBase.writePlayersName(playerName, fScore);
        }
    }

    // Окно для ввода имени игрока.
    public void enterName() {
        JOptionPane nameOption = new JOptionPane();
        playerName = nameOption.showInputDialog(null, "Введите свое имя:", "Конец игры", JOptionPane.INFORMATION_MESSAGE);
            if (playerName != null) {
                endGame = true;
            }
            if (playerName != null) {
                if (playerName.length() == 0) {
                    nameOption.showMessageDialog(null, "Имя не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    enterName();
            }
        }
    }
}



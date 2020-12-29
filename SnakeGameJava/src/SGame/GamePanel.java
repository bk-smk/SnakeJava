package SGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;

public class GamePanel extends JPanel {
    // Переменная для реализации логики игры.
    private SnakeGame snakeGame;
    // Таймер отрисовки.
    private Timer tmDraw, tmUpdate;
    // Изображения, используемые в игре.
    private Image dot, apple, wallpaper, endGame;
    // Надпись для количества очков.
    private JLabel score;
    // Три кнопки.
    private JButton btn1, btn2, btn3;
    // Ссылка на панель.
    private GamePanel panel;

    // Класс для обработки событий от клавиатуры.
    private class GameKeys implements KeyListener { // Интерфейс KeyListener имеет 3 метода.
        // Метод при нажатии на клавишу.
        public void keyPressed(KeyEvent e) {
            // Получение кода нажатой клавиши.
            int key = e.getKeyCode();
            // Если происходит нажатие одной из четырех клавиш (стрелочек), то изменяется направление змейки.
            if (key == KeyEvent.VK_LEFT) snakeGame.newDirection = 0;
            else if (key == KeyEvent.VK_UP) snakeGame.newDirection = 1;
            else if (key == KeyEvent.VK_RIGHT) snakeGame.newDirection = 2;
            else if (key == KeyEvent.VK_DOWN) snakeGame.newDirection = 3;
        }
        // Метод вызывается после того, как пользователь нажмет и отпустит любую клавишу.
        public void keyReleased(KeyEvent e) {
        }
        // Метод срабатывает каждый раз, когда пользователь вводит символы Unicode.
        public void keyTyped(KeyEvent e) {
        }
    }

    // Конструктор класса.
    public GamePanel() {
        // Помещаем ссылку на саму панель в переменную.
        panel = this;
        // Делаем панель в фокусе - для приема событий от клавиатуры.
        this.setFocusable(true);
        // Подключение обработчика события для клавиатуры к панели.
        this.addKeyListener(new GameKeys());


        // Присваиваем переменным считанные файлы изображений.
        try {
            dot = ImageIO.read(new FileInputStream("resource/dot.png"));
            apple = ImageIO.read(new FileInputStream("resource/apple.png"));
            wallpaper = ImageIO.read(new FileInputStream("resource/wallpaper.jpg"));
            endGame = ImageIO.read(new FileInputStream("resource/GameOver.png"));
        } catch (IOException ex) {}

        // Создаем объект новой игры.
        snakeGame = new SnakeGame();
        // Запускаем игру.
        snakeGame.start();
        // Создаем, настраиваем и запускаем таймер для отрисовки игрового поля.
        tmDraw = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Вызываем перерисовку - paintComponent().
                repaint();
            }
        });
        tmDraw.start();

        // Создаем, настраиваем и запускаем таймер для изменения логики игры.
        tmUpdate = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Если не конец игры, то перемещаем змейку.
                if (snakeGame.endGame == false) {
                    snakeGame.moveSnake();
                }
                // Выводим информацию о количестве очков.
                score.setText("Счет: " + snakeGame.score);
            }
        });
        tmUpdate.start();

        // Включаем возможность произвольного размещения элементов интерфейса на панели.
        setLayout(null);
        // Добавляем текстовую надпись, которая показывает счет игры.
        score = new JLabel("Счет: 0");
        score.setForeground(Color.WHITE);
        score.setFont(new Font("Times New Roman", 0, 24));
        score.setBounds(665, 180, 150, 50);
        add(score);

        // Указываем цвет, шрифт, расположение и в конце добавляем ее к панели.
        // Затем добавляем кнопку Новая игра.
        // К кнопке подключается обработчик события при нажатии на кнопку.
        // При нажатии вызывается метод: snakeGame.start(), который сбрасывает значения в массиве на начальные, и игра перезапускается.
        btn1 = new JButton();
        btn1.setText("Новая игра");
        btn1.setForeground(Color.BLUE);
        btn1.setFont(new Font("Times New Roman", 0, 20));
        btn1.setBounds(630, 30, 150, 40);
        btn1.addActionListener(new ActionListener() {
            // Обработчик события при нажатии на кнопку Новая игра.
            public void actionPerformed(ActionEvent arg0) {
                // Запуск игры.
                snakeGame.start();
                // Забираем фокус у кнопки Новая игра.
                btn1.setFocusable(false);
                // Забираем фокус у кнопки Выход.
                btn2.setFocusable(false);
                // Забираем фокус у кнопки Результаты.
                btn3.setFocusable(false);
                // Отдаем фокус панели.
                panel.setFocusable(true);
                // Запускаем таймер.
                tmUpdate.start();
            }
        });
        add(btn1);

        // Добавление кнопки "Выход".
        btn2 = new JButton();
        btn2.setText("Выход");
        btn2.setForeground(Color.RED);
        btn2.setFont(new Font("Times New Roman", 0, 20));
        btn2.setBounds(630, 80, 150, 40);
        btn2.addActionListener(new ActionListener() {
            // Обработчик события при нажатии на кнопку Выход.
            public void actionPerformed(ActionEvent arg0) {
                // Выход из игры - завершение работы приложения.
                System.exit(0);
            }
        });
        add(btn2);

        // Добавление кнопки "Результаты".
        btn3 = new JButton();
        btn3.setText("Результаты");
        btn3.setForeground(Color.DARK_GRAY);
        btn3.setFont(new Font("Times New Roman", 0, 20));
        btn3.setBounds(630, 130, 150, 40);
        btn3.addActionListener(new ActionListener() {
            // Обработчик события при нажатии на кнопку Результаты.
            public void actionPerformed(ActionEvent arg0) {
                new ResultsFrame();
                // Забираем фокус у кнопки Результаты.
                btn3.setFocusable(false);
            }

        });
        add(btn3);
    }

    // Метод отрисовки.
    public void paintComponent(Graphics g) {
        // Очищение игрового поля.
        super.paintComponent(g);
        // Отрисовка фона.
        g.drawImage(wallpaper, 0, 0, 800, 650, null);
        // Отрисовка игрового поля на основании массива.
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (snakeGame.mas[i][j] != 0) {
                    if (snakeGame.mas[i][j] == 1) {
                        // Выводим голову змейки в ячейку игрового поля.
                        g.drawImage(dot, 10 + j * 20, 10 + i * 20, 20, 20, null);
                    } else if (snakeGame.mas[i][j] == -1) {
                        // Выводим объект для поедания в ячейку игрового поля.
                        g.drawImage(apple, 10 + j * 20, 10 + i * 20, 20, 20, null);
                    } else if (snakeGame.mas[i][j] >= 2) {
                        // Выводим тело змейки.
                        g.drawImage(dot, 10 + j * 20, 10 + i * 20, 20, 20, null);
                    }
                }
            }
        }
        // Отрисовка сетки игрового поля из белых линий.
        g.setColor(Color.GRAY);
        for (int i = 0; i <= 30; i++) {
            // Рисование линий сетки.
            g.drawLine(10 + i * 20, 10, 10 + i * 20, 610);
            g.drawLine(10, 10 + i * 20, 610, 10 + i * 20);
        }
        //Вывод изображения конца игры - при окончании игры.
        if (snakeGame.endGame == true) {
            g.drawImage(endGame, 250, 200, 200, 100, null);
        }
    }
}


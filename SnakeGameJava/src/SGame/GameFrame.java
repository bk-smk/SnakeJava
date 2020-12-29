package SGame;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    // Конструктор класса.
    public GameFrame() {
    // Создание объекта панели и подключения ее к окну.
        GamePanel panel = new GamePanel();
        Container cont = getContentPane();
        cont.add(panel);
    // Заголовок окна.
        setTitle("Змейка");
    // Границы окна: расположение и размеры.
        setBounds(0, 0, 800, 650);
    // Расположение окна с игрой на экране.
        setLocation(300, 50);
    // Операция при закрытии окна - завершение приложения.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Запрет изменения размеров окна.
        setResizable(false);
    // Отображение (показ) окна.
        setVisible(true);
    }
}

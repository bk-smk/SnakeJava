package SGame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ResultsFrame extends JFrame {
    // Модель данных таблицы.
    private DefaultTableModel tableModel;
    // Ссылка на таблицу.
    private JTable table;
    // Ссылки на кнопки.
    private JButton removeLine, removeAll;
    // Наименования заголовков столбцов.
    private Object[] columnsHeader = new String[]{"id игрока", "Имя игрока", "Количество очков"};

    // Конструктор класса.
    public ResultsFrame() {
        super("Результаты");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        // Наполнение модели данными.
        getResults();
        // Создание таблицы на основании модели данных.
        table = new JTable(tableModel);

        // Создание кнопки удаления строки таблицы.
        removeLine = new JButton("Удалить строку");
        removeLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Номер выделенной строки.
                    int idx = table.getSelectedRow();
                    // Получение объекта строки перед удалением строки.
                    Object desc = table.getModel().getValueAt(idx, 2);
                    // Удаление выделенной строки.
                    tableModel.removeRow(idx);
                    // Выполнение запроса, удаление строки из СУБД.
                    Connection conn = DataBase.getDbConnection();
                    String query = "DELETE FROM " +  Const.PLAYER_TABLE + " WHERE " + Const.PLAYERS_SCORE + " = ? LIMIT 1";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, (String) desc);
                    ps.executeUpdate();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Создание кнопки для удаления таблицы полностью.
        removeAll = new JButton("Стереть таблицу");
        removeAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Обнуление рядов модели таблицы.
                    tableModel.setRowCount(0);
                    // Выполнение запроса, удаление всей таблицы из СУБД, обнуление id.
                    Connection conn = DataBase.getDbConnection();
                    String query = "TRUNCATE " +  Const.PLAYER_TABLE;
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.executeUpdate();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Размещение таблиц в панели с блочным расположением.
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table));
        getContentPane().add(contents);
        // Создание объекта панели (для кнопок) и подключения ее к окну.
        JPanel removePanel = new JPanel();
        removePanel.add(this.removeLine);
        removePanel.add(this.removeAll);
        getContentPane().add(removePanel, "South");
        // Вывод окна на экран.
        setSize(400, 630);
        setLocation(450, 60);
        setResizable(false);
        setVisible(true);
        // Убираем фокус, чтобы при закрытии окна работало управление змейкой.
        setFocusable(false);
    }

    // Метод получает данные из СУБД и заносит их в таблицу.
    private void getResults() {
        try {
            // Соединение с СУБД, получение данных из СУБД.
            Connection conn = DataBase.getDbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT *" + " FROM " + Const.PLAYER_TABLE);
            // Присвоение переменной количества столбцов.
            int numColumns = rs.getMetaData().getColumnCount();
            Vector column = new Vector();
            // Добавление столбцов таблицы.
            for (int i = 1; i <= numColumns; i++) {
                column.add(rs.getMetaData().getColumnName(i));
            }
            Vector data = new Vector();
            // Добавление рядов таблицы.
            while (rs.next()) {
                Vector row = new Vector();
                for (int i = 1; i <= numColumns; i++) {
                    row.add(rs.getString(i));
                }
                // Добавление информации из СУБД в таблицу.
                data.add(row);
            }
            // Создание стандартной модели.
            tableModel = new DefaultTableModel(data, column);
            // Определение наименований столбцов.
            tableModel.setColumnIdentifiers(columnsHeader);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
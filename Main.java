import java.io.*;
import java.util.Scanner;

public class Main {

    static final String FILE_NAME = "file.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nМеню:");
            System.out.println("1 Додати кілька рядків");
            System.out.println("2 Показати весь файл з номерами рядків");
            System.out.println("3 Показати діапазон рядків");
            System.out.println("4 Вставити рядок у певну позицію");
            System.out.println("5 Вийти");
            System.out.print("Виберіть дію: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addLines(scanner);
                    break;
                case "2":
                    printAllLines();
                    break;
                case "3":
                    printRange(scanner);
                    break;
                case "4":
                    insertLine(scanner);
                    break;
                case "5":
                    exit = true;
                    System.out.println("Вихід з програми ");
                    break;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
        scanner.close();
    }

    static void addLines(Scanner scanner) {
        System.out.println("Вводьте рядки (порожній рядок для завершення):");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            while (true) {
                String line = scanner.nextLine();
                if (line.isEmpty()) break;
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Рядки додано");
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
        }
    }

    static void printAllLines() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int num = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(num + ": " + line);
                num++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено");
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }

    static void printRange(Scanner scanner) {
        System.out.print("Початковий рядок: ");
        int start = readInt(scanner);
        System.out.print("Кінцевий рядок: ");
        int end = readInt(scanner);

        if (start <= 0 || end < start) {
            System.out.println("Невірний діапазон");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int num = 1;
            while ((line = br.readLine()) != null) {
                if (num >= start && num <= end) {
                    System.out.println(num + ": " + line);
                }
                if (num > end) break;
                num++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не було знайдено");
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }

    static void insertLine(Scanner scanner) {
        System.out.print("Номер рядка для вставки: ");
        int pos = readInt(scanner);
        if (pos <= 0) {
            System.out.println("Невірний номер рядка");
            return;
        }
        System.out.println("Введіть текст для вставки:");
        String newLine = scanner.nextLine();

        String[] lines = readAllLines();
        if (lines == null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
                bw.write(newLine);
                bw.newLine();
                System.out.println("Рядок додано у файл.");
            } catch (IOException e) {
                System.out.println("Помилка запису: " + e.getMessage());
            }
            return;
        }

        int len = lines.length;
        if (pos > len) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                bw.write(newLine);
                bw.newLine();
                System.out.println("Рядок додано в кінець файлу.");
            } catch (IOException e) {
                System.out.println("Помилка запису: " + e.getMessage());
            }
            return;
        }

        String[] newLines = new String[len + 1];
        int idx = 0;
        for (int i = 0; i < len; i++) {
            if (i == pos - 1) {
                newLines[idx++] = newLine;
            }
            newLines[idx++] = lines[i];
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String l : newLines) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("Рядок вставлено.");
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
        }
    }

    static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static String[] readAllLines() {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            while (br.readLine() != null) count++;
        } catch (IOException e) {
            return null;
        }

        if (count == 0) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String[] lines = new String[count];
            for (int i = 0; i < count; i++) {
                lines[i] = br.readLine();
            }
            return lines;
        } catch (IOException e) {
            return null;
        }
    }
}

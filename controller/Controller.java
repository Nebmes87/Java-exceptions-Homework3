package controller;
import view.View;
import model.Model;
import myexception.*;

import java.io.IOException;
import java.util.Scanner;

public class Controller {
    View viewer = new View();

    public Controller() {
    }

    public void run() {
        this.viewer.printInfo("Начало работы!");
        this.viewer.printInfo("Введите фамилию, имя, отчество, дату рождения, номер телефона  и пол, разделенные пробелом.");
        this.viewer.printInfo("Форматы данных:");
        this.viewer.printInfo("Фамилия, имя и отчество - строки");
        this.viewer.printInfo("Дата рождения - строка формата <dd.mm.yyyy>");
        this.viewer.printInfo("Номер телефона - целое беззнаковое число без форматирования");
        this.viewer.printInfo("Пол - символ латиницей f или m");
        this.viewer.printInfo("Ввод \"quit\" или \"exit\" - выход из программы");

        boolean exit = false;
        Scanner scan = new Scanner(System.in);
        String inputLine = "";
        while (!exit) {
            this.viewer.printInfo("Введите данные: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("quit") || inputLine.equals("exit")) {
                this.viewer.printWarning("Аварийное завершение программы");
                break;
            }
            try {
                this.viewer.printInfo(String.format("Вы ввели: %s%n", inputLine));
                Model data = new Model(inputLine);
                this.viewer.printInfo("Данные введены корректно.");
                this.viewer.printInfo(String.format("Данные записаны в структуру: %s", data.toString()));
                data.saveToFile();
                this.viewer.printInfo("Будете еще вводить данные? ([Y]/n)");
                inputLine = scan.nextLine();
                if (!(inputLine.isEmpty() || (inputLine.toUpperCase().charAt(0) == 'Y'))) {
                    exit = true;
                }
            } catch (NotEnoughArgumentException e) {
                this.viewer.printWarning("Введенных данных недостаточно! Введите данные заново!");
            } catch (TooMuchArgumentException e) {
                this.viewer.printWarning("Введенных данных слишком много! Введите данные заново!");
            } catch (FieldSexNotFoundException e) {
                this.viewer.printWarning("Поле \"Пол\" не найдено! Введите данные заново!");
            } catch (FieldPhoneNotFoundException e) {
                this.viewer.printWarning("Поле \"Номер телефона\" не найдено! Введите данные заново!");
            } catch (FieldBirthdayNotFoundException e) {
                this.viewer.printWarning("Поле \"День рождения\" не найдено! Введите данные заново!");
            } catch (FieldLastNameNotFoundException e) {
                this.viewer.printWarning("Поле \"Фамилия\" не найдено! Введите данные заново!");
            } catch (FieldFirstNameNotFoundException e) {
                this.viewer.printWarning("Поле \"Имя\" не найдено! Введите данные заново!");
            } catch (FieldMiddleNameNotFoundException e) {
                this.viewer.printWarning("Поле \"Отчество\" не найдено! Введите данные заново!");
            } catch (IOException e) {
                this.viewer.printWarning(String.format("Ошибка ввода/вывода: %s",e.getMessage()));
            } catch (Exception e) {
                this.viewer.printWarning("Неизвестная ошибка! Введите данные заново!");
            }
        }
        this.viewer.printInfo("Работа завершена.");
    }
}
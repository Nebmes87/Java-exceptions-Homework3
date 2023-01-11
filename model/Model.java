package model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.time.format.DateTimeFormatter;
import java.util.List;

import myexception.*;

public class Model {
    protected String firstName = null;
    protected String lastName = null;
    protected String middleName = null;
    protected LocalDateTime birthday = null;
    protected Long phone = null;
    protected boolean sex = true;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final int MAX_EXCEPTION_COUNT =6;
    Integer bitMask = 0b00000000;
    public Model(String input) throws NotEnoughArgumentException
                                    , TooMuchArgumentException
                                    , FieldSexNotFoundException
                                    , FieldPhoneNotFoundException
                                    , FieldBirthdayNotFoundException
                                    , FieldMiddleNameNotFoundException
                                    , FieldFirstNameNotFoundException
                                    , FieldLastNameNotFoundException {

        List<String> list = Arrays.asList(input.split(" "));
        if (list.size() < 6) {
            throw new NotEnoughArgumentException();
        } else if (list.size() > 6) {
            throw new TooMuchArgumentException();
        } else {
            for (String item : list) {
                if (((bitMask & 0b00100000) == 0) && this.isSex(item)) {
                    this.sex = item.equals("m");
                    bitMask = bitMask | 0b00100000;
                } else if (((bitMask & 0b00010000) == 0) && this.isPhone(item)) {
                    this.phone = Long.parseLong(item);
                    bitMask = bitMask | 0b00010000;
                } else if (((bitMask & 0b00001000) == 0) && this.isDate(item)) {
                    this.birthday = LocalDateTime.parse(String.format("%s 00:00:00", item), formatter);
                    bitMask = bitMask | 0b00001000;
                } else if ((bitMask & 0b00000001) == 0) {
                    this.lastName = item;
                    bitMask = bitMask | 0b00000001;
                } else if ((bitMask & 0b00000010) == 0) {
                    this.firstName = item;
                    bitMask = bitMask | 0b00000010;
                } else if ((bitMask & 0b00000100) == 0) {
                    this.middleName = item;
                    bitMask = bitMask | 0b00000100;
                }
            }

            if (bitMask != Math.pow(2,this.MAX_EXCEPTION_COUNT)) {
                for(int i = 0; i < this.MAX_EXCEPTION_COUNT; i++) {
                    if ((this.bitMask & (int)Math.pow(2,i)) == 0) {
                        switch (i) {
                            case 0 -> {
                                throw new FieldLastNameNotFoundException();
                            }
                            case 1 -> {
                                throw new FieldFirstNameNotFoundException();
                            }
                            case 2 -> {
                                throw new FieldMiddleNameNotFoundException();
                            }
                            case 3 -> {
                                throw new FieldBirthdayNotFoundException();
                            }
                            case 4 -> {
                                throw new FieldPhoneNotFoundException();
                            }
                            case 5 -> {
                                throw new FieldSexNotFoundException();
                            }
                        }
                    }
                }
            }
        }
    }
    public String getFirstName() {
        return (this.firstName != null) ? this.firstName : "";
    }
    public String getLastName() {
        return (this.lastName != null) ? this.lastName : "";
    }
    public String getMiddleName() {
        return (this.middleName != null) ? this.middleName : "";
    }
    public LocalDateTime getBirthday() {
        return (this.birthday != null) ? this.birthday : LocalDateTime.MIN;
    }
    public Long getPhone() {
        return (this.phone != null) ? this.phone : -1;
    }
    public boolean getSex() {
        return this.sex;
    }
    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s"
                , this.getLastName()
                , this.getFirstName()
                , this.getMiddleName()
                , this.getBirthday().format(formatter).toString().substring(0,10)
                , this.getPhone().toString()
                , this.getSex() ? "m" : "f"
                );
    }

    private boolean isSex(String item) {
        return item != null && (item.equals("m") || item.equals("f"));
    }

    private boolean isPhone(String item) {
        return item != null && item.matches("[0-9]+");
    }

    private boolean isDate(String item) {
        try {
            LocalDateTime.parse(String.format("%s 00:00:00", item), formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public void saveToFile() throws IOException {
        try (FileWriter writer = new FileWriter(String.format("%s.txt",this.lastName),true)) {
            writer.write(String.format("%s%n",this.toString()));
            writer.flush();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
package com.roodie.pifagormatrix.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.roodie.pifagormatrix.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Roodie on 15.03.2015.
 */
public class User implements Parcelable {

    public static final String USER_INSTANCE = "USER_INSTANCE";

    private static final long serialVersionUID = 1L;

    private int birthday;
    private int birthmonth;
    private int birthyear;
    private int id;
    private String userName;
    private ArrayList<String> matrixArray;


    public User() {
    }

    public User(int birthday, int month, int year, int id, String userName) {
        this.birthday = birthday;
        this.birthmonth = month;
        this.birthyear = year;
        this.id = id;
        this.userName = userName;
    }

    public int getBirthday() {
        return birthday;
    }

    public int getMonth() {
        return birthmonth;
    }

    public int getYear() {
        return birthyear;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public User(Parcel in) {
        int[] data;
        data = in.createIntArray();
        this.birthday = data[0];
        this.birthmonth = data[1];
        this.birthyear = data[2];
        this.id = data[3];
        this.userName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[]{this.birthday,
                this.birthmonth, this.birthyear, this.id});
        dest.writeString(this.userName);

    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public String toString() {
        return "User{" +
                "birthday=" + birthday +
                ", birthmonth=" + birthmonth +
                ", birthyear=" + birthyear +
                ", id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }


    public String getStringFullBirthday() {
        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, this.getYear());
        instance.set(Calendar.MONTH, this.getMonth());
        instance.set(Calendar.DAY_OF_MONTH, this.getBirthday());
        return new SimpleDateFormat("dd MM yyyy").format(instance.getTime());
    }

    public String[] matrixPythagorasBirthdayNumbers() {
        String[] array = this.getStringFullBirthday().split("\\s+");
        for (String arr : array)
            System.out.println("Birthday data: " + arr);
        return array;
    }


    public String[] matrixPythagorasBasicNumbers() {
        String[] array = new String[4];
        try {
            array[0] = String.valueOf(this.matrixPythagorasFirstNumber());
            array[1] = String.valueOf(this.matrixPythagorasSecondNumber());
            array[2] = String.valueOf(this.matrixPythagorasThirdNumber());
            array[3] = String.valueOf(this.matrixPythagorasFourthNumber());
            for (String arr : array)
                System.out.println("Basic numbers: " + arr);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return array;
    }


    public String[] matrixPythagorasExtraNumbers() {
        String[] array = new String[2];
        try {
            array[0] = String.valueOf(this.matrixPythagorasFifthNumber());
            array[1] = String.valueOf(this.matrixPythagorasSixthNumber());
            for (String arr : array)
                System.out.println("Extra numbers: " + arr);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return array;
    }


    public String[] matrixPythagorasBasicNumbersArray(Utils.ArrayDestination type) {
        final String[] array = {"", "", "", "", "", "", "", "", "", "", "", "", ""};
        final String[] matrixPythagorasBirthdayNumbers = this.matrixPythagorasBirthdayNumbers();
        for (String number : matrixPythagorasBirthdayNumbers) {
            for (int i = 0; i < number.length(); i++) {
                final int numberValueAt = Integer.valueOf(String.valueOf(number.charAt(i)));
                array[numberValueAt] += String.valueOf(number.charAt(i));
            }
        }
        final String[] matrixPythagorasBasicNumbers = this.matrixPythagorasBasicNumbers();
        for (String number : matrixPythagorasBasicNumbers) {
            if (!"null".equals(number)) {
                if (Integer.valueOf(number) >= 10 && Integer.valueOf(number) <= 12) {
                    final int numberValue = Integer.valueOf(number);
                    array[numberValue] = String.valueOf(array[numberValue]) + String.valueOf(numberValue);
                }
                if (Integer.valueOf(number) != 11) {
                    for (int i = 0; i < number.length(); i++) {
                        final int numberValueAt = Integer.valueOf(String.valueOf(number.charAt(i)));
                        array[numberValueAt] = String.valueOf(array[numberValueAt]) + String.valueOf(numberValueAt);
                    }
                }
            }
        }

        for (int j = 0; j < array.length; ++j) {
            if (type == Utils.ArrayDestination.GRID) {
                if (array[j].equals(""))
                    array[j] = "0";
            } else {
                if (array[j].equals(""))
                    array[j] = "0" + (Object) j;
                array[j] = "M" + j + ":" + array[j];
            }
        }

        return array;
    }

    public String[] matrixPythagorasExtraNumbersArray() {
        final String[] array = {"", "", "", "", "", "", "", "", "", "", "", "", ""};
        final String[] matrixPythagorasExtraNumbers = this.matrixPythagorasExtraNumbers();
        for (String number : matrixPythagorasExtraNumbers) {
            if (!"null".equals(number)) {
                if (Integer.valueOf(number) >= 10 && Integer.valueOf(number) <= 12) {
                    int numberValue = Integer.valueOf(number);
                    array[numberValue] = String.valueOf(array[numberValue]) + String.valueOf(numberValue);
                }
                if (Integer.valueOf(number) != 11) {
                    for (int i = 0; i < number.length(); i++) {
                        int numberValueAt = Integer.valueOf(String.valueOf(number.charAt(i)));
                        array[numberValueAt] = String.valueOf(array[numberValueAt]) + String.valueOf(numberValueAt);
                    }
                }
            }
        }
        for (int j = 0; j < array.length; ++j) {
            if (array[j].equals("")) {
                array[j] = "0";
            }
        }
        return array;
    }


    // Get first derived number
    public Integer matrixPythagorasFirstNumber() {
        return Utils.simplifyNumberHalf(this.birthday + (1 + this.birthmonth) * 100 + this.birthyear * 10000);
    }

    // Get second derived number
    public Integer matrixPythagorasSecondNumber() {
        int result = Utils.simplifyNumberHalf(this.matrixPythagorasFirstNumber());
        if (result < 10) return null;
        else return result;
    }

    // Get third derived number
    public Integer matrixPythagorasThirdNumber() {
        final int n = 1 + this.birthmonth;
        final int userBirthDay = this.birthday;
        return Math.abs(this.matrixPythagorasFirstNumber() - (2 * Integer.parseInt(String.valueOf(String.valueOf(userBirthDay).length() == 1 ? "0" : String.valueOf(userBirthDay).charAt(0)))));
    }

    // Get fourth derived number
    public Integer matrixPythagorasFourthNumber() {
        if (this.matrixPythagorasThirdNumber() < 10)
            return null;
        int result = Utils.simplifyNumberHalf(this.matrixPythagorasThirdNumber());
        return result;
    }

    // Get fifth derived number
    public Integer matrixPythagorasFifthNumber() {
        int result = matrixPythagorasFirstNumber() + matrixPythagorasThirdNumber();
        return result;
    }

    // Get sixth derived number
    public Integer matrixPythagorasSixthNumber() {
        int result = (matrixPythagorasSecondNumber() == null ? 0 : matrixPythagorasSecondNumber()) + (matrixPythagorasFourthNumber() == null ? 0 : matrixPythagorasFourthNumber());
        if (result == 0)
            return null;
        return result;
    }


    public ArrayList<String> matrixPythagorasGridDataBasic() {
        final String[] basicNumbersArray = this.matrixPythagorasBasicNumbersArray(Utils.ArrayDestination.GRID);
        final String[] extraNumbersArray = this.matrixPythagorasExtraNumbersArray();


        for (int i = 0; i < basicNumbersArray.length; i++) {
            if (i != 0 && String.valueOf(extraNumbersArray[i].charAt(0)).equals("0")) {
                extraNumbersArray[i] = "";
            } else {
                extraNumbersArray[i] = "(" + extraNumbersArray[i] + ")";
            }
            if (i != 0 && String.valueOf(basicNumbersArray[i].charAt(0)).equals("0")) {
                basicNumbersArray[i] = "- ";
            }
            basicNumbersArray[i] += extraNumbersArray[i];
        }

        ArrayList<String> matrixList = new ArrayList<String>(Arrays.asList(basicNumbersArray));
        return matrixList;
    }

    public String[] matrixPythagorasGridDataExtra() {
        final String[] basicNumbersArray = this.matrixPythagorasBasicNumbersArray(Utils.ArrayDestination.GRID);
        final String[] extraNumbersArray = this.matrixPythagorasExtraNumbersArray();
        String[] basicNumbersSum = new String[4];
        for (int i = 0, j = 0; i < 4; i++) {
            int bas1, bas2, bas3, ext1, ext2, ext3;
            if (i == 3) {
                bas1 = Integer.valueOf(basicNumbersArray[j + 1]);
                bas2 = Integer.valueOf(basicNumbersArray[j + 2]);
                bas3 = Integer.valueOf(basicNumbersArray[j + 3]);
                ext1 = Integer.valueOf(extraNumbersArray[j + 1]);
                ext2 = Integer.valueOf(extraNumbersArray[j + 2]);
                ext3 = Integer.valueOf(extraNumbersArray[j + 3]);


            } else {
                bas1 = Utils.simplifyNumberHalf(Integer.valueOf(basicNumbersArray[j + 1]));
                bas2 = Utils.simplifyNumberHalf(Integer.valueOf(basicNumbersArray[j + 2]));
                bas3 = Utils.simplifyNumberHalf(Integer.valueOf(basicNumbersArray[j + 3]));
                ext1 = Utils.simplifyNumberHalf(Integer.valueOf(extraNumbersArray[j + 1]));
                ext2 = Utils.simplifyNumberHalf(Integer.valueOf(extraNumbersArray[j + 2]));
                ext3 = Utils.simplifyNumberHalf(Integer.valueOf(extraNumbersArray[j + 3]));
            }
            int sumBasic = bas1 + bas2 + bas3;
            if (sumBasic > 12)
                sumBasic = Utils.simplifyNumberHalf(bas1 + bas2 + bas3);
            int sumExtra = ext1 + ext2 + ext3;
            if (sumExtra > 12)
                sumExtra = Utils.simplifyNumberHalf(ext1 + ext2 + ext3);
            basicNumbersSum[i] = (sumBasic != 0 ? String.valueOf(sumBasic) : "") + (sumExtra != 0 ? " (" + String.valueOf(sumExtra) + ")" : "");
            j += 3;
        }
        //ArrayList<String> matrixList = new ArrayList<String>(Arrays.asList(basicNumbersSum));
        return basicNumbersSum;
    }


    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public void setBirthmonth(int birthmonth) {
        this.birthmonth = birthmonth;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}

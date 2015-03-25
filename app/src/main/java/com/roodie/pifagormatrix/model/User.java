package com.roodie.pifagormatrix.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Roodie on 15.03.2015.
 */
public class User implements Parcelable{

    public static final String USER_INSTANCE = "USER_INSTANCE";

    private static final long serialVersionUID = 1L;

    private int birthday;
    private int birthmonth;
    private int birthyear;
    private int id;
    private String userName;

    public User() {}

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
        dest.writeIntArray(new int[] {this.birthday,
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
        instance.set(Calendar.YEAR,this.getYear());
        instance.set(Calendar.MONTH,this.getMonth());
        instance.set(Calendar.DAY_OF_MONTH,this.getBirthday());
        return new SimpleDateFormat("dd MMMM yyyy").format(instance.getTime());
    }

    public String matrixPythagorasAllNumbers() {
        return String.valueOf(this.matrixPythagorasBirthdayNumber()) + " " + this.matrixPythagorasFirstNumber() + " " + this.matrixPythagorasSecondNumber() + " " + this.matrixPythagorasThirdNumber() + " " + this.matrixPythagorasFourthNumber();
    }

    public String[] matrixPythagorasAllNumbersArray() {
        final String[] array = { "", "", "", "", "", "", "", "", "", "" };
        final String matrixPythagorasAllNumbers = this.matrixPythagorasAllNumbers();
        for (int i = 0; i < matrixPythagorasAllNumbers.length(); ++i) {
            final String value = String.valueOf(matrixPythagorasAllNumbers.charAt(i));
            if (!value.equals(" ") && Integer.valueOf(value) != 0) {
                final int intValue = Integer.valueOf(value);
                array[intValue] = String.valueOf(array[intValue]) + String.valueOf(matrixPythagorasAllNumbers.charAt(i));
            }
        }
        for (int j = 1; j <= 9; ++j) {
            if (array[j].equals("")) {
                array[j] = "0" + (Object)j;
            }
        }
        return array;
    }

    public String matrixPythagorasBirthdayNumber() {
        return String.valueOf(String.valueOf(this.birthday)) + " " + String.valueOf(1 + this.birthmonth) + " " + String.valueOf(this.birthyear);
    }

    public String matrixPythagorasFirstNumber() {
        return String.valueOf(this.simplifyNumberHalf(this.birthday + (1 + this.birthmonth) * 100 + this.birthyear * 10000));
    }

    public String matrixPythagorasSecondNumber() {
        int result = this.simplifyNumberHalf(Integer.valueOf(this.matrixPythagorasFirstNumber()));
        //if ( result < 10 )  return String.valueOf(0);
        return String.valueOf(result);
    }

    public String matrixPythagorasThirdNumber() {
        final int n = 1 + this.birthmonth;
        final int userBirthDay = this.birthday;
        return String.valueOf(Math.abs(Integer.valueOf(this.matrixPythagorasFirstNumber()) -(2 * Integer.parseInt(String.valueOf(String.valueOf(userBirthDay).charAt(0))))));
    }

    public String matrixPythagorasFourthNumber() {
        int result = this.simplifyNumberHalf(Integer.valueOf(this.matrixPythagorasThirdNumber()));
        //if ( result < 10 ) return String.valueOf(0);
        return String.valueOf(result);
    }

    public String[] matrixPythagorasGridData() {
        final String[] array = { "", "", "", "", "", "", "", "", "" };
        final String[] matrixPythagorasAllNumbersArray = this.matrixPythagorasAllNumbersArray();
        for (int i = 0; i < -1 + matrixPythagorasAllNumbersArray.length; ++i) {
            if (String.valueOf(matrixPythagorasAllNumbersArray[i + 1].charAt(0)).equals("0")) {
                array[i] = "-";
            }
            else {
                array[i] = matrixPythagorasAllNumbersArray[i + 1];
            }
        }
        return array;
    }


    public int simplifyNumberHalf(final int n) {
        final String string = Integer.toString(n);
        int n2 = 0;
        if (string.length() == 1) {
            return n;
        }
        for (int i = 0; i < string.length(); ++i) {
            n2 += Integer.valueOf(string.substring(i, i + 1));
        }
        String.valueOf(n2);
        return n2;
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

package com.example.lessontestapp.utils;

public class MyTimeToFormat {

    private String hourStr;
    private String minutesStr;
    private String secondStr;
    private int MyDuration;

    public MyTimeToFormat(int duration){
        this.MyDuration = duration;
    }

    public String toFormat(){
        if (MyDuration / 1000 >= 60){
            secondStr = "60";
        }

        return null;
    }

    public String toHourStr(){
        int hour = MyDuration / 1000 / 60 / 60;
        String str = String.valueOf(hour)+"0";
        return str;
    }

    public String toMinutesStr(){
        int minutes = MyDuration / 1000 / 60;
        String str = "0"+String.valueOf(minutes);
        return str;
    }

    public String toSecondStr(){
        double secondFloat = MyDuration / 1000.0 / 60.0;
        int secondInt = MyDuration / 1000 / 60;
        double resultFloat = secondFloat - secondInt;
        String str = String.format("%.2f",resultFloat);
        String str1 = str.substring(2);
        return str1;
    }


}

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Utilitaries {



    public static  long now() {
        return System.currentTimeMillis();
    }


    public static long nowToken() {return now() + timeToken;}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private static long[] findYear(long seconds) {
        int lastCompleteYear = 1970;

        while(true) {

            boolean isBisextile = isBisextile(lastCompleteYear);

            long year = isBisextile ? 31622400L : 31536000L;
            if(seconds < year) {
                break;
            }
            seconds -= year;
            lastCompleteYear++;
        }
        return new long[]{lastCompleteYear,seconds};
    }



    // this method lose effectively miliseconds to keep integers
    public static String convertLongToDate(long time) {
        long seconds = time / 1000;

        long[] infos = findYear(seconds);

        int year = (int)infos[0];

        seconds = infos[1];

        int[] findMonthAndDay = findMonthAndDay(seconds,isBisextile(year));

        return year + "/" + findMonthAndDay[0] + "/" + findMonthAndDay[1];
    }




    private static int[] findMonthAndDay(long seconds,boolean isBisextileYear) {

        int numberOfDays = (int) (seconds / 86400);

        int i;
        for( i = 0; i < 12; ++i) {

            int currentIndex = i;
            if(i == 1 && isBisextileYear) {
                currentIndex = 12;
            }
            int daysPerMonth =  DAYS_PER_MONTH[currentIndex];
            if(numberOfDays >= daysPerMonth ) {
                numberOfDays -= daysPerMonth;

            }else {
              break;
            }
        }
        return new int[]{i+1,numberOfDays+1};
    }


    public static long getDeltaTime(long time) {
     return System.currentTimeMillis() - time;
    }



    private static final int[] DAYS_PER_MONTH = {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 29};


    private static boolean isBisextile(int year) {
        return year % 4 == 0 && year % 100 != 0  || year % 400 == 0;
    }

    public static final long timeToken = 432000000L;




    final static Random rnd = new Random();

    public static double randomChance() {
        return rnd.nextInt(1,101);
    }





}

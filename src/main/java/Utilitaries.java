public class Utilitaries {



    public static  long now() {
        return System.currentTimeMillis();
    }







    // this method lose effectively miliseconds to keep integers
    public static String convertLongToDate(long time) {
        long seconds = time / 1000;

       int lastCompleteYear = 1971;

     while(true) {

      boolean isBisextile = isBisextile(lastCompleteYear);

         long seuilAnnee = isBisextile ? 31622400L : 31536000L;
         if(seconds < seuilAnnee) {
             break;
         }
         seconds -= seuilAnnee;
        lastCompleteYear++;
     }

     int currentYear = lastCompleteYear + 1;

     int daysInCurrentYear = (int) seconds * 60 * 24;


     int month = 1;
     for(int i = 0; i < 12; ++i) {
         if(i == 1) {


             
         }


     }
    }


    private static final int[] JOURS_PAR_MOIS = {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 29};


    private static boolean isBisextile(int year) {
        return year % 4 == 0 && year % 100 != 0  || year % 400 == 0;
    }







    private static long daysToSeconds(int days) {
        return (long) days * 24 * 60;
    }
    private static long secondsToHours(long seconds) {
        return  seconds / (long)Math.pow(1000,2);
    }


}

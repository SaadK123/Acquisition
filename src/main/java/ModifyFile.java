import java.io.*;
import java.util.Scanner;

public class ModifyFile {

     static Scanner scanner;
    public Tuple<String,String> readFileForTokenAndPlayerId(String fileAddress)  {
      FileInputStream reader =  openFile(fileAddress,true);

      int currentASCII;


      return new Tuple<>(values[0],values[1]);
    }


    public




    private void CloseFileAccess(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new AcquisitionException("cannot close file");
        }
    }

    private <T> T  openFile(String fileAddress,boolean isReadMode) {


        final AcquisitionException AE  = new AcquisitionException("cannot open the file");
        if(isReadMode)
            try (FileInputStream reader = new FileInputStream(fileAddress)) {
                return (T) reader;
            } catch (IOException e) {
                throw AE;
            }


        try(FileOutputStream placer = new FileOutputStream(fileAddress)) {
            return  (T) placer;
        }catch (IOException e) {
            throw AE;
        }
    }
}

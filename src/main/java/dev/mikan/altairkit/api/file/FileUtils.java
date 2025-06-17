package dev.mikan.altairkit.api.file;




import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class FileUtils {



    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        InputStream inputStream = source;

        try {
            copyToFile(inputStream, destination);
        } catch (Throwable var6) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }

            throw var6;
        }

        if (inputStream != null) {
            inputStream.close();
        }

    }






    public static void copyToFile(InputStream inputStream, File file) throws IOException {
        OutputStream out = OutputStreamUtil.newOutputStream(file, false);

        try {
            copy(inputStream, out);
        } catch (Throwable var6) {
            if (out != null) {
                try {
                    out.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }

            throw var6;
        }

        if (out != null) {
            out.close();
        }

    }


    public static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        long count = copyLarge(inputStream, outputStream);
        return count > 2147483647L ? -1 : (int)count;
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        return copy(inputStream, outputStream, 8192);
    }


    public static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] buffer) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(outputStream, "outputStream");

        long count;
        int n;
        for(count = 0L; -1 != (n = inputStream.read(buffer)); count += (long)n) {
            outputStream.write(buffer, 0, n);
        }

        return count;
    }


    public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        return copyLarge(inputStream, outputStream, byteArray(bufferSize));
    }

    public static byte[] byteArray(int size) {
        return new byte[size];
    }



}

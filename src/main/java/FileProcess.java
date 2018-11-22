import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileProcess {
    /**
     * 将文件随机分割，在此工程中没有用
     * @param srcpath
     * @param despath
     * @param nums
     */
    public static void cut(String srcpath,String despath,int nums) {
        File file = new File(srcpath);
        int num = nums;//分割文件的数量
        long lon = file.length() / 10L + 1L;//使文件字节数+1，保证取到所有的字节
        try {
            RandomAccessFile raf1 = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[1024];//值设置越小，则各个文件的字节数越接近平均值，但效率会降低，这里折中，取1024
            int len = -1;
            for (int i = 0; i < num; i++) {
                String name = despath + i + ".txt";
                File file2 = new File(name);
                RandomAccessFile raf2 = new RandomAccessFile(file2, "rw");
                while ((len = raf1.read(bytes)) != -1){//读到文件末尾时，len返回-1，结束循环
                    raf2.write(bytes, 0, len);
                    if (raf2.length() > lon)//当生成的新文件字节数大于lon时，结束循环
                        break;
                }
                raf2.close();
            }
            raf1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

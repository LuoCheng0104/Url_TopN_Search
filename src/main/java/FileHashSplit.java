import java.io.*;

public class FileHashSplit {
    /**
     * 将大文件通过读入，根据url的hash值的后三位，将出现url
     * 分别放入不同的1000个文件，这样保证了同一个url
     * 在同一个文件中
     * @param src 大文件的路径
     * @param des 小文件的保存路径
     * @throws IOException
     */
    public static void filesplit(String src,String des) throws IOException {
        //读文件
        File file=new File(src);
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        String line=null;
        while((line = br.readLine()) != null) {
            //同时写多个文件
            String url=line.split("\t")[0];//取出url
            int hashCode=Math.abs(url.hashCode());
            String index=null;
            if (hashCode < 1000) {
                 index = String.format("%03d", hashCode);
                 //System.out.println(str);
            }else {
                index=String.valueOf(hashCode);
                index=index.substring(index.length() - 3);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(des+"\\"+index+".txt"), true));//根据索引创建不同的对象追加写入
            writer.append(url);
            writer.newLine();
            writer.flush();
        }
        fr.close();
        br.close();

    }
}

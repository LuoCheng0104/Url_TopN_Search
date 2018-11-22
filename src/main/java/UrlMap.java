import java.io.*;
import java.util.*;

public class UrlMap {
    /**
     * 对小文件里出现的url进行计数，结果保存
     * @param src 小文件的路径
     * @param desc 结果保存的路径
     */
    public static void urlmap(String src,String desc){
        HashMap<String,Integer> map=new HashMap<String, Integer>();
        File file=new File(src);
        File fileto = new File(desc);
        FileWriter fw =null;
        BufferedWriter writer = null;
        try {
           FileReader fr=new FileReader(file);
           BufferedReader br=new BufferedReader(fr);
           String line=null;
            while((line = br.readLine()) != null) {
                String url=line.split("\t")[0];
                if(!map.containsKey(url)){
                    map.put(url,1);
                }else{
                    int i=map.get(url)+1;
                    map.remove(url);
                    map.put(url,i);
                }
            }
            Iterator<Map.Entry<String,Integer>> it=map.entrySet().iterator();
            fw = new FileWriter(fileto,true);
            writer = new BufferedWriter(fw);
            while(it.hasNext()){
                Map.Entry<String,Integer> en=it.next();
                //System.out.println("("+en.getKey()+","+en.getValue()+")");
                writer.append(en.getKey()+" "+en.getValue());
                //writer.newLine();//换行
            }
            writer.flush();
            fr.close();
            br.close();
            fw.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 找出每个文件里的Top100,并保存
     * @param src
     * @param des
     * @param K
     * @throws IOException
     */
    public static void findTop(String src,String des,int K) throws IOException {
        File file=new File(src);
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        int[] arr=null;
        int[] top=null;
        int arrindex=0;
        String line=null;
        while ((line = br.readLine()) != null){
            arr[arrindex++]=Integer.parseInt(line.split(" ")[1]);
        }
        top=MinHeap.topK(arr,K);
        br.reset();
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(des), true));//将topk重新写入文件
        for(int i=0;i<K;i++){
            while ((line = br.readLine()) != null&&(Integer.parseInt(line.split(" ")[1])==top[i])){
                writer.append(line);
                writer.newLine();
                writer.flush();
            }
        }
        fr.close();
        br.close();
        writer.close();
    }

    /**
     * 将两个文件的top100结果合并,文件里面已经是有序的了可以用归并排序，保存
     * @param src1
     * @param src2
     * @param des
     */
    public static void merge(String src1,String src2,String des) throws IOException {
        ArrayList<String> merge=new ArrayList<String>();//存储合并的结果
        //文件一
        File file=new File(src1);
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        //文件二
        File file2=new File(src2);
        FileReader fr2=new FileReader(file);
        BufferedReader br2=new BufferedReader(fr);
        String line1=null;
        String line2=null;
        line1= br.readLine();
        line2= br.readLine();
        while(line1!=null&&line2!=null) {
            if (Integer.parseInt(line1.split(" ")[1]) > Integer.parseInt(line2.split(" ")[1])) {
                merge.add(line2);
                line2 = br2.readLine();
            } else {
                merge.add(line1);
                line1 = br.readLine();
            }
        }
        while (line1==null&&line2!=null){
            merge.add(line2);
            line2=br2.readLine();
        }
        while (line2==null&&line1!=null){
            merge.add(line1);
            line1=br.readLine();
        }
        //从后往前遍历100个存储
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(des), true));//将topk重新写入文件
        int count =100;
        ListIterator<String> it=merge.listIterator();
        while (it.hasNext()) {
           // System.out.println(it.next());将迭代器的移到最后
        }
        while (it.hasPrevious()&&count>0) {
            count--;
            writer.append(it.previous());
            writer.newLine();
            writer.flush();
        }
        br.close();
        br2.close();
        writer.close();
        fr.close();
        fr2.close();
    }

    /**
     * 合并的过程
     * @param output
     * @throws IOException
     */
    public static void mergeProcess(String output) throws IOException {
        /*********MergeProcess********/
        //对1000文件进行merge成500个 编号:000~499
        for(int i=0;i<1000;i+=2){
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",i+1);
            String str3=String.format("%03d",i/2);
            UrlMap.merge(output+"\\top"+str1+".txt",output+"\\top"+str2+".txt",output+"\\top 500"+str3+".txt");
        }
        //对500文件进行merge成250个 编号:000~249
        for(int i=0;i<500;i+=2){
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",i+1);
            String str3=String.format("%03d",i/2);
            UrlMap.merge(output+"\\top 500"+str1+".txt",output+"\\top 500"+str2+".txt",output+"\\top 250"+str3+".txt");
        }
        //对250文件进行merge成125个 编号:000~124
        for(int i=0;i<250;i+=2){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",i+1);
            String str3=String.format("%03d",i/2);
            UrlMap.merge(output+"\\top 250"+str1+".txt",output+"\\top 250"+str2+".txt",output+"\\top 125"+str3+".txt");
        }
        //对125文件进行merge成64个 编号:000~62
        for(int i=0;i<=124/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",124-i);
            String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 125"+str1+".txt",output+"\\top 125"+str2+".txt",output+"\\top 63"+str3+".txt");
        }
        //对63文件进行merge成32个 编号:000~31
        for(int i=0;i<=62/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",62-i);
            String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 63"+str1+".txt",output+"\\top 63"+str2+".txt",output+"\\top 32"+str3+".txt");
        }
        //对32文件（000~031）进行merge成16个 编号:000~15
        for(int i=0;i<=31/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",31-i);
            String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 32"+str1+".txt",output+"\\top 32"+str2+".txt",output+"\\top 16"+str3+".txt");
        }
        //对16文件（000~015）进行merge成8个 编号:000~008
        for(int i=0;i<=15/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",15-i);
            String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 16"+str1+".txt",output+"\\top 16"+str2+".txt",output+"\\top 8"+str3+".txt");
        }
        //对8文件（000~007）进行merge成4个 编号:000~004
        for(int i=0;i<=7/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",7-i);
            String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 8"+str1+".txt",output+"\\top 8"+str2+".txt",output+"\\top 4"+str3+".txt");
        }
        //对4文件（000~03）进行merge成2个 编号:000~001
        for(int i=0;i<=3/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",3-i);
            String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 4"+str1+".txt",output+"\\top 4"+str2+".txt",output+"\\top 2"+str3+".txt");
        }
        //对2文件（000~001）进行merge成2个 编号:000~000
        for(int i=0;i<=1/2;i++){ //对每个小文件找出top100;
            String str1=String.format("%03d",i);
            String str2=String.format("%03d",1-i);
            //String str3=String.format("%03d",i);
            UrlMap.merge(output+"\\top 2"+str1+".txt",output+"\\top 2"+str2+".txt",output+"\\finalTop.txt");
        }
    }
}

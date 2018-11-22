import java.io.IOException;

public class TopSearch{
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.print("Usage:<# input file path> <# output file path> <# K>");
            System.exit(1);
        }
        String input=args[0];
        String output=args[1];
        int k=Integer.valueOf(args[2]);
        //将大文件hash分割成小文件
        FileHashSplit.filesplit(input,output);
        // 对每个小文件进行Map成“url num”的形式
        for(int i=0;i<1000;i++){
            String str=String.format("%03d",i);
            UrlMap.urlmap(output+str+".txt",output+"\\map"+str+".txt");
        }
        //对每个小文件找出top100   编号:000~999
        for(int i=0;i<1000;i++){
            String str=String.format("%03d",i);
            UrlMap.findTop(output+"\\map"+str+".txt",output+"\\top"+str+".txt",k);
        }
        //开始结果合并
        UrlMap.mergeProcess(output);
    }
}

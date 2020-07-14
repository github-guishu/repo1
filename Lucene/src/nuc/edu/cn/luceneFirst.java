package nuc.edu.cn;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName luceneFirst
 * @Author liuxiaobo
 * @Date 2020/6/18&20:13
 * @Version 1.0
 **/
public class luceneFirst {
     /*
     FSDirectory是用来读取磁盘上的文件夹的  现在用来指定索引库的位置
      */
    @Test
    public void createIndex() throws IOException {
        //1.创建一个directory对象 指定索引库保存的位置
        //把索引库创建在磁盘里
        Directory directory = FSDirectory.open(new File("E:\\team\\indexLibrary").toPath());
        //把索引库创建在内存  Directory directory = new RAMDirectory();
        //2.基于Directory对象创建IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig());
        //3.获取磁盘上的文件，对应每个文件创建一个文档对象
        File directorys= new File("E:\\lucene\\lucencetexts");
        File[] files = directorys.listFiles();
        //遍历文件
        for (File f:
             files) {
            //获取文件的名字
            String fileName = f.getName();
            //获取文件的内容
            String filePath = f.getPath();
            //获取文件的内容
            String fileContent = FileUtils.readFileToString(f,"utf-8");
            //获取文件的大小
            long fileSize = FileUtils.sizeOf(f);
            Field fieldName = new TextField("name",fileName, Field.Store.YES);
            Field fieldPath = new TextField("path",filePath, Field.Store.YES);
            Field fieldContent = new TextField("content",fileContent, Field.Store.YES);
            Field fieldSize = new TextField("size",fileSize+"", Field.Store.YES);
            //4.向文档对象中添加域
            Document document = new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            //5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        //关闭IndexWriter
        indexWriter.close();
    }

    @Test
    public void queryIndex() throws IOException {
        //1.创建一个Directory对象，指明索引库的位置
        //2.创建一个IndexReader对象
        //3.创建一个IndexSearcher对象，
        //4.创建一个query对象 TermQuery对象
        //5.执行查询，返回TopDocs对象
        //6.取查询结果的总记录数  TopDocs对象的totalHits方法
        //7.取文档列表     TopDocs对象的scoreDocs方法
        //8.打印文档内容
        //9.关闭IndexReader对象
        Directory directory = FSDirectory.open(new File("E:\\team\\indexLibrary").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        Query query =  new TermQuery(new Term("content","spring"));
        TopDocs docs = searcher.search(query, 10);
        long totalHits = docs.totalHits;
        System.out.println("总记录数为："+totalHits);
         ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (ScoreDoc doc:
             scoreDocs) {
            //取读取到的文件id
            int id = doc.doc;
            //通过id获取文件
            Document document = searcher.doc(id);
            System.out.println(document.getField("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println("----------------------------------------");
        }
        indexReader.close();
    }
}

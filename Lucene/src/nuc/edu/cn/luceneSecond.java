package nuc.edu.cn;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.sql.SQLOutput;

/**
 * @ClassName luceneSecond
 * @Author liuxiaobo
 * @Date 2020/6/25&9:05
 * @Version 1.0
 **/
public class luceneSecond {

    public void createIndexLibrary() throws Exception{
        //指定索引库的位置
        //创建IndexWriter对象
        //从磁盘读取文件
        //遍历文件
        //创建域对象
        //创建document对象
        //将域对象到document中
        //添加document对象到索引库中
        //关闭IndexWriter

    }
    @Test
    public void findIndexLibrary() throws Exception{
        //找到索引库的位置
        Directory directory = FSDirectory.open(new File("E:\\JavaTools\\team\\indexLibrary").toPath());
        //创建IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建IndexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("context","spring"));
        //执行查询  查询到所有的文件
        TopDocs topDocs = indexSearcher.search(query, 10);
        long totalHits = topDocs.totalHits;
        System.out.println("总记录数:"+totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //遍历结果
        for (ScoreDoc doc:scoreDocs
             ) {
            //获取域的ID
            int id = doc.doc;
            //用IndexReader通过域的id读取域的值
            Document document = indexSearcher.doc(id);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println("-----------------------------------------");
        }
        //关闭IndexReader
        indexReader.close();
    }
}
